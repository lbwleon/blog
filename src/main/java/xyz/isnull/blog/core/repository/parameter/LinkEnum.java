package xyz.isnull.blog.core.repository.parameter;

import org.springframework.util.StringUtils;

import javax.persistence.Query;

public enum LinkEnum {

    EQ("="),

    GE(">="),

    GT(">"),

    IN("IN"),

    LIKE("LIKE"),

    LE("<="),

    LT("<"),

    NE("<>"),

    NIN("NOT IN"),

    EN("IS NILL"){

        @Override
        public String toCondition(String key, String index ) {
            return key+" "+getName();
        }

        @Override
        public void setParameter(Predicate predicate, Query query, String index){}
    },

    NN("IS NOT NULL"){
        @Override
        public String toCondition(String key, String index ) {
            return key+" "+getName();
        }

        @Override
        public void setParameter(Predicate predicate, Query query, String index){}
    };

    private String name;

    LinkEnum(String name){this.name = name;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toCondition(String key, String index){
        return key + " " + this.name + ":" + StringUtils.replace(key, ".", "_")+index;
    }

    public void setParameter(Predicate predicate, Query query, String index){
        query.setParameter(StringUtils.replace(predicate.getKey() + index, ".", "_"), predicate.getValue());

    }
}
