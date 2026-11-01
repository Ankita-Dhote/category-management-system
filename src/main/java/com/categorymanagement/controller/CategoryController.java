package com.categorymanagement.controller;

import com.categorymanagement.entity.Category;
import com.categorymanagement.repository.CategoryRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Dashboard
    @GetMapping("/")
    public String dashboard(Model model) {

        var categories = categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("totalCategories", categories.size());

        model.addAttribute("activeCategories",
                categories.stream()
                .filter(c -> "Active".equalsIgnoreCase(c.getStatus()))
                .count());

        model.addAttribute("inactiveCategories",
                categories.stream()
                .filter(c -> "Inactive".equalsIgnoreCase(c.getStatus()))
                .count());

        return "index";
    }

    // Open Add Category Form
    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {

        Category category = new Category();
        category.setStatus("Active");

        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Add Category");
        model.addAttribute("formAction", "/categories/save");

        return "category-form";
    }

    // Save Category
    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category) {

        categoryRepository.save(category);

        return "redirect:/";
    }

    // Open Edit Form
    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid category ID: " + id));

        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Edit Category");
        model.addAttribute("formAction", "/categories/update/" + id);

        return "category-form";
    }

    // Update Category
    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category category) {

        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid category ID: " + id));

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        existing.setStatus(category.getStatus());

        categoryRepository.save(existing);

        return "redirect:/";
    }

    // Delete Category
    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {

        categoryRepository.deleteById(id);

        return "redirect:/";
    }
}