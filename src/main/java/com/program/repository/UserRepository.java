package com.program.repository;

import com.program.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.roles = :roleId")
    List<User> findByRoleId(Integer roleId);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailDetail(String email);



}
