package com.BEM.Data.MongoDB.service;

import com.BEM.Data.MongoDB.collection.Person;
import com.BEM.Data.MongoDB.repository.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSavePerson() {
        // Given
        String id = "1";
        Person person = Person.builder()
                .firstName("AnyName")
                .lastName("AnyLastName")
                .build();
        when(personRepo.save(any(Person.class))).thenReturn(Person.builder().personID(id).build());

        // When
        String returnedId = personService.save(person);

        // Then
        assertEquals(id, returnedId);
        verify(personRepo,times(1));
    }

    @Test
    void shouldReturnPersonsWithNameStartingWith() {
        // Given
        String namePrefix = "AnyName";
        Person person1 = Person.builder()
                .firstName("AnyNameOne")
                .lastName("LastNameOne")
                .build();
        Person person2 = Person.builder()
                .firstName("AnyNameTwo")
                .lastName("LastNameTwo")
                .build();

        List<Person> expectedPersons = List.of(person1, person2);

        // Mock the findByFirstNameStartsWith method
        when(personRepo.findByFirstNameStartsWith(namePrefix)).thenReturn(expectedPersons);

        // When
        List<Person> actualPersons = personService.getPersonWith(namePrefix);

        // Then
        assertEquals(expectedPersons, actualPersons);
    }


}