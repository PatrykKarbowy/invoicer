package invoicer.invoicer.service;

import invoicer.invoicer.model.AppUser;
import invoicer.invoicer.repository.AppUserRepository;
import invoicer.invoicer.request.AuthenticationRequest;
import invoicer.invoicer.request.RefreshTokenRequest;
import invoicer.invoicer.response.AuthenticationResponse;
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
        AppUser appUser = appUserRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtAccessToken = jwtService.generateAccessToken(appUser);
        var jwtRefreshToken = jwtService.generateRefreshToken(appUser);

        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AuthenticationResponse refresh(RefreshTokenRequest request){
        String userEmail = jwtService.extractUsername(request.getRefreshToken());
        AppUser appUser = appUserRepository.findByEmail(userEmail).orElseThrow();
        var jwtAccessToken = jwtService.generateAccessToken(appUser);
        var jwtRefreshToken = jwtService.generateRefreshToken(appUser);
        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
}
