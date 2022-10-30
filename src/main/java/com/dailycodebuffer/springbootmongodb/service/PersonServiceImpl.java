package com.dailycodebuffer.springbootmongodb.service;

import com.dailycodebuffer.springbootmongodb.collection.Person;
import com.dailycodebuffer.springbootmongodb.repository.PersonRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{

    @Autowired
    private PersonRepository personRepository;
    @Value("${JWT_SECRET}")
    private String secret;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public String save(Person person) {
        String salt = BCrypt.gensalt(10);
        String hashPassword = BCrypt.hashpw(person.getPass(), salt);
        person.setPass(hashPassword);
        return personRepository.save(person).getId();
    }

    @Override
    public String login(Person person) {
        Person person1 = personRepository.findById(person.getId()).get();
        if(BCrypt.checkpw(person.getPass(), person1.getPass())) {
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.MONTH , 2);
            JwtBuilder token = Jwts.builder()
                    .setAudience(person.getId())
                    .setId(person.getId())
                    .setExpiration(calendar.getTime())
                    .claim("neme", person1.getFirstName())
                    .claim("email", person1.getEmail())
                    .signWith(SignatureAlgorithm.HS256, secret);
            return token.compact();
        }

        return "no token";
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
