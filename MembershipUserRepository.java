package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.MembershipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MembershipUserRepository extends JpaRepository<MembershipUser, Long> {
    Optional<MembershipUser> findByPhone(String phone);
    
    
}
