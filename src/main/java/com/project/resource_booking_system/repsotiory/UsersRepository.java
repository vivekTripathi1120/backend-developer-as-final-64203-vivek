package com.project.resource_booking_system.repsotiory;

import com.project.resource_booking_system.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

  @Query("SELECT u FROM Users u WHERE u.email = :username OR u.username = :username And u.deletedFlag = false")
  Optional<Users> findByUsername(String username);

  @Query("SELECT u FROM Users u WHERE u.email = :email AND u.deletedFlag = false")
  Users existsByEmail(String email);

}