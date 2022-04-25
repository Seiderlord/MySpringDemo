package at.demo.tests;

import at.demo.model.User;
import at.demo.model.UserRole;
import at.demo.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Some very basic tests for {@link UserService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDataInitialization() {
        Assert.assertEquals("Insufficient amount of users initialized for test data source", 4, userService.getAllUsers().size());
        for (User user : userService.getAllUsers()) {
            if ("admin".equals(user.getUsername())) {
                Assert.assertTrue("User \"admin\" does not have role ADMIN", user.getUserRoles().contains(UserRole.ADMIN));
                Assert.assertNotNull("User \"admin\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"admin\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"admin\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"admin\" has a updateDate defined", user.getUpdateDate());
            } else if ("manager".equals(user.getUsername())) {
                Assert.assertTrue("User \"manager\" does not have role MANAGER", user.getUserRoles().contains(UserRole.MANAGER));
                Assert.assertNotNull("User \"manager\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"manager\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"manager\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"manager\" has a updateDate defined", user.getUpdateDate());
            } else if ("employee".equals(user.getUsername())) {
                Assert.assertTrue("User \"employee\" does not have role EMPLOYEE", user.getUserRoles().contains(UserRole.EMPLOYEE));
                Assert.assertNotNull("User \"employee\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"employee\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"employee\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"employee\" has a updateDate defined", user.getUpdateDate());
            } else if ("customer".equals(user.getUsername())) {
                Assert.assertTrue("User \"customer\" does not have role EMPLOYEE", user.getUserRoles().contains(UserRole.CUSTOMER));
                Assert.assertNotNull("User \"customer\" does not have a createUser defined", user.getCreateUser());
                Assert.assertNotNull("User \"customer\" does not have a createDate defined", user.getCreateDate());
                Assert.assertNull("User \"customer\" has a updateUser defined", user.getUpdateUser());
                Assert.assertNull("User \"customer\" has a updateDate defined", user.getUpdateDate());
            } else {
                Assert.fail("Unknown user \"" + user.getUsername() + "\" loaded from test data source via UserService.getAllUsers");
            }
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDeleteUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeDeletedUser = userService.loadUser("employee");
        Assert.assertNotNull("Employee could not be loaded from test data source", toBeDeletedUser);
        int numberOfUsers = userService.getAllUsers().size();
        userService.deleteUser(toBeDeletedUser);

        Assert.assertEquals("No user has been deleted after calling UserService.deleteUser", numberOfUsers - 1, userService.getAllUsers().size());
        User deletedUser = userService.loadUser("employee");
        Assert.assertNull("Deleted User1 could still be loaded from test data source via UserService.loadUser", deletedUser);


        for (User remainingUser : userService.getAllUsers()) {
            Assert.assertNotEquals("Deleted User1 could still be loaded from test data source via UserService.getAllUsers", toBeDeletedUser.getUsername(), remainingUser.getUsername());
        }
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeSavedUser = userService.loadUser("manager");
        Assert.assertNotNull("manager could not be loaded from test data source", toBeSavedUser);

        Assert.assertNull("User \"manager\" has a updateUser defined", toBeSavedUser.getUpdateUser());
        Assert.assertNull("User \"manager\" has a updateDate defined", toBeSavedUser.getUpdateDate());
        toBeSavedUser.setEmail("changed-email@whatever.wherever");
        userService.saveUser(toBeSavedUser);

        User freshlyLoadedUser = userService.loadUser("manager");

        Assert.assertNotNull("manager could not be loaded from test data source after being saved", freshlyLoadedUser);
        Assert.assertNotNull("User \"manager\" does not have a updateUser defined after being saved", freshlyLoadedUser.getUpdateUser());
        Assert.assertEquals("User \"manager\" has wrong updateUser set", adminUser, freshlyLoadedUser.getUpdateUser());
        Assert.assertNotNull("User \"manager\" does not have a updateDate defined after being saved", freshlyLoadedUser.getUpdateDate());
        Assert.assertEquals("User \"manager\" does not have a the correct email attribute stored being saved", "changed-email@whatever.wherever", freshlyLoadedUser.getEmail());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User toBeCreatedUser = new User();
        toBeCreatedUser.setUsername("newuser");
        toBeCreatedUser.setPassword("passwd");
        toBeCreatedUser.setEnabled(true);
        toBeCreatedUser.setFirstName("New");
        toBeCreatedUser.setLastName("User");
        toBeCreatedUser.setEmail("new-email@whatever.wherever");
        toBeCreatedUser.setPhone("+12 345 67890");
        toBeCreatedUser.setUserRoles(Sets.newSet(UserRole.EMPLOYEE, UserRole.MANAGER));
        userService.saveUser(toBeCreatedUser);

        User freshlyCreatedUser = userService.loadUser("newuser");
        Assert.assertNotNull("New user could not be loaded from test data source after being saved", freshlyCreatedUser);
        Assert.assertEquals("User \"newuser\" does not have a the correct username attribute stored being saved", "newuser", freshlyCreatedUser.getUsername());
        Assert.assertEquals("User \"newuser\" does not have a the correct password attribute stored being saved", "passwd", freshlyCreatedUser.getPassword());
        Assert.assertEquals("User \"newuser\" does not have a the correct firstName attribute stored being saved", "New", freshlyCreatedUser.getFirstName());
        Assert.assertEquals("User \"newuser\" does not have a the correct lastName attribute stored being saved", "User", freshlyCreatedUser.getLastName());
        Assert.assertEquals("User \"newuser\" does not have a the correct email attribute stored being saved", "new-email@whatever.wherever", freshlyCreatedUser.getEmail());
        Assert.assertEquals("User \"newuser\" does not have a the correct phone attribute stored being saved", "+12 345 67890", freshlyCreatedUser.getPhone());
        Assert.assertTrue("User \"newuser\" does not have role MANAGER", freshlyCreatedUser.getUserRoles().contains(UserRole.MANAGER));
        Assert.assertTrue("User \"newuser\" does not have role EMPLOYEE", freshlyCreatedUser.getUserRoles().contains(UserRole.EMPLOYEE));
        Assert.assertNotNull("User \"newuser\" does not have a createUser defined after being saved", freshlyCreatedUser.getCreateUser());
        Assert.assertEquals("User \"newuser\" has wrong createUser set", adminUser, freshlyCreatedUser.getCreateUser());
        Assert.assertNotNull("User \"newuser\" does not have a createDate defined after being saved", freshlyCreatedUser.getCreateDate());
    }

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testMarkUserAsDeleted() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeDeletedUser = userService.loadUser("employee");
        Assert.assertNotNull("Employee user could not be loaded from test data source", toBeDeletedUser);
        int numberOfUsers = userService.getAllUsers().size();

        userService.markUserAsDeleted(toBeDeletedUser);

        Assert.assertEquals("User has been deleted after calling UserService.markUserAsDeleted", numberOfUsers, userService.getAllUsers().size());
        User deletedUser = userService.loadUser("employee");
        Assert.assertNotNull("Marked user could not be loaded from test data source via UserService.loadUser", deletedUser);
        Assert.assertNotNull(deletedUser.getUpdateUser());
        Assert.assertEquals(adminUser, deletedUser.getUpdateUser());
        Assert.assertTrue("The deleted flag of the marked user is not true after calling userService.markUserAsDeleted", deletedUser.isDeleted());

    }
    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testDisableAndEnableUser() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);
        User toBeDisabledUser = userService.loadUser("employee");
        Assert.assertNotNull("Employee user could not be loaded from test data source", toBeDisabledUser);
        int numberOfUsers = userService.getAllUsers().size();

        userService.disableUser(toBeDisabledUser);

        Assert.assertEquals("Number of users changed after calling UserService.disableUser", numberOfUsers, userService.getAllUsers().size());
        User disabledUser = userService.loadUser("employee");
        Assert.assertNotNull("Marked user could not be loaded from test data source via UserService.loadUser", disabledUser);
        Assert.assertNotNull(disabledUser.getUpdateUser());
        Assert.assertEquals(adminUser, disabledUser.getUpdateUser());
        Assert.assertFalse("The enabled flag of the marked user is not false after calling userService.disableUser", disabledUser.isEnabled());

        userService.enableUser(disabledUser);

        Assert.assertEquals("Number of users changed after calling UserService.disableUser", numberOfUsers, userService.getAllUsers().size());
        User enabledUser = userService.loadUser("employee");
        Assert.assertNotNull("Marked user could not be loaded from test data source via UserService.loadUser", enabledUser);
        Assert.assertNotNull(enabledUser.getUpdateUser());
        Assert.assertEquals(adminUser, enabledUser.getUpdateUser());
        Assert.assertTrue("The enabled flag of the marked user is not true after calling userService.disableUser", enabledUser.isEnabled());

    }

