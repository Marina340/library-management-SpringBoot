package com.example.mendix.Controller;

import com.example.mendix.CustomAnnotation.LoggableAction;
import com.example.mendix.Entity.Category;
import com.example.mendix.Service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "CREATE", entity = "Category")
    public Category createCategory(@RequestBody Category category) {
        // Optional: If parent is provided, fetch it from DB
        if (category.getParent() != null && category.getParent().getId() != null) {
            Category parent = categoryService.getCategoryById(category.getParent().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent category not found"));
            category.setParent(parent);
        }
        return categoryService.saveCategory(category);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET", entity = "Category")
    public Page<Category> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.getAllCategories(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','STAFF')")
    @LoggableAction(action = "GET BY CATEGORY ID", entity = "Category")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    @LoggableAction(action = "UPDATE", entity = "Category")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableAction(action = "DELETE", entity = "Category")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
