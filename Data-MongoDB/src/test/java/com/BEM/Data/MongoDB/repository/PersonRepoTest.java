package com.BEM.Data.MongoDB.repository;

import com.BEM.Data.MongoDB.collection.Address;
import com.BEM.Data.MongoDB.collection.Person;
import com.BEM.Data.MongoDB.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonRepoTest {

    @Autowired
    private PersonRepo personRepo;

    @Test
    public void save(){

        Address add = Address.builder()
                .address1("aa")
                .address2("bb")
                .city("cc")
                .build();

        Person per = Person.builder()
                .firstName("name")
                .lastName("surname")
                .addressList(List.of(add))
                .build();
        personRepo.save(per);
    }

    @Test
    public void findALl(){
        List<Person> persons = personRepo.findAll();
        System.out.printf(persons.toString());
    }

}