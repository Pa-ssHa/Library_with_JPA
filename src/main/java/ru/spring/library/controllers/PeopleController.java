package ru.spring.library.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spring.library.config.SpringConfig;
import ru.spring.library.models.Book;
import ru.spring.library.models.Person;
import ru.spring.library.repositories.PeopleRepository;
import ru.spring.library.services.PersonService;
import ru.spring.library.util.PersonValidator;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    private final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    public PeopleController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }


    @GetMapping()
    public String index(Model model) {
        logger.info("Выводим список людей");
        try {
            model.addAttribute("people", personService.findAll());
        }catch (Exception e){
            logger.error("Ошибка при выводе списка людей");
        }
        logger.info("Вывод списка людей закончен");
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
//        Person person = personService.findOne(id);
//        List<Book> books = personService.findBookByPerson(id);
//
//        System.out.println("Person: " + person);
//        System.out.println("Books: " + books);
//
//        model.addAttribute("person", person);
//        model.addAttribute("books", books);

        logger.info("Вывод информации по одному человеку с id: " + id);

        try {
            model.addAttribute("person", personService.findOne(id));
            model.addAttribute("books", personService.findBookByPerson(id));
        }catch (Exception e){
            logger.error("Ошибка при доступе к информации человека с id: " + id);
        }

        logger.info("Вывод информации по одному человеку с id: " + id + " закончен");

        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

   @PostMapping
    public String create(@ModelAttribute("person") Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.findOne(id));
        return "people/edit";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") Person person,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        personService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }

}
