package com.BEM.JPA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data /*
provides Getters, Setters, toString(), equals(object o), hashCode(), RequiredArgsConstructor
*/
@Entity
@Table(name = "student_table",
        uniqueConstraints = @UniqueConstraint(
                name = "email",
                columnNames = "email"
        )
)
@NoArgsConstructor /* hibernate creates entity instance through reflection when loading data from the database,
 Many frameworks rely on the existence of no-args constructor.
 Reflection is a feature in java that allows programs to inspect and manipulate the runtime behavior of applications.With reflection,
 Hibernate can discover information about the entity class, such as its constructors, methods, and fields,
 and can dynamically create instances of the class.
 1) Hibernate uses reflection to find the no-argument constructor and instantiate the object
 2) Hibernate then populates the fields of the object with the values from the database
*/
@AllArgsConstructor /*
Provides a quick and easy way to create fully-initialized Student instances,
which can be particularly helpful for testing,
or whenever you need to create objects with all their properties set in a single step
*/
@Builder
public class Student {

    @Id
    @SequenceGenerator(name = "std", sequenceName = "std", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "std")
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    @Embedded
    private Guardian guardian;

    @ManyToMany(mappedBy = "student")
    private List<Course> courses;



}
