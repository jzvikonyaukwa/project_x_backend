package com.axe.consumable_category;

import com.axe.consumable_category.dtos.CategoryDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumable-categories")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsumableCategoryController {

    private final ConsumableCategoryService consumableCategoryService;

    public ConsumableCategoryController(ConsumableCategoryService consumableCategoryService) {
        this.consumableCategoryService = consumableCategoryService;
    }

    @GetMapping("")
    public List<ConsumableCategory> getAllConsumableCategories(){
        return consumableCategoryService.getAllConsumableCategories();
    }

    @PostMapping("")
    public ConsumableCategory addConsumableCategory(@RequestBody CategoryDTO consumableCategory){
        return consumableCategoryService.addConsumableCategory(consumableCategory);
    }

    @PatchMapping("{id}")
    public ConsumableCategory updateConsumableCategory(@PathVariable Long id, @RequestBody CategoryDTO consumableCategory){
        return consumableCategoryService.updateConsumableCategory(id, consumableCategory);
    }
}
