package ru.spring.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.spring.library.models.Book;
import ru.spring.library.models.Person;

import java.util.List;
import java.util.Optional;

//@Component
public class PersonDAO {

//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public PersonDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Person> index() {
//        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
//    }
//
//    public Person show(int id){
//        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
//                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
//    }
//
//    public void save(Person person){
//        jdbcTemplate.update("INSERT INTO person(birthYear, name ) VALUES(?, ?)",
//               person.getBirthYear(), person.getName());
//    }
//
//    public void update(int id, Person updatePerson){
//        jdbcTemplate.update("UPDATE person SET birthYear=?, name=? WHERE id=?",
//                updatePerson.getBirthYear(), updatePerson.getName(), id);
//    }
//
//    public void delete(int id){
//        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
//    }
//
//    public Optional<Person> getPersonByName(String name){
//        return jdbcTemplate.query("SELECT * FROM person WHERE name=?", new Object[]{name},
//                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
//    }
//
//    public List<Book> getBookByPerson(int id){
//        return jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new Object[]{id},
//                new BeanPropertyRowMapper<>(Book.class));
//
//    }



}
