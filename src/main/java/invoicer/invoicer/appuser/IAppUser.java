package invoicer.invoicer.appuser;

import invoicer.invoicer.registration.token.ConfirmationTokenResponse;

import java.util.List;
import java.util.Optional;

public interface IAppUser {
    List<AppUser> getUsers();
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByEmail(String email);
    ConfirmationTokenResponse signUpUser(AppUser appUser);
    AppUser save(AppUser appUser);
    void deleteById(Long id);
    int enableAppUser(String email);
}
