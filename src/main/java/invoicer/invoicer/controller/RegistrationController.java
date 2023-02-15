package invoicer.invoicer.controller;

import invoicer.invoicer.request.RegistrationRequest;
import invoicer.invoicer.response.ConfirmationTokenResponse;
import invoicer.invoicer.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("register")
    public ConfirmationTokenResponse register(@Valid @RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping("confirm")
    public ConfirmationTokenResponse confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
