package com.wook.online_store.dto;

import lombok.Data;

@Data
public class AddProductDTO {
    private String title;
    private Double price;
    private String description;
    private Long categoryId;
    private String imageUrl;
}
