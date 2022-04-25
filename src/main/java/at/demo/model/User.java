package at.demo.model;

import at.demo.ui.converters.UserRoleConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity representing users.
 */
@Entity
public class User extends Person{

    public User(){
        this.userRoles = new HashSet<>();
    }

    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private boolean deleted;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User createUser;
    @ManyToOne
    private User updateUser;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "User_UserRole"
            , joinColumns = @JoinColumn(name = "UserId"),
    uniqueConstraints = {@UniqueConstraint(columnNames = {"UserId","UserRole"})})
    @Column(name = "UserRole", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private Set<UserRole> userRoles;

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", deleted=" + deleted +
                ", createUser=" + createUser.getUsername() +
                (updateUser != null? ", updateUser=" + updateUser.getUsername() : "") +
                ", userRoles=" + userRoles +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }
}
