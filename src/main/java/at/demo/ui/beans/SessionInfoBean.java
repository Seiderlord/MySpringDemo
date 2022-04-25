package at.demo.ui.beans;

import at.demo.model.User;
import at.demo.model.UserRole;
import at.demo.repositories.UserRepository;
import at.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Session information bean to retrieve session-specific parameters.
 *
 */
@Component
@Scope("application")
public class SessionInfoBean {

    @Autowired
    private UserRepository userRepository;

    /**
     * Attribute to cache the current user.
     */
    private User currentUser;

    /**
     * Returns the currently logged on user, null if no user is authenticated
     * for this session.
     *
     * @return
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            String currentUserName = getCurrentUserName();
            if (currentUserName.isEmpty()) {
                return null;
            }
            currentUser = userRepository.findFirstByUsername(currentUserName);
        }
        return currentUser;
    }

    /**
     * Returns the username of the user for this session, empty string if no
     * user has been authenticated for this session.
     *
     * @return
     */
    public String getCurrentUserName() {
        if (!isLoggedIn()) {
            return "";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        return name;
    }

    /**
     * Returns the roles of the user for this session as space-separated list,
     * empty string if no user has been authenticated for this session-
     *
     * @return
     */
    public String getCurrentUserRoles() {
        if (!isLoggedIn()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority role : auth.getAuthorities()) {
            sb.append(role.getAuthority());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Checks if a user is authenticated for this session.
     *
     * @return true if a non-anonymous user has been authenticated, false
     * otherwise
     */
    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.isAuthenticated() && !auth.getName().equals("anonymousUser");
        } else {
            return false;
        }
    }

    /**
     * Checks if the user for this session has the given role (c.f.
     * {@link UserRole}).
     *
     * @param role the role to check for as string
     * @return true if a user is authenticated and the current user has the
     * given role, false otherwise
     */
    public boolean hasRole(String role) {
        if (role == null || role.isEmpty() || !isLoggedIn()) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (role.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if the user for this session has any role of the given roles (c.f.
     * {@link UserRole}).
     *
     * @param roles the roles to check for as string
     * @return true if a user is authenticated and the current user has one of the
     * given roles, false otherwise
     */
    public boolean hasAnyRole(String... roles) {
        for (String role: roles) {
            if(this.hasRole(role))
                return true;
        }
        return false;
    }

}
