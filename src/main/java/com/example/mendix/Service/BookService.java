package com.example.mendix.Service;

import com.example.mendix.Dto.BookDTO;
import com.example.mendix.Dto.BookResponseDTO;
import com.example.mendix.Entity.Author;
import com.example.mendix.Entity.Book;
import com.example.mendix.Entity.Category;
import com.example.mendix.Entity.Publisher;
import com.example.mendix.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final BorrowTransactionRepository borrowRepository;
    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       PublisherRepository publisherRepository,
                       BorrowTransactionRepository borrowRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.borrowRepository=borrowRepository;

    }

    @Transactional
    public Book saveBook(BookDTO dto, MultipartFile file) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPublicationYear(dto.getPublicationYear());
        book.setLanguage(dto.getLanguage());
        book.setSummary(dto.getSummary());
        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            book.setCoverImage(base64Image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image", e);
        }
        // Publisher
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found: " + dto.getPublisherId()));
        book.setPublisher(publisher);

        // Authors
        Set<Author> authors = new HashSet<>();
        if (dto.getAuthorIds() != null) {
            for (Long id : dto.getAuthorIds()) {
                Author author = authorRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Author not found: " + id));
                authors.add(author);
            }
        }
        book.setAuthors(authors);

        // Categories
        Set<Category> categories = new HashSet<>();
        if (dto.getCategoryIds() != null) {
            for (Long id : dto.getCategoryIds()) {
                Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id));
                categories.add(category);
            }
        }
        book.setCategories(categories);

        // Save everything at once
        return bookRepository.save(book);
    }

    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void deleteBook(Long id) {
        boolean isBorrowed = borrowRepository.existsByBookIdAndStatus(id, "BORROWED");
        if (isBorrowed) {
            throw new RuntimeException("Cannot delete book. It is currently borrowed.");
        }
        bookRepository.deleteById(id);
    }

    // ===== Map Book to Response DTO =====
// ===== Map Book to Response DTO =====
    public BookResponseDTO mapToDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.id = book.getId();
        dto.title = book.getTitle();
        dto.isbn = book.getIsbn();
        dto.edition = book.getEdition();
        dto.publicationYear = book.getPublicationYear();
        dto.language = book.getLanguage();
        dto.summary = book.getSummary();
        dto.coverImage = book.getCoverImage();
        // Publisher (still showing id + name)
        if (book.getPublisher() != null) {
            BookResponseDTO.PublisherDTO pub = new BookResponseDTO.PublisherDTO();
            pub.id = book.getPublisher().getId();
            pub.name = book.getPublisher().getName();
            dto.publisher = pub;
        }

        // Authors: only ID
        dto.authors = book.getAuthors().stream().map(a -> {
            BookResponseDTO.AuthorDTO adto = new BookResponseDTO.AuthorDTO();
            adto.id = a.getId();
            adto.name=a.getName();
            return adto;
        }).toList();

        // Categories: only ID
        dto.categories = book.getCategories().stream().map(c -> {
            BookResponseDTO.CategoryDTO cdto = new BookResponseDTO.CategoryDTO();
            cdto.id =c.getId();
            cdto.name=c.getName();
            return cdto;
        }).toList();

        return dto;
    }
    @Transactional
    public Book updateBook(Long id, BookDTO dto, MultipartFile file) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));

        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setEdition(dto.getEdition());
        book.setPublicationYear(dto.getPublicationYear());
        book.setLanguage(dto.getLanguage());
        book.setSummary(dto.getSummary());

        // Update cover image if provided
        if (file != null && !file.isEmpty()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
                book.setCoverImage(base64Image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image", e);
            }
        }

        // Update Publisher
        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found: " + dto.getPublisherId()));
            book.setPublisher(publisher);
        }

        // Update Authors
        if (dto.getAuthorIds() != null) {
            Set<Author> authors = new HashSet<>();
            for (Long authorId : dto.getAuthorIds()) {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));
                authors.add(author);
            }
            book.setAuthors(authors);
        }

        // Update Categories
        if (dto.getCategoryIds() != null) {
            Set<Category> categories = new HashSet<>();
            for (Long categoryId : dto.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));
                categories.add(category);
            }
            book.setCategories(categories);
        }

        return bookRepository.save(book);
    }


}
