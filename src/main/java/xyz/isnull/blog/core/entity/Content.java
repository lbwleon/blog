package xyz.isnull.blog.core.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_content")
@Data
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // 内容缩略
    private String slug;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifydate;

    private String contents;

    //作者
    private long author_id;

    //点击数
    private Integer hits;

    //类别
    private String type;

    //内容类型 md、html
    private String fmt_type;

    //缩略图
    private String thumb_img;

    //标签
    private String tags;

    //分类列表
    private String categories;

    private String status;

    //评论数
    private Integer comments_num;

    // 是否允许评论
    private Boolean allow_comment;

    // 是否允许ping
    private Boolean allow_ping;

    // 允许出现在聚合中
    private Boolean allow_feed;


}
