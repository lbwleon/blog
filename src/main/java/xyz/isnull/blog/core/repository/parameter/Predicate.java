package xyz.isnull.blog.core.repository.parameter;

import lombok.Data;

import javax.persistence.Query;
import java.io.Serializable;

@Data
public class Predicate implements Serializable {

    /**
     * 字段
     */
    private String key;

    private Object value;

    private LinkEnum link = LinkEnum.EQ;

    public Predicate(String key, Object value){
        this.key = key;
        this.value = value;
    }

    public Predicate(String key, Object value, LinkEnum link){
        this.key = key;
        this.value = value;
        this.link = link;
    }

    public String toCondition(String index){
        return  link.toCondition(key, index);
    }

    public void setParameter(Query query, String index){
        link.setParameter(this, query, index);
    }
}
