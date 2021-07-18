package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.CategoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {
    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    @GetMapping("add")
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @PostMapping("add")
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, Model model, @RequestParam int categoryId) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        Optional<Category> optionalCategory = categoryDao.findById(categoryId);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            newCheese.setCategory(category);
            cheeseDao.save(newCheese);
            return "redirect:..";
        }

        model.addAttribute("title", "Add Cheese");
        return "redirect:..";
    }

    @GetMapping("remove")
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @PostMapping("remove")
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {
        for (int cheeseId : cheeseIds) {
            cheeseDao.deleteById(cheeseId);
        }

        return "redirect:..";
    }
}
