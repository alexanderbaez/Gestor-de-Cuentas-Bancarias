package com.ab.banco.repository;

import com.ab.banco.models.Account;
import com.ab.banco.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepositoty extends JpaRepository<User, Long> {
}
