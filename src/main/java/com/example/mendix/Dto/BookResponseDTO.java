package com.example.mendix.Dto;

import java.util.List;

public class BookResponseDTO {
    public Long id;
    public String title;
    public String isbn;
    public int edition;
    public int publicationYear;
    public String language;
    public String summary;
    public String coverImage;
    public PublisherDTO publisher;
    public List<AuthorDTO> authors;
    public List<CategoryDTO> categories;

    public static class PublisherDTO {
        public Long id;
        public String name;
    }

    public static class AuthorDTO {
        public Long id;
        public String name;
    }

    public static class CategoryDTO {
        public Long id;
        public String name;
    }
}
