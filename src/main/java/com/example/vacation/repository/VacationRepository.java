package com.example.vacation.repository;

import com.example.vacation.domain.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation,String> {
    Vacation findByUsername(String username);
    List<Vacation> findAllByUsername(String username);
}
