package xyz.isnull.blog.core.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="t_user")
public class User {

    /** GenerationType:
        TABLE：使用一个特定的数据库表格来保存主键。
        SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
        IDENTITY：主键由数据库自动生成（主要是自动增长型）
        AUTO：主键由程序控制。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = {@JoinColumn(name = "user_id")},
                inverseJoinColumns = {@JoinColumn(name = "roles_id")})
    private List<Role> roles;

}
