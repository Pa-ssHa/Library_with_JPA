package ru.spring.library.services;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.spring.library.models.Book;
import ru.spring.library.models.Person;
import ru.spring.library.repositories.PeopleRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class PersonService {


    private final PeopleRepository peopleRepository;


    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){

        Optional<Person> foundPerson =peopleRepository.findById(id);

        return foundPerson.orElse(null);
    }


    /*public List<Book> checkBook(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            List<Book> books = person.get().getBooks();
            List<Book> overdueBooks = new ArrayList<>();

            LocalDateTime now = LocalDateTime.now();

            for (Book book : books) {
                if (book.getStartTime() != null) {
                    LocalDateTime startTime = book.getStartTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();


                    long daysBetween = Duration.between(startTime, now).toDays();

                    if (daysBetween >= 10) {
                        overdueBooks.add(book);
                    }
                }
            }
            return overdueBooks; // Возвращаем список просроченных книг
        }
        return new ArrayList<>();
    }*/

    public List<Book> checkBook(int id) {
        // Находим человека по id
        Optional<Person> person = peopleRepository.findById(id);

        // Если человек не найден, возвращаем пустой список
        if (!person.isPresent()) {
            return List.of(); // Возвращаем пустой список
        }

        // Получаем список книг, которые у человека
        List<Book> books = person.get().getBooks();

        // Инициализируем список для просроченных книг
        List<Book> overdueBooks = new ArrayList<>();

        // Проверяем каждую книгу
        for (Book book : books) {
//            System.out.println(book.toString());
            if (book.getStartTime() != null) {
                long daysBetween = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - book.getStartTime().getTime());
//                System.out.println(book.getStartTime().getTime());
//                System.out.println(System.currentTimeMillis());
//                System.out.println(daysBetween);
                if (daysBetween >= 10) {
                    overdueBooks.add(book); // Добавляем книгу в список просроченных
                }
            }
        }
//        System.out.println(overdueBooks);

        return overdueBooks; // Возвращаем список просроченных книг
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> findPersonByName(String name){
        Optional<Person> foundPerson = peopleRepository.findPersonByName(name);
        return foundPerson;
    }

    public List<Book> findBookByPerson(int id){
        Person person = peopleRepository.findById(id).orElse(null);
        return person.getBooks();
    }

}
