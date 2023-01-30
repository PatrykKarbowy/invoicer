package invoicer.invoicer.registration;

import invoicer.invoicer.appuser.AppUser;
import invoicer.invoicer.appuser.AppUserRole;
import invoicer.invoicer.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    public String register(RegistrationRequest request){
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

    public String confirmToken(String token){
        return null;
    }
}
