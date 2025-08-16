package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Entity.Author;
import com.example.mendix.Service.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "CREATE", entity = "Author")
    public Author createAuthor(@RequestBody Author author) {
        return authorService.saveAuthor(author);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET", entity = "Author")
    public Page<Author> getAuthors(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return authorService.getAllAuthors(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET BY AUTHOR ID", entity = "Author")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthor(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "UPDATE", entity = "Author")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        return authorService.updateAuthor(id, updatedAuthor)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "Author")
    public void deleteAuthor(@PathVariable Long id) {
        if (authorService.getAuthor(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        authorService.deleteAuthor(id);
    }

}
