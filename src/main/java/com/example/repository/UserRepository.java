package com.example.repository;

import com.example.domain.User;
import com.example.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    //    @Query("SELECT new com.example.dto.UserDto(u.name, u.surname) FROM User u WHERE u.id = :id")
    //    UserDto findUserById(@Param("id") Long id);
    UserDto findUserById(Long id);


    User findUserByPhone(String phone);


}
