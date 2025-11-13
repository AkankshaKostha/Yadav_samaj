package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.HappyStory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HappyStoryRepository extends JpaRepository<HappyStory, Long> {
   
}
