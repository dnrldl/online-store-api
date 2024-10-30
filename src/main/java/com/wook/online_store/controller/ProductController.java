package com.wook.online_store.controller;

import com.wook.online_store.domain.Product;
import com.wook.online_store.dto.AddProductDTO;
import com.wook.online_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody AddProductDTO addProductDTO) {
        return productService.addProduct(addProductDTO);
    }

    // 카테고리가 0(설정을 안하면)이면 아무 제품
    @GetMapping
    public Page<Product> getProducts(@RequestParam(required = false, defaultValue = "0") Long categoryId,
                                     @RequestParam(required = false, defaultValue = "0") int page) {
        int size = 10;
        if (categoryId == 0)
            return productService.getProducts(page, size);
        else
            return productService.getProducts(categoryId, page, size);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }
}
