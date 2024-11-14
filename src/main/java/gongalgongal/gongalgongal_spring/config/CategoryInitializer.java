package gongalgongal.gongalgongal_spring.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;

@Component
public class CategoryInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        // 중복 방지를 위해 기존 카테고리 존재 여부 확인
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("장학"));
            categoryRepository.save(new Category("대외활동")); // 필요 시 추가
        }
    }
}

