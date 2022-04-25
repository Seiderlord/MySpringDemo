package at.demo.services;

import at.demo.model.AvatarImage;
import at.demo.model.Person;
import at.demo.model.User;
import at.demo.model.UserRole;
import at.demo.repositories.AvatarImageRepository;
import at.demo.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import at.demo.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AvatarImageRepository avatarImageRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private SessionInfoBean sessionInfoBean;


    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns a collection of all clients.
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','EMPLOYEE')")
    public Collection<User> getAllClients() {
        return userRepository.findAllByUserRolesContains(UserRole.CUSTOMER);
    }
    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
    public User loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link Person#createDate} for new
     * entities or {@link Person#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createUser}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User saveUser(User user) {
        if (user.isNew()) {
            user.setCreateDate(LocalDateTime.now());
            user.setCreateUser(sessionInfoBean.getCurrentUser());
            auditLogService.logCreateUser(user);
            return userRepository.save(user);
        } else {
            user.setUpdateDate(LocalDateTime.now());
            user.setUpdateUser(sessionInfoBean.getCurrentUser());
            auditLogService.logUpdateUser(user);
            return userRepository.save(user);
        }
    }


    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(User user) {
        userRepository.delete(user);
        auditLogService.logDeleteUser(user);
    }


    /**
     * Marks the user as deleted.
     *
     * @param user the user to mark as deleted
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User markUserAsDeleted(User user) {
        User userToDelete = userRepository.findFirstByUsername(user.getUsername());
        userToDelete.setUpdateDate(LocalDateTime.now());
        userToDelete.setUpdateUser(sessionInfoBean.getCurrentUser());
        userToDelete.setDeleted(true);
        auditLogService.logDeleteUser(user);
        return userRepository.save(userToDelete);
    }

    /**
     * Disable the user.
     *
     * @param user the user to disable
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User disableUser(User user) {
        User userToDisable = userRepository.findFirstByUsername(user.getUsername());
        userToDisable.setUpdateDate(LocalDateTime.now());
        userToDisable.setUpdateUser(sessionInfoBean.getCurrentUser());
        userToDisable.setEnabled(false);

        auditLogService.logUpdateUser(userToDisable);
        return userRepository.save(userToDisable);
    }

    /**
     * Enable the user.
     *
     * @param user the user to enable
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User enableUser(User user) {
        User userToDisable = userRepository.findFirstByUsername(user.getUsername());
        userToDisable.setUpdateDate(LocalDateTime.now());
        userToDisable.setUpdateUser(sessionInfoBean.getCurrentUser());
        userToDisable.setEnabled(true);

        auditLogService.logUpdateUser(userToDisable);
        return userRepository.save(userToDisable);
    }

    @PreAuthorize("isAuthenticated()")
    public AvatarImage loadProfileImage(){
        return avatarImageRepository.findByUser(sessionInfoBean.getCurrentUser());
    }
}
