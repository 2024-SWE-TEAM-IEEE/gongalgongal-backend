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
            categoryRepository.save(new Category("대외활동"));
            categoryRepository.save(new Category("특강"));
            categoryRepository.save(new Category("공모전"));
            categoryRepository.save(new Category("채용"));
            categoryRepository.save(new Category("행사"));
            categoryRepository.save(new Category("학사"));
            categoryRepository.save(new Category("교환학생"));
            categoryRepository.save(new Category("연구"));
            categoryRepository.save(new Category("봉사"));
            categoryRepository.save(new Category("인턴"));
            categoryRepository.save(new Category("생활"));
            categoryRepository.save(new Category("기숙사"));
            categoryRepository.save(new Category("동아리"));
            categoryRepository.save(new Category("등록금"));
            categoryRepository.save(new Category("수업"));
            categoryRepository.save(new Category("공지"));
            categoryRepository.save(new Category("도서관"));
            categoryRepository.save(new Category("시험"));
            categoryRepository.save(new Category("기타"));
        }
    }
}
