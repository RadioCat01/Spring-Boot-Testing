package com.BEM.Data.MongoDB.service;

import com.BEM.Data.MongoDB.collection.Person;
import com.BEM.Data.MongoDB.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo personRepo;

    public String save(Person person) {
        return personRepo.save(person).getPersonID();
    }


    public List<Person> getPersonWith(String name) {return personRepo.findByFirstNameStartsWith(name);}
}
