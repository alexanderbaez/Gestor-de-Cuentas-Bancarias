package com.ab.banco.persistence.repository;

import com.ab.banco.persistence.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoty extends JpaRepository<User, Long> {
}
