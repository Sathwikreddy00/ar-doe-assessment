package com.alpha.recon.doe.assessment.controller;

import com.alpha.recon.doe.assessment.domain.User;
import com.alpha.recon.doe.assessment.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return this.userService.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return this.userService.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User dto) {
        return this.userService.create(dto);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User dto) {
        return this.userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.userService.delete(id);
    }
}
