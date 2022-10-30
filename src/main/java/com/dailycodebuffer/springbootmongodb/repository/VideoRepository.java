package com.dailycodebuffer.springbootmongodb.repository;

import com.dailycodebuffer.springbootmongodb.collection.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
}
