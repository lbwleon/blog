package xyz.isnull.blog.core.repository.expand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import xyz.isnull.blog.core.exception.DataException;
import xyz.isnull.blog.core.repository.parameter.Operator;
import xyz.isnull.blog.core.repository.parameter.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;

public class MyExpandJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ExpandJpaRepository<T, ID>{


    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager entityManager;

    public MyExpandJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    @Override
    public T findOne(String condition, Object... objects) {
        if(isEmpty(condition)){
            throw new NullPointerException("条件不能为空");
        }
        T result = null;
        //TODO exception
        result = (T) createQuery(condition,objects).getSingleResult();
        return result;
    }

    @Override
    public List<T> findAll(String condition, Object... objects) {
        return null;
    }

    @Override
    public List<T> findAll(Iterable<Predicate> predicates, Operator operator) {
        return null;
    }

    @Override
    public List<T> findAll(Iterable<Predicate> predicates, Operator operator, Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Iterable<Predicate> predicates, Operator operator, Pageable pageable) {
        return null;
    }


    /**
     * 声明entityClass的查询
     */
    private Query createQuery(String condition, Sort sort, Object[] objects) {

        JpqlQueryHolder queryHolder = new JpqlQueryHolder(condition,sort,objects);

        return queryHolder.createQuery();
    }

    /**
     * 声明entityClass的查询
     */
    private Query createQuery(String condition, Object[] objects) {
        return createQuery(condition, null, objects);
    }

    @Override
    public Page<T> findAllByNativeSql(String sql, Pageable pageable, Class clz, Object[] objects) {
        Query nativeQuery = entityManager.createNativeQuery(sql, clz);
        if(objects != null && objects.length != 0){
            for (int i = 0; i < objects.length; i++) {
                nativeQuery.setParameter(i+1, objects[i]);
            }
        }

        List<T> resultList = nativeQuery.getResultList();

        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<T>(resultList, pageable, Long.valueOf(resultList.size()));
    }

    @Override
    public List<T> findAllByNativeSql(String sql, Class clz, Object[] objects) {
        Query query = entityManager.createNativeQuery(sql, clz);
        if(objects != null && objects.length != 0){
            for (int i = 0; i < objects.length; i++) {
                query.setParameter(i+1, objects[i]);
            }
        }
        return query.getResultList();
    }

    private class JpqlQueryHolder{

        private final String ALIAS = "x";

        private final String FIND_ALL_QUERY_STRING = "from %s " + ALIAS;

        private final String[] IGNORE_CONSTAINS_CHARSEQUENCE = {"where", "WHERE", "from","FROM"};

        private String condition = null;

        private Sort sort;

        private Object[] objects;

        private Iterable<Predicate> predicates;

        private Operator operator = Operator.AND;

        /**
         * 这是带 查询条件 连接条件  排序的构造方法
         * @param predicates   封装的类 返回的结果类似 id=1 name like %abc%
         * @param operator   两个值 and or
         * @param sort       字段排序  eg.   id asc
         */
        private JpqlQueryHolder(Iterable<Predicate> predicates, Operator operator, Sort sort){
            this.predicates = predicates;
            this.operator = operator;
            this.sort = sort;
        }

        /**
         * 同上 不带排序
         * @param predicates
         * @param operator
         */
        private JpqlQueryHolder(Iterable<Predicate> predicates , Operator operator ) {
            this.operator = operator;
            this.predicates = predicates;
        }

        private JpqlQueryHolder(String condition, Sort sort, Object[] objects){
            this(condition,objects);
            this.sort = sort;
        }

        private JpqlQueryHolder(String condition, Object[] objects){
            if(startsWithAny(condition, IGNORE_CONSTAINS_CHARSEQUENCE)){
                throw new DataException(DataException.noPermission,"查询条件中只能包含WHERE条件表达式!");
            }
            this.condition = trimToNull(condition);
            this.objects = objects;
        }

        private Query createQuery(){
            StringBuilder sql = new StringBuilder();
            // select x from table
            sql.append(QueryUtils.getQueryString(FIND_ALL_QUERY_STRING, entityInformation.getEntityName()))
                //where
                .append(applyCondition());
            //用QueryUtils 创建一个带参数的sql 然后通过applyQueryParameter() 为参数设置值
            Query query = entityManager.createQuery(QueryUtils.applySorting(sql.toString(), sort, ALIAS));
            applyQueryParameter(query);
            return query;
        }

        private String applyCondition(){
            List<String> conditions = map2Conditions();
            if(condition != null){
                conditions.add(condition);
            }
            condition = join(conditions, " " + operator.name() + " ");
            return  isEmpty(condition) ? "" : condition;
        }

        private List<String> map2Conditions(){
            if(predicates == null || !predicates.iterator().hasNext()){
                return new ArrayList<String>();
            }
            List<String> conditions = new ArrayList<String>();
            Iterator<Predicate> iterator = predicates.iterator();
            int i = 0;
            while (iterator.hasNext()){
                Predicate predicate = iterator.next();
                if(predicate.getKey() == null){
                    continue;
                }
                conditions.add(predicate.toCondition(String.valueOf(i++)));
            }
            return conditions;
        }

        private void applyQueryParameter(Query query){
            //TODO  上面是conditiions add
            if(objects != null){
                for (int i = 0; i < objects.length; i++) {
                    query.setParameter(i,objects[i]);
                }
            }

            if(predicates != null || predicates.iterator().hasNext()){
                Iterator<Predicate> iterator = predicates.iterator();
                int i = 0;
                while (iterator.hasNext()){
                    Predicate predicate = iterator.next();
                    if(predicate.getKey() == null){
                        continue;
                    }
                    predicate.setParameter(query,String.valueOf(i++));
                }
            }
        }
    }
}
