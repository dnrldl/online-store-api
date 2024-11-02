package com.wook.online_store.config;

import com.wook.online_store.domain.Category;
import com.wook.online_store.domain.Role;
import com.wook.online_store.domain.User;
import com.wook.online_store.repository.CategoryRepository;
import com.wook.online_store.repository.RoleRepository;
import com.wook.online_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initRole() {
        return args -> {
            if (roleRepository.count() != 2) {
                Role userRole = new Role();
                userRole.setRoleId(1L);
                userRole.setName("ROLE_USER");

                Role adminRole = new Role();
                adminRole.setRoleId(2L);
                adminRole.setName("ROLE_ADMIN");

                roleRepository.save(userRole);
                roleRepository.save(adminRole);
            }
        };
    }

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