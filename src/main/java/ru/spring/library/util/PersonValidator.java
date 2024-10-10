package ru.spring.library.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.spring.library.models.Person;
import ru.spring.library.services.PersonService;

@Component
public class PersonValidator implements Validator {

//    private final PersonDAO personDAO;

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }
//    @Autowired
//    public PersonValidator(PersonDAO personDAO) {
//        this.personDAO = personDAO;
//    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(personService.findPersonByName(person.getName()).isPresent())
            errors.rejectValue("name", "", "Человек с таким именом уже существует");
    }
}
