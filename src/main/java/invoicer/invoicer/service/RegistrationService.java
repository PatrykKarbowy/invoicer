package invoicer.invoicer.service;

import invoicer.invoicer.model.AppUser;
import invoicer.invoicer.exception.AppUserNotFoundException;
import invoicer.invoicer.model.role.AppUserRole;
import invoicer.invoicer.validation.EmailValidator;
import invoicer.invoicer.request.RegistrationRequest;
import invoicer.invoicer.model.ConfirmationToken;
import invoicer.invoicer.response.ConfirmationTokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public ConfirmationTokenResponse register(RegistrationRequest request){
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
    public ConfirmationTokenResponse confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
                () -> new AppUserNotFoundException("Token not found")
        );
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return ConfirmationTokenResponse
                .builder()
                .token("Confirmed")
                .build();
    }
}
