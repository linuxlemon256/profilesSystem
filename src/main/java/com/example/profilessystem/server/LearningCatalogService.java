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
        var completedCodes = java.util.Set.of("080701", "080202", "080205", "080717T", "080717");
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
