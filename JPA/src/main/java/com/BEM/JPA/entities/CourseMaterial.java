package com.BEM.JPA.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseMaterial {

    @Id
    @GeneratedValue
    private Integer courseMaterialId;

    private String materialName;

   /* @OneToOne(
            cascade = CascadeType.ALL, //The cascade operations determine how changes to the parent entity will affect the child entities.
            fetch = FetchType.LAZY // entities are fetched on-demand, EAGER fetches all the data immediately along with the parent entity.
    )
    @JoinColumn(name = "course_id",referencedColumnName = "courseId") // Uni directional One to One mapping, @PrimaryKeyJointColumn used to directly set the column as primary key
    private Course course;
  */
    @OneToOne(cascade = CascadeType.ALL,
              fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;


}
