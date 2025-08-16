package com.example.mendix.Service;

import com.example.mendix.Entity.Author;
import com.example.mendix.Repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Page<Author> getAllAuthors(int page, int size) {
        return authorRepository.findAll(PageRequest.of(page, size));
    }
    public Optional<Author> updateAuthor(Long id, Author updatedAuthor) {
        return authorRepository.findById(id).map(author -> {
            author.setName(updatedAuthor.getName());
            author.setBiography(updatedAuthor.getBiography());
            return authorRepository.save(author);
        });
    }
    public Optional<Author> getAuthor(Long id) {
        return authorRepository.findById(id);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
