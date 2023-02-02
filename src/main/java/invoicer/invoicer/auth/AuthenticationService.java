package invoicer.invoicer.auth;

import invoicer.invoicer.appuser.AppUserRepository;
import invoicer.invoicer.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(appUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
