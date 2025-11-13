package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    // Additional query methods can be added here if needed
}
