package at.demo.ui.controllers;

import at.demo.model.Person;
import at.demo.model.User;
import at.demo.services.PersonService;
import at.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user detail view.
 *
 */
@Component
@Scope("view")
public class PersonDetailController {

    @Autowired
    private PersonService personService;

    /**
     * Attribute to cache the currently displayed person
     */
    private Person person;

    /**
     * Sets the currently displayed person and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadPerson()}, {@link #doSavePerson()} and
     * {@link #doDeletePerson()}.
     *
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;
        doReloadPerson();
    }

    /**
     * Returns the currently displayed person.
     *
     * @return
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Action to force a reload of the currently displayed person.
     */
    public void doReloadPerson() {
        person = personService.loadPerson(person.getId());
    }

    /**
     * Action to save the currently displayed person.
     */
    public void doSavePerson() {
        person = this.personService.savePerson(person);
    }

    /**
     * Action to delete the currently displayed person.
     */
    public void doDeletePerson() {
        this.personService.deletePerson(person);
        person = null;
    }

}
