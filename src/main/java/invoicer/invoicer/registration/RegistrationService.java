package invoicer.invoicer.registration;

import invoicer.invoicer.appuser.AppUser;
import invoicer.invoicer.appuser.AppUserRole;
import invoicer.invoicer.appuser.AppUserService;
import invoicer.invoicer.registration.token.ConfirmationToken;
import invoicer.invoicer.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    public ResponseEntity<String> register(RegistrationRequest request){
        boolean isValid = emailValidator.test(request.getEmail());

        if(!isValid){
            throw new IllegalStateException("email is not valid");
        }

        return appUserService.signUpUser(
                new AppUser(
                        request.getEmail(),
                        request.getPassword(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getCity(),
                        request.getStreet(),
                        request.getPostalCode(),
                        request.getPhoneNumber(),
                        AppUserRole.CLIENT
                )
        );
    }
    public ResponseEntity<String> confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found")
        );
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return ResponseEntity.ok("Your account has been confirmed");
    }
}
