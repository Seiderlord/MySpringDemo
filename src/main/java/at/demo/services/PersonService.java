package at.demo.services;

import at.demo.model.Person;
import at.demo.model.User;
import at.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Returns a collection of all persons.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<Person> getAllPersons() {
        Collection<Person> personCollection = personRepository.findAll();
        if (personCollection ==  null){
            personCollection = Collections.EMPTY_LIST;
        }
        return personCollection;
    }

    /**
     * Saves the person. This method will also set {@link Person#createDate} for new
     * entities or {@link Person#updateDate} for updated entities
     *
     * @param person the person to save
     * @return the updated person
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Person savePerson(Person person) {
        if (person.isNew()) {
            person.setCreateDate(LocalDateTime.now());
        } else {
            person.setUpdateDate(LocalDateTime.now());
        }
        return personRepository.save(person);
    }

    /**
     * Loads a single person identified by its id.
     *
     * @param id the id to search for
     * @return the person with the given id
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Person loadPerson(String id) {
        return personRepository.findById(id);
    }


    /**
     * Deletes the person.
     *
     * @param person the person to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePerson(Person person) {
        personRepository.delete(person);
        // :TODO: write some audit log stating who and when this user was permanently deleted.
    }


}
