package com.wook.online_store.dto;

import lombok.Data;

@Data
public class productDTO {
    private String title;
    private Double price;
    private Integer count;
    private String description;
    private String majorCategoryName;
    private String subCategoryName;
    private String imageUrl;
}
