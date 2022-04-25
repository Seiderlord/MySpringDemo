package at.demo.utils;

import at.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getUserRoles();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO: ADD EXPIRATION DATE
        return this.user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO: ADD EXPIRATION DATE
        return this.user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO: ADD EXPIRATION DATE
        return this.user.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    //getters and setters if required
}
