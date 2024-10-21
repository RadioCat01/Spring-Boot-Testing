package com.Blog.Blog.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class user{

    @Id
    @GeneratedValue
    private Integer id;
    private String email;
    private String name;
    private int age;
}

