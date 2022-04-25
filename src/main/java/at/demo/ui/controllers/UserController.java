package at.demo.ui.controllers;

import at.demo.model.User;
import at.demo.model.UserRole;
import at.demo.services.AuditLogService;
import at.demo.services.UserService;
import org.primefaces.PrimeFaces;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 * Controller for the user list view.
 *
 */
@Component
@Scope("view")
public class UserController implements Serializable{

    @Autowired
    private UserService userService;

    @Autowired
    private PrimeFaces primeFaces;
    /**
     * Attribute to cache the currently displayed user
     */
    private User user;

    private List<UserRole> userRoles;

    public boolean isUserInCreation() {
        return userInCreation;
    }

    public void setUserInCreation(boolean userInCreation) {
        this.userInCreation = userInCreation;
    }
    public void setUserInCreationFalse() {
        this.userInCreation = false;
    }
    private boolean userInCreation;


    @PostConstruct
    public void init() {
        setUserInCreationFalse();
        reloadUsers();
    }

    public void prepareNewUser(){
        this.user = new User();
        this.userRoles = new ArrayList<UserRole>();
        this.userInCreation = true;
    }
    public Collection<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, {@link #doSaveUser()} and
     * {@link #doDeleteUser()}.
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        this.userRoles = new ArrayList<>(user.getUserRoles());
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }


    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {
//        try {
        user.getUserRoles().clear();
        user.getUserRoles().addAll(this.userRoles);
        this.userInCreation = false;
        if (user.isNew()) {
            user = this.userService.saveUser(user);

//                this.auditLogService.logCreateUser(this.sessionInfoBean.getCurrentUser(), this.user);
        } else {

            user = this.userService.saveUser(user);
//                this.auditLogService.logChangeUserData(this.sessionInfoBean.getCurrentUser(), this.user);
        }


        primeFaces.executeScript("PF('userEditDialog').hide()");
//        } catch (EmailAlreadyExistsException e) {
//            facesMsgHelper.errorMessage("The E-Mail you entered is already assigned to another user");
//        } catch (InvalidUsernameException e) {
//            facesMsgHelper.errorMessage("The picked username already exists");
//        } catch (Exception e) {
//            facesMsgHelper.generalError();
//        }
    }


    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() {


//        try {
        this.userService.deleteUser(user);
        //this.auditLogService.logDeleteUser(this.sessionInfoBean.getCurrentUser(), this.user);
        //facesMsgHelper.successMessage("User was deleted");
        user = null;
        this.userRoles = null;

//        } catch (final Exception e) {
//            facesMsgHelper.generalError();
//            this.logger.error("deleteUser", e);
//        }
    }

    public void onItemUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage();
        msg.setSummary("Item unselected: " + event.getObject().toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private List<User> users;

    private List<User> filteredUsers;

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }



    private void reloadUsers() {
        this.users = new ArrayList<>(userService.getAllUsers());
    }


    /**
     * Returns a list of all users.
     *
     * @return
     */
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        User usr = (User) value;
        return (usr.getFirstName().toLowerCase() + usr.getLastName().toLowerCase()).contains(filterText);
    }

}