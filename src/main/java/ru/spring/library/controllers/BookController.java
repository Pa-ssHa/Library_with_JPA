package ru.spring.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Lists;
import ru.spring.library.models.Book;
import ru.spring.library.models.Person;
import ru.spring.library.services.BookService;
import ru.spring.library.services.PersonService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {

//    private final BookDAO bookDAO;
//
//    private final PersonDAO personDAO;

    private final PersonService personService;
    private final BookService bookService;

    @Autowired
    public BookController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }


    @GetMapping("/all")
    public String index( Model model, @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year){
        if(sort_by_year)
            model.addAttribute("book", bookService.sortByYear());
        else
            model.addAttribute("book", bookService.findAll());

        return "book/index";
    }

    @GetMapping()
    public String indexNew(@RequestParam("page") int page, @RequestParam(value = "sort_by_year", required = false) boolean sort_by_year,
                               @RequestParam("books_per_page") int books_per_page, Model model){
        List<Book> books;
        if(sort_by_year) {
            books = bookService.findCountOfBookOnPage(page, books_per_page);
            Collections.sort(books, Book.sortByYear);
            model.addAttribute("book", books);
        }
        else
            model.addAttribute("book", bookService
                    .findCountOfBookOnPage(page, books_per_page));

        return "book/index";
    }

    @GetMapping("/search")
    public String searchBook(Model model){

        model.addAttribute("book", new Book());
        return "book/search";
    }

//    @PostMapping("/search")
//    public String searchBook(Book book, Model model, String title){
//
//        Optional<Book> foundBooks = bookService.findByTitleStartingWith(title);
//        if(foundBooks.isPresent())
//            model.addAttribute("foundBooks", foundBooks);
//        else
//            model.addAttribute("foundBooksNull", foundBooks);
//
//        return "book/search";
//    }

    @PostMapping("/search")
    public String searchBook(@RequestParam("title") String title, Model model) {
        List<Book> foundBooks = bookService.findByTitleContainingIgnoreCase(title);
        model.addAttribute("title", title);


        if (!foundBooks.isEmpty()) {
            model.addAttribute("foundBooks", foundBooks);
        } else {
            model.addAttribute("foundBooksNull", true);
        }

        return "book/search";
    }


    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "book/new";
        bookService.save(book);
        return "redirect:/book/all";
    }
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person){

        model.addAttribute("book", bookService.findOne(id));

        Optional<Person> bookOwner = bookService.findBookOwner(id);
        if(bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", personService.findAll());

        return "book/show";
    }


    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "book/new";
    }





    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookService.findOne(id));
        return "book/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, BindingResult bindingResult,
                         @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "book/edit";
        bookService.update(id, book);
        return "redirect:/book";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/book/all";
    }


    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        Book book = bookService.findOne(id);
        book.setStartTime(new Date());
        bookService.save(book);
        bookService.assign(id, selectedPerson);

        return "redirect:/book/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        Book book = bookService.findOne(id);
        book.setStartTime(null);
        bookService.save(book);
        bookService.release(id);

        return "redirect:/book/" + id;
    }









}
