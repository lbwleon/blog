package xyz.isnull.blog.core.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_relationships")
@Data
public class Relationships {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private Long mid;

    private String type;

    public Relationships() {
    }

    public Relationships(Long cid, Long mid, String type) {
        this.cid = cid;
        this.mid = mid;
        this.type = type;
    }
}
