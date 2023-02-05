package invoicer.invoicer.controller;

import invoicer.invoicer.request.AuthenticationRequest;
import invoicer.invoicer.request.RefreshTokenRequest;
import invoicer.invoicer.response.AuthenticationResponse;
import invoicer.invoicer.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    @PostMapping("refresh")
    public AuthenticationResponse refresh(@RequestBody RefreshTokenRequest request){
        return authenticationService.refresh(request);
    }


}
