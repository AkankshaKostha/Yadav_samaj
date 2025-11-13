package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.MatrimonialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MatrimonialUserRepository extends JpaRepository<MatrimonialUser, Long> {

	 
	    List<MatrimonialUser> findByApprovedFalse(); // Pending users
	    Optional<MatrimonialUser> findByPhone(String phone);
	    boolean existsByPhone(String phone);
	    boolean existsByEmail(String email);
	    List<MatrimonialUser> findByApprovedTrueAndPaymentDoneFalse(); 
    List<MatrimonialUser> findByApprovedTrue();
    List<MatrimonialUser> findByStatus(String status);
    long countByStatus(String status);
    List<MatrimonialUser> findByApprovedTrueAndPaidFalse();
    
   
    
}
