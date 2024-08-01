package com.BEM.Data.MongoDB.controller;

import com.BEM.Data.MongoDB.collection.Person;
import com.BEM.Data.MongoDB.repository.PersonRepo;
import com.BEM.Data.MongoDB.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonRepo personRepo;

    @PostMapping
    public String save(@RequestBody Person person) {
        return personService.save(person);
    }

    @GetMapping
    public List<Person> getAllStartWith(@RequestParam("name") String name) {
        return personService.getPersonWith(name);
    }

    @GetMapping("/all")
    public List<Person> getAll(){
        return personRepo.findAll();
    }
}
