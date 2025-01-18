package com.axe.consumable_category;

import com.axe.consumable_category.dtos.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumableCategoryService {
    private final ConsumableCategoryRepository consumableCategoryRepository;

    public ConsumableCategoryService(ConsumableCategoryRepository consumableCategoryRepository) {
        this.consumableCategoryRepository = consumableCategoryRepository;
    }

    public List<ConsumableCategory> getAllConsumableCategories() {
        return consumableCategoryRepository.findAll();
    }

    public ConsumableCategory getConsumableCategoryByName(String category) {
        return consumableCategoryRepository.findByName(category);
    }

    public ConsumableCategory addConsumableCategory(CategoryDTO consumableCategory) {
        ConsumableCategory category = new ConsumableCategory();
        category.setName(consumableCategory.getName());
        return consumableCategoryRepository.save(category);
    }


    public ConsumableCategory updateConsumableCategory(Long id, CategoryDTO consumableCategory) {
        ConsumableCategory category = consumableCategoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setName(consumableCategory.getName());
            return consumableCategoryRepository.save(category);
        }else {
            return null;
        }
    }
}
