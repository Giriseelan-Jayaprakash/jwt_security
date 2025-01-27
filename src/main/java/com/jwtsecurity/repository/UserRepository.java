package com.jwtsecurity.repository;

import com.jwtsecurity.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
//    Users findByUsername(String username);

    Users findByEmailId(String username);

    boolean existsByEmailId(String emailId);
}
