package com.wook.online_store.service;

import com.wook.online_store.domain.Category;
import com.wook.online_store.domain.Product;
import com.wook.online_store.dto.AddProductDTO;
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
    private final CategoryService categoryService;

    @Transactional
    public Product addProduct(AddProductDTO addProductDTO) {
        Category category = categoryService.getCategory(addProductDTO.getCategoryName());  // id로 찾다가 이름으로 변경
        Product product = new Product();
        product.setCategory(category);
        product.setTitle(addProductDTO.getTitle());
        product.setPrice(addProductDTO.getPrice());
        product.setCount(addProductDTO.getCount());
        product.setDescription(addProductDTO.getDescription());
        product.setImageUrl(addProductDTO.getImageUrl());
        //rating 추가

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(Long categoryId, int page, int size) {
        return productRepository.findProductByCategory_Id(categoryId, PageRequest.of(page, size));
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
