package com.wook.online_store.repository;

import com.wook.online_store.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByMajorCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findBySubCategoryId(Long categoryId, Pageable pageable);
}
