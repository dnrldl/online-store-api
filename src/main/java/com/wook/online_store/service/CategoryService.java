package com.wook.online_store.service;

import com.wook.online_store.domain.Category;
import com.wook.online_store.dto.AddCategoryDTO;
import com.wook.online_store.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 카테고리 추가하기
    @Transactional
    public Category addCategory(AddCategoryDTO addCategoryDTO) {
        Category category = new Category();
        category.setName(addCategoryDTO.getName());

        return categoryRepository.save(category);
    }


    // 카테고리 수정하기
    public Category updateCategory(Long id, Category categoryDetails) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
        category.setName(categoryDetails.getName());
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
