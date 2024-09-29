package com.BEM.BookManagement.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverrides({
        @AttributeOverride(
                name = "Name",
                column = @Column(name = "Guardian_Name")
        ),
        @AttributeOverride(
                name = "Email",
                column = @Column(name = "Guardian_Email")
        ),
        @AttributeOverride(
                name = "Phone",
                column = @Column(name = "Guardian_Mobile")
        )
})
@Builder
public class Guardian {

    private String Name;
    private String Email;
    private String Phone;

}
