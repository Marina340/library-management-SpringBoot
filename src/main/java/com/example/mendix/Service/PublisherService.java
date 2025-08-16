package com.example.mendix.Service;

import com.example.mendix.Entity.Publisher;
import com.example.mendix.Repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher savePublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Page<Publisher> getAllPublishers(int page, int size) {
        return publisherRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Publisher> getPublisherById(Long id) {
        return publisherRepository.findById(id);
    }
    public Publisher updatePublisher(Long id, Publisher updatedPublisher) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        publisher.setName(updatedPublisher.getName());
        publisher.setAddress(updatedPublisher.getAddress());

        return publisherRepository.save(publisher);
    }
    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}
