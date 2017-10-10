package xyz.isnull.blog.core.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
