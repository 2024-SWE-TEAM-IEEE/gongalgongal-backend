package gongalgongal.gongalgongal_spring.service;

import gongalgongal.gongalgongal_spring.dto.categories.CategoryResponse;
import gongalgongal.gongalgongal_spring.model.Category;
import gongalgongal.gongalgongal_spring.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryResponse getCategories() {
        List<CategoryResponse.CategoryInfo> categories = categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse.CategoryInfo(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());

        CategoryResponse.Status status = new CategoryResponse.Status("success", "Categories retrieved successfully");
        return new CategoryResponse(status, new CategoryResponse.Data(categories));
    }
}
