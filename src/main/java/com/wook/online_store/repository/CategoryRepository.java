package com.wook.online_store.repository;

import com.wook.online_store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndParentIsNull(String majorCategory); // 대분류 카테고리 조회
    Optional<Category> findByName(String categoryName);
}
