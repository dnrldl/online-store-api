package com.wook.online_store.controller;

import com.wook.online_store.domain.Category;
import com.wook.online_store.dto.AddCategoryDTO;
import com.wook.online_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody AddCategoryDTO addCategoryDTO) {
        return categoryService.addCategory(addCategoryDTO);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }
}
