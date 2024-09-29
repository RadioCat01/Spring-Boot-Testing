package com.BEM.BookManagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "teacher",cascade = CascadeType.ALL) /* MappedBy annotation and JointColumn annotation should not be in same relationship */
    private List<Course> courses;

}
