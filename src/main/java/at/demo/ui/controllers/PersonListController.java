package at.demo.ui.controllers;

import at.demo.model.Person;
import at.demo.model.User;
import at.demo.services.PersonService;
import at.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Controller for the user list view.
 *
 */
@Component
@Scope("view")
public class PersonListController {

    @Autowired
    private PersonService personService;

    /**
     * Returns a list of all persons.
     *
     * @return
     */
    public Collection<Person> getPersons() {
        return personService.getAllPersons();
    }

}
