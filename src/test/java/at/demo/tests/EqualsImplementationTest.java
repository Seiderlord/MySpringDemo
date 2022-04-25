package at.demo.tests;

import at.demo.model.Person;
import at.demo.model.User;
import at.demo.model.UserRole;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract.
 * {@see <a href="http://www.jqno.nl/equalsverifier/">Equalsverifier</a>  for more
 * information.}
 *
 */
public class EqualsImplementationTest {

    @Test
    public void testUserEqualsContract() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setId("PER00-0000001");
        User user2 = new User();
        user2.setUsername("user2");
        user1.setId("PER00-0000002");

        EqualsVerifier.forClass(Person.class).withPrefabValues(User.class, user1, user2).suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }

    @Test
    public void testUserRoleEqualsContract() {
        EqualsVerifier.forClass(UserRole.class).verify();
    }

}
