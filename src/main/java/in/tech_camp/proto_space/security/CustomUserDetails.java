package in.tech_camp.proto_space.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.proto_space.entity.UserEntity;
import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    public String getProfile() {
        return user.getProfile();
    }

    public String getAffiliation() {
        return user.getAffiliation();
    }

    public String getPosition() {
        return user.getPosition();
    }

    @Override
    public Collection<? extends GrantedAuthority>
            getAuthorities() {

        return Collections.emptyList();
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
