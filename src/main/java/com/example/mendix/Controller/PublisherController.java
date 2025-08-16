package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Entity.Publisher;
import com.example.mendix.Service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "CREATE", entity = "Publisher")
    public Publisher createPublisher(@RequestBody Publisher publisher) {
        return publisherService.savePublisher(publisher);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET", entity = "Publisher")
    public Page<Publisher> getPublishers(
            @RequestParam(value ="page" ,defaultValue = "0") int page,
            @RequestParam(value ="size" ,defaultValue = "10") int size
    ) {
        return publisherService.getAllPublishers(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET BY PUBLISHER ID", entity = "Publisher")
    public Publisher getPublisherById(@PathVariable Long id) {
        return publisherService.getPublisherById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "UPDATE", entity = "Publisher")
    public Publisher updatePublisher(@PathVariable Long id, @RequestBody Publisher publisher) {
        return publisherService.updatePublisher(id, publisher);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "Publisher")
    public void deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
    }
}
