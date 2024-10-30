package com.wook.online_store.dto;

import lombok.Data;

@Data
public class AddProductDTO {
    private String title;
    private Double price;
    private Integer count;
    private String description;
    private String categoryName;
    private String imageUrl;
}
