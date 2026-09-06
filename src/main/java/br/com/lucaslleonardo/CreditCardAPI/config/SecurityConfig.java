package br.com.lucaslleonardo.CreditCardAPI.config;


import br.com.lucaslleonardo.CreditCardAPI.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider(userDetailsServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/usuario/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/cartao/*")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/cartao/*/delete")
                        .hasRole("ADMIN")

                        .requestMatchers("/cartao/**")
                        .hasAnyRole("CLIENTE", "ADMIN")



                        .requestMatchers(HttpMethod.GET, "/cliente/listaClientes")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/cliente/*")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/cliente/delete/*")
                        .hasRole("ADMIN")

                        .requestMatchers("/cliente/**")
                        .hasAnyRole("CLIENTE", "ADMIN")



                        .requestMatchers(HttpMethod.DELETE, "/conta/*/*/delete")
                        .hasRole("ADMIN")

                        .requestMatchers("/conta/**")
                        .hasAnyRole("CLIENTE", "ADMIN")



                        .requestMatchers(HttpMethod.PUT, "/compra/cancelar/*")
                        .hasRole("ADMIN")

                        .requestMatchers("/compra/**")
                        .hasAnyRole("CLIENTE", "ADMIN")



                        .requestMatchers("/fatura/**")
                        .hasAnyRole("CLIENTE", "ADMIN")



                        .requestMatchers("/pagamento/**")
                        .hasAnyRole("CLIENTE", "ADMIN")


                        .anyRequest().authenticated()
                )
                
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
        

    }

}
