package xyz.isnull.blog.core.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_metas")
@Data
public class Metas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**标签的分类 link category tags*/
    private String type;

}
