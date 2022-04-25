package at.demo.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Enumeration of available user roles.
 */
public enum UserRole implements GrantedAuthority {

    ADMIN("Admin"),
    MANAGER("Manager"),
    EMPLOYEE("Employee"),
    CUSTOMER("Customer");

    public static final List<UserRole> USER_ROLES_LIST = new ArrayList<UserRole>() {
        {
            add(ADMIN);
            add(MANAGER);
            add(EMPLOYEE);
            add(CUSTOMER);
        }

    };

    private String name;

    UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getAuthority() {
        return this.name.toUpperCase();
    }

    public static UserRole of(String name) {
        return Stream.of(UserRole.values())
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
