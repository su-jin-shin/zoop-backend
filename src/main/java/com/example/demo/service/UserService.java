package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User create(User user){
        System.out.println(">>> user create"+user);
        return userRepository.save(user);
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }
    public void delete(Long id){
        userRepository.deleteById(id);
    }
}
