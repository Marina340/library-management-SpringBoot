package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Dto.UserDTO;
import com.example.mendix.Dto.UserResponseDTO;
import com.example.mendix.Entity.User;
import com.example.mendix.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "CREATE", entity = "User")
    public User createUser(@RequestBody UserDTO dto) {
        return userService.createUser(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "UPDATE", entity = "User")
    public UserResponseDTO updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        User updatedUser = userService.updateUser(id, dto);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "User")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "GET BY USER ID", entity = "User")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "GET", entity = "User")
    public Page<UserResponseDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userService.getAllUsers(pageable); // now returns Page<UserResponseDTO>
    }

}
