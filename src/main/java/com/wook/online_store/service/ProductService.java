package com.wook.online_store.service;

import com.wook.online_store.domain.Category;
import com.wook.online_store.domain.Product;
import com.wook.online_store.dto.productDTO;
import com.wook.online_store.exception.InvalidDataException;
import com.wook.online_store.repository.CategoryRepository;
import com.wook.online_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Product addProduct(productDTO productDTO) {
        Category majorCategory = categoryRepository.findByNameAndParentIsNull(productDTO.getMajorCategoryName())
                .orElseThrow(() -> new InvalidDataException("존재하지 않는 대분류 카테고리입니다: " + productDTO.getMajorCategoryName()));

        Category subCategory = categoryRepository.findByName(productDTO.getSubCategoryName())
                .orElseThrow(() -> new InvalidDataException("존재하지 않는 중분류 카테고리입니다: " + productDTO.getSubCategoryName()));

        if (!subCategory.getParent().equals(majorCategory)) {
            throw new InvalidDataException("중분류 카테고리가 대분류에 속하지 않습니다.");
        }

        Product product = new Product();
        product.setMajorCategory(majorCategory);
        product.setSubCategory(subCategory);
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setCount(productDTO.getCount());
        product.setDescription(productDTO.getDescription());
        product.setImageUrl(productDTO.getImageUrl());
        //rating 추가

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getMajorProducts(Long categoryId, int page, int size) {
        return productRepository.findByMajorCategoryId(categoryId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<Product> getSubProducts(Long categoryId, int page, int size) {
        return productRepository.findBySubCategoryId(categoryId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
