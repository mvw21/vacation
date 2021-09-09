package com.example.vacation.repository;

import com.example.vacation.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);
    Optional<User> findFirstByUsername(String username);
}
