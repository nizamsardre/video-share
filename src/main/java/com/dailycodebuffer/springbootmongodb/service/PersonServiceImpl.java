package com.dailycodebuffer.springbootmongodb.service;

import com.dailycodebuffer.springbootmongodb.collection.Person;
import com.dailycodebuffer.springbootmongodb.repository.PersonRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Person person) {
        return personRepository.save(person).getId();
    }

    @Override
    public List<Person> getPersonStartWith(String name) {
        return personRepository.findByFirstNameStartsWith(name);
    }

    @Override
    public void delete(String id) {
        personRepository.deleteById(id);
    }


    @Override
    public Page<Person> search(String name, String email, Pageable pageable) {

        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if(name !=null && !name.isEmpty()) {
            criteria.add(Criteria.where("firstName").regex(name,"i"));
        }

        if(email !=null && !email.isEmpty()) {
            criteria.add(Criteria.where("email").is(email));
        }

        if(!criteria.isEmpty()) {
            query.addCriteria(new Criteria()
                    .andOperator(criteria.toArray(new Criteria[0])));
        }

        Page<Person> people = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Person.class
                ), pageable, () -> mongoTemplate.count(query.skip(0).limit(0),Person.class));
        return people;
    }

}
