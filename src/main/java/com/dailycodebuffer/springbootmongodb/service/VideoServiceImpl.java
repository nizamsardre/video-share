package com.dailycodebuffer.springbootmongodb.service;

import com.dailycodebuffer.springbootmongodb.collection.Video;
import com.dailycodebuffer.springbootmongodb.repository.VideoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository photoRepository;



    @Override
    public Video getVideo(String id) {
        return photoRepository.findById(id).get();
    }
}
