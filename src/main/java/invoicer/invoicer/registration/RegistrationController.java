package invoicer.invoicer.registration;

import invoicer.invoicer.registration.token.ConfirmationTokenResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "register")
    public ConfirmationTokenResponse register(@Valid @RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public ConfirmationTokenResponse confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
