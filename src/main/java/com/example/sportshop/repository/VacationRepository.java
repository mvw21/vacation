package com.example.sportshop.repository;

import com.example.sportshop.domain.entity.User;
import com.example.sportshop.domain.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacationRepository extends JpaRepository<Vacation,String> {
    Vacation findByUsername(String username);
}
