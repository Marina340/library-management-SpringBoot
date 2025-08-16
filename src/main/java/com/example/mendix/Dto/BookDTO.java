package com.example.mendix.Dto;

import java.util.Set;

public class BookDTO {
    public String title;
    public String isbn;
    public int edition;
    public int publicationYear;
    public String language;
    public String summary;
    public String coverImage;
    public Long publisherId;
    public Set<Long> authorIds;
    public Set<Long> categoryIds;

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getEdition() {
        return edition;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getLanguage() {
        return language;
    }

    public String getSummary() {
        return summary;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public Set<Long> getAuthorIds() {
        return authorIds;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }
}
