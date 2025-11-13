package com.example.yadavsajam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.yadavsajam.model.PremiumPlan;

@Repository
public interface PremiumPlanRepository extends JpaRepository<PremiumPlan, Long> {}
