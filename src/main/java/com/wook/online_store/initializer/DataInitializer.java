package com.wook.online_store.initializer;

import com.wook.online_store.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    @Autowired
    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        if (categoryRepository.count() == 0) {
//            Category electronics = new Category();
//            electronics.setName("Electronics");
//            electronics.setDescription("Devices and gadgets");
//            Category clothing = new Category();
//            clothing.setName("Clothing");
//            clothing.setDescription("Apparel and accessories");
//            categoryRepository.saveAll(List.of(electronics, clothing));
//        }
    }
}
