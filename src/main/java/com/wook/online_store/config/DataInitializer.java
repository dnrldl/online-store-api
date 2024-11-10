package com.wook.online_store.config;

import com.wook.online_store.domain.Category;
import com.wook.online_store.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner initCategory() {
        return args -> {
            if (categoryRepository.count() == 0) {
                // 상의 id=1
                Category tops = new Category();
                tops.setName("상의");
                categoryRepository.save(tops);

                subCategorySave(tops, "반소매");
                subCategorySave(tops, "긴소매");
                subCategorySave(tops, "맨투맨");
                subCategorySave(tops, "후드티");
                subCategorySave(tops, "셔츠");

                // 바지 id=7
                Category pants = new Category();
                pants.setName("하의");
                categoryRepository.save(pants);

                subCategorySave(pants, "데님");
                subCategorySave(pants, "슬랙스");
                subCategorySave(pants, "반바지");
                subCategorySave(pants, "트레이닝");

                // 아우터 id=12
                Category outers = new Category();
                outers.setName("아우터");
                categoryRepository.save(outers);

                subCategorySave(outers, "후드집업");
                subCategorySave(outers, "가디건");
                subCategorySave(outers, "후리스");
                subCategorySave(outers, "코트");
                subCategorySave(outers, "패딩");

                // 신발 id=18
                Category shoes = new Category();
                shoes.setName("신발");
                categoryRepository.save(shoes);

                subCategorySave(shoes, "스니커즈");
                subCategorySave(shoes, "구두");
                subCategorySave(shoes, "운동화");
                subCategorySave(shoes, "슬리퍼");
            }
        };
    }

    private void subCategorySave(Category parent, String subName) {
        Category sub = new Category();
        sub.setName(subName);
        sub.setParent(parent);
        categoryRepository.save(sub);
    }
}