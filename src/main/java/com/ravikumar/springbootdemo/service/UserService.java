package com.ravikumar.springbootdemo.service;


import com.ravikumar.springbootdemo.domain.Role;
import com.ravikumar.springbootdemo.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUserById(Long id);
    List<User> getUsers();
}
