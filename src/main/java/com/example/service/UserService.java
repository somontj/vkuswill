package com.example.service;

import com.example.authUtils.AuthUtils;
import com.example.domain.User;
import com.example.dto.LoginDto;
import com.example.dto.UserDto;
import com.example.exception.BadCredentialsException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public UserService(UserRepository userRepository, AuthUtils authUtils) {
        this.userRepository = userRepository;
        this.authUtils = authUtils;
    }

    public User getUser(LoginDto loginDto) {
        
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        
        String encodedPassword = (user != null) ? user.getPassword() : "NOT_FOUND_DUMMY_HASH";

        boolean isPasswordMatch = authUtils.matchPassword(loginDto.getPassword(), encodedPassword);

        
        if (user == null || !isPasswordMatch) {
            throw new BadCredentialsException("Неверный email или пароль");
        }

        return user;

    }


    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)

                .toList();
    }

    public UserDto getUser(Long id) {
        return userRepository.findById(id).map(UserDto::new).orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
    }

    public void userRegister(User user) {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Пользователь с таким " + user.getEmail() + " уже существует");
        }

        String encodedPassword = authUtils.encodePassword(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }


    public void deleteUser(Long id) {
        User userDatabase = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким " + id + " не найден"));
        userRepository.delete(userDatabase);
    }

}
