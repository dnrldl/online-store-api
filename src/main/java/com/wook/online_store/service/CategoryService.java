package com.wook.online_store.service;

import com.wook.online_store.entity.Category;
import com.wook.online_store.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 카테고리 목록 가져오기
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 카테고리 추가하기
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 카테고리 수정하기
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }

    // 카테고리 삭제하기
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
