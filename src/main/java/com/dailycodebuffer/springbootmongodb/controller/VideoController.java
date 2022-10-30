package com.dailycodebuffer.springbootmongodb.controller;

import com.dailycodebuffer.springbootmongodb.collection.Video;
import com.dailycodebuffer.springbootmongodb.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")//details
    public void get(@PathVariable String id) {
        videoService.getVideo(id);
    }



}
