package com.Blog.Blog.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(length = 5000)
    private String content;
    private String postedBy;
    private String image;
    private Date date;
    private int likeCount;
    private int viewCount;
    private List<String> tags;

}
