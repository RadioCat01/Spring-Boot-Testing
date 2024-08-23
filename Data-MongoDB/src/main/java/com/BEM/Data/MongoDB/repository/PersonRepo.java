package com.BEM.Data.MongoDB.repository;

import com.BEM.Data.MongoDB.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepo extends MongoRepository<Person, String> {

    List<Person> findByFirstNameStartsWith(String name);

    List<Person> findByLastName(String lastName);
}
