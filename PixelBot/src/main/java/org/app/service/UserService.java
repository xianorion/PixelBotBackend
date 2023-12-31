package org.app.service;

import org.app.Exceptions.UserNotFoundException;
import org.app.repository.UserRepository;
import org.app.vo.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionFactory sessionFactory;

    public Iterable<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public User save(User userObj) {
        return userRepository.save(userObj);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersAboveAge(int age){
        List<User> userList = userRepository.findUsersOfAge(age);
        if(userList.isEmpty())
            throw new UserNotFoundException("No users above " + age + " found");
        return userRepository.findUsersOfAge(age);
    }
}
