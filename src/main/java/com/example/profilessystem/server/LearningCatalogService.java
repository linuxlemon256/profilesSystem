package com.example.profilessystem.server;

import com.example.profilessystem.entity.LearningCategoryPage;
import com.example.profilessystem.entity.MajorCategory;
import com.example.profilessystem.mapper.LearningCatalogMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningCatalogService {
    private final LearningCatalogMapper mapper;
    private final LearningAccessService accessService;

    public LearningCatalogService(LearningCatalogMapper mapper, LearningAccessService accessService) {
        this.mapper = mapper;
        this.accessService = accessService;
    }

    public List<MajorCategory> categories() {
        List<MajorCategory> categories = mapper.findCategories();
        // Match the actual knowledge pages registered in category-detail.html.
        var completedCodes = java.util.Set.of(
                "080701", "080202", "080205", "080717T", "080717", "080917T",
                "080917", "080901", "080914TK", "080914", "080918TK", "080918",
                "080911TK", "080911", "080910T", "080910", "080906", "080909T",
                "080909", "080913T", "080913", "080919T", "080919", "080904K",
                "080904", "080907T", "080907", "080905", "080903", "080912T",
                "080912", "080915T", "080915", "080902", "080908T", "080908",
                "080916T", "080916");
        var total = new java.util.HashMap<Long, Integer>();
        var completed = new java.util.HashMap<Long, Integer>();
        for (var major : mapper.findMajorCoverage()) {
            total.merge(major.categoryId(), 1, Integer::sum);
            if (completedCodes.contains(major.code())) completed.merge(major.categoryId(), 1, Integer::sum);
        }
        for (var category : categories) {
            category.setTotalMajorCount(total.getOrDefault(category.getId(), 0));
            category.setCompletedMajorCount(completed.getOrDefault(category.getId(), 0));
        }
        return categories;
    }

    public LearningCategoryPage category(String slug, String username) {
        MajorCategory category = mapper.findCategoryBySlug(slug);
        if (category == null) return null;
        return new LearningCategoryPage(
                category,
                mapper.findMajors(category.getId()),
                mapper.findRoutes(category.getId(), accessService.level(username)));
    }
}
