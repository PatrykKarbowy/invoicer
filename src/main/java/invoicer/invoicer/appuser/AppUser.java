package invoicer.invoicer.appuser;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName ="user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotNull
    private String password;
    @NotNull
    @Size(min = 2, message = "First name should have at least 2 characters")
    private String firstName;
    @NotNull
    @Size(min = 2, message = "Last name should have at least 2 characters")
    private String lastName;
    @NotNull
    @Size(min = 2, message = "City should have at least 2 characters")
    private String city;
    @NotNull
    @Size(min = 2, message = "Street should have at least 2 characters")
    private String street;
    @NotNull
    private String postalCode;
    @NotNull
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private boolean enabled = false;

    public AppUser(String email, String password, String firstName, String lastName, String city, String street, String postalCode, String phoneNumber, AppUserRole appUserRole){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.appUserRole = appUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }
}
