package com.dailycodebuffer.springbootmongodb.service;

import com.dailycodebuffer.springbootmongodb.collection.Person;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    String save(Person person);
    String login(Person person);

    List<Person> getPersonStartWith(String name);

    void delete(String id);



    Page<Person> search(String name, String email, Pageable pageable);


}
