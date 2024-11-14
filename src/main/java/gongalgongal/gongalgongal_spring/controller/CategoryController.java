package gongalgongal.gongalgongal_spring.controller;

import gongalgongal.gongalgongal_spring.dto.categories.CategoryResponse;
import gongalgongal.gongalgongal_spring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public CategoryResponse getCategories() {
        return categoryService.getCategories();
    }
}
