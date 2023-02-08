package com.ravikumar.springbootdemo.controller;


import com.ravikumar.springbootdemo.domain.Role;
import com.ravikumar.springbootdemo.domain.User;
import com.ravikumar.springbootdemo.service.UserServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/manage/users")
    public ResponseEntity<List<User>>getManagableUsers() {
        return ResponseEntity.ok().body(
                userService.getUsers().stream()
                        .filter(user -> !user.getRoles().contains(new Role("ROLE_ADMIN")))
                        .toList()
        );
    }

    @GetMapping("/user/{userId}")
    public User getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok().body(userService.getUserByUsername(email));
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveUser(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/revokeRole")
    public ResponseEntity<?> revokeRoleToUser(@RequestBody RoleToUserForm form) {
        userService.revokeRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.isUserRegistered(user.getUsername()))
        {
            return ResponseEntity.badRequest().build();
        }

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/register").toUriString());
        User registeredUser = userService.saveUser(user);
        userService.addRoleToUser(registeredUser.getUsername(), "ROLE_USER");
        return ResponseEntity.created(uri).body(registeredUser);
    }

    private String getUserId(User user) {
        return userService.getUserByUsername(user.getUsername()).getId().toString();
    }

    private List<String> getUserAuthorityList(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Data
    private static class RoleToUserForm {
        private String username;
        private String roleName;
    }
}
