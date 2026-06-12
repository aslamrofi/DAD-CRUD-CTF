package com.crudctf.repository;

import com.crudctf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring magically writes the SQL for this method just based on the name!
    User findByUsername(String username);
}