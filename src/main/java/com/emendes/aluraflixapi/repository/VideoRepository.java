package com.emendes.aluraflixapi.repository;

import com.emendes.aluraflixapi.model.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

}