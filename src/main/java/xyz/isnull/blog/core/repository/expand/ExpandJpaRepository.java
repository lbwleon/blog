package xyz.isnull.blog.core.repository.expand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import xyz.isnull.blog.core.repository.parameter.Operator;
import xyz.isnull.blog.core.repository.parameter.Predicate;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface ExpandJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>{

    T findOne(String condition, Object... objects);

    List<T> findAll(String condition, Object... objects);

    List<T> findAll(Iterable<Predicate> predicates, Operator operator);

    List<T> findAll(Iterable<Predicate> predicates, Operator operator, Sort sort);

    Page<T> findAll(Iterable<Predicate> predicates, Operator operator, Pageable pageable);

    Page<T> findAllByNativeSql(String sql,Pageable pageable,Class clz,Object[] objects);

    List<T> findAllByNativeSql(String sql,Class clz, Object[] objects);

}
