package com.dailycodebuffer.springbootmongodb.controller;

import com.dailycodebuffer.springbootmongodb.collection.Person;
import com.dailycodebuffer.springbootmongodb.collection.Video;
import com.dailycodebuffer.springbootmongodb.service.VideoService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/photo")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Value("${JWT_SECRET}")
    private String secret;

    @GetMapping("/{id}")//details
    public void get(@PathVariable String id) {
        videoService.getVideo(id);
    }

    @PostMapping
    public String save(@RequestHeader(value = "Authorization") String authorization,
                       @RequestBody Video video) {
        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(authorization) //without bearer
                .getBody();
        if(body.getId() == "")
            video.setCreatedBy(new ObjectId(body.getId()));
        else
            return "wrong token";
        return videoService.save(video);
    }



}
