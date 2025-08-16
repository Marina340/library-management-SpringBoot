package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Dto.BookDTO;
import com.example.mendix.Dto.BookResponseDTO;
import com.example.mendix.Service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "CREATE", entity = "Book")
    public BookResponseDTO createBook(
            @RequestPart("book") BookDTO dto,
            @RequestPart("coverImage") MultipartFile file) {

        return bookService.mapToDTO(bookService.saveBook(dto, file));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET", entity = "Book")
    public Page<BookResponseDTO> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return bookService.getAllBooks(pageable);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET BY BOOK ID", entity = "Book")
    public BookResponseDTO getBook(@PathVariable Long id) {
        return bookService.getBook(id)
                .map(bookService::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "Book")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public BookResponseDTO updateBook(
            @PathVariable Long id,
            @RequestPart("book") BookDTO dto,
            @RequestPart(value = "coverImage", required = false) MultipartFile file) {

        return bookService.mapToDTO(bookService.updateBook(id, dto, file));
    }

}