//    @Test(expected = org.springframework.orm.jpa.JpaSystemException.class)
    @Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testExceptionForEmptyUsername() {
        User adminUser = userService.loadUser("admin");
        Assert.assertNotNull("Admin user could not be loaded from test data source", adminUser);

        User toBeCreatedUser = new User();
        userService.saveUser(toBeCreatedUser);
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadUsers() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUsers() {
        for (User user : userService.getAllUsers()) {
            Assert.fail("Call to userService.getAllUsers should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testUnauthorizedLoadUser() {
        User user = userService.loadUser("admin");
        Assert.fail("Call to userService.loadUser should not work without proper authorization for other users than the authenticated one");
    }

    @WithMockUser(username = "user1", authorities = {"EMPLOYEE"})
    public void testAuthorizedLoadUser() {
        User user = userService.loadUser("user1");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "user1", user.getUsername());
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void testUnauthorizedSaveUser() {
        User user = userService.loadUser("employee");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "employee", user.getUsername());
        userService.saveUser(user);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "employee", authorities = {"EMPLOYEE"})
    public void testUnauthorizedDeleteUser() {
        User user = userService.loadUser("employee");
        Assert.assertEquals("Call to userService.loadUser returned wrong user", "employee", user.getUsername());
        userService.deleteUser(user);
    }

}
