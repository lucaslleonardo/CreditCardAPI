package br.com.lucaslleonardo.CreditCardAPI.service;


import br.com.lucaslleonardo.CreditCardAPI.config.TokenProvider;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.LoginRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private Long expiration;

    public TokenResponse login(LoginRequest loginRequest) throws Exception {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = tokenProvider.generateToken(userDetails);

            return new TokenResponse(token, expiration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
