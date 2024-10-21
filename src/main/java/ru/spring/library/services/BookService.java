package ru.spring.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spring.library.models.Book;
import ru.spring.library.models.Person;
import ru.spring.library.repositories.BookRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookrepository;
    @Autowired
    public BookService(BookRepository bookrepository) {
        this.bookrepository = bookrepository;
    }

    public List<Book> findAll(){
        return bookrepository.findAll();
    }

    public List<Book> findCountOfBookOnPage(int page, int books_per_page){
        List<Book> allBook = bookrepository.findAll();
        int difference = 0;
        List<Book> selectedBooks;
        if (page*books_per_page > allBook.size() && page*books_per_page - allBook.size() < books_per_page )
            difference = page*books_per_page - allBook.size();
        else if (page*books_per_page - allBook.size() >= books_per_page || page == 0 || books_per_page ==0) {
            selectedBooks = null;
            return selectedBooks;
        }
        selectedBooks = allBook.subList(books_per_page * (page - 1),
                books_per_page * page - difference);

        return selectedBooks;
    }

    public List<Book> sortByYear(){
        List<Book> booksSort = bookrepository.findAll();
        Collections.sort(booksSort, Book.sortByYear);
        return  booksSort;
    }

    public Book findOne(int id){
        Optional<Book> foundBook = bookrepository.findById(id);
        return  foundBook.orElse(null);
    }



    @Transactional
    public void save(Book book){
        bookrepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);

        bookrepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        bookrepository.deleteById(id);
    }

    public List<Book> findByTitleContainingIgnoreCase(String startingWith){
        List<Book> foundBook =bookrepository.findByTitleContainingIgnoreCase(startingWith);

        return foundBook;
    }

    public Optional<Person> findBookOwner(int id){
        Book book = bookrepository.findById(id).orElse(null);

        Optional<Person> foundOwner = Optional.ofNullable(book.getOwner());
        return foundOwner;
    }



//    @Transactional
//    public void assign(int id, Person selectedPerson){
//        Session session = sessionFactory.getCurrentSession();
//
//        Book book = session.get(Book.class, id);
//        book.setOwner(selectedPerson);
//    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = bookrepository.findById(id).orElse(null);
        if (book != null) {
            book.setOwner(selectedPerson);
            bookrepository.save(book);
        }
    }

//    @Transactional
//    public void release(int id){
//        Session session = sessionFactory.getCurrentSession();
//
//        Book book = session.get(Book.class, id);
//        book.setOwner(null);
//    }

    @Transactional
    public void release(int id) {
        Book book = bookrepository.findById(id).orElse(null);
        if (book != null) {
            book.setOwner(null);
            bookrepository.save(book);
        }
    }

}
