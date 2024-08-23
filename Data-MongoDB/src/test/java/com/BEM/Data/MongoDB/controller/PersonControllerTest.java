package com.BEM.Data.MongoDB.controller;

import com.BEM.Data.MongoDB.collection.Address;
import com.BEM.Data.MongoDB.collection.Person;
import com.BEM.Data.MongoDB.repository.PersonRepo;
import com.BEM.Data.MongoDB.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonRepo personRepo;

    @Test
    void shouldSavePerson() throws Exception {
        String id = "1";
        Person person = Person.builder()
                .firstName("AnyName")
                .lastName("AnyLastName")
                .build();

        // Convert the Person object to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String personJson = objectMapper.writeValueAsString(person);

        // Mock the service call
        when(personService.save(any(Person.class))).thenReturn(id);

        // Perform the POST request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(id)); // Verify that the response matches the expected ID
    }

    @Test
    void shouldReturnPersonsStartingWith() throws Exception {
        // given
        String namePrefix = "AnyName";
        Person person1 = Person.builder().firstName("AnyName1").lastName("LastName1").build();
        Person person2 = Person.builder().firstName("AnyName2").lastName("LastName2").build();
        List<Person> persons = Arrays.asList(person1, person2);

        // when
        when(personService.getPersonWith(namePrefix)).thenReturn(persons);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/person")
                        .param("name", namePrefix)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("AnyName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("AnyName2"));
    }

    @Test
    void shouldReturnAllPersons() throws Exception {
        // given
        Person person1 = Person.builder().firstName("FirstName1").lastName("LastName1").build();
        Person person2 = Person.builder().firstName("FirstName2").lastName("LastName2").build();
        List<Person> persons = Arrays.asList(person1, person2);

        // when
        when(personRepo.findAll()).thenReturn(persons);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/person/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("FirstName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("FirstName2"));
    }

}