package com.vrubayka.restapi_sample.repository;

import com.vrubayka.restapi_sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query methods can be added here if needed
}
