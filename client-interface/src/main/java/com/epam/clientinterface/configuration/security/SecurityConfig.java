package com.epam.clientinterface.configuration.security;

import com.epam.clientinterface.domain.UserDetailAuth;
import com.epam.clientinterface.repository.UserRepository;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger;
    private final UserRepository userRepository;

    public SecurityConfig(Logger logger, UserRepository userRepository) {
        super();
        this.logger = logger;
        this.userRepository = userRepository;

        // Inherit security context in async function calls
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    //Configure the authentication manager with the correct provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userRepository.findByEmailWithRoles(username).map(UserDetailAuth::new)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username))));
    }

    @Override
    //Configure web security (public URLs, private URLs, authorization, etc.)
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        // Set unauthorized requests exception handler
        http = http.exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                logger.error("Unauthorized request - {}", authException.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
            }).and();

        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/user/registration").permitAll()
            .antMatchers(HttpMethod.POST, "/bank/public/login").permitAll()
            .antMatchers(HttpMethod.GET, "/bank/secured/**").authenticated()
            .anyRequest().authenticated();

        // Add filters before ss
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(initialAuthenticationFilter(), JwtTokenFilter.class);
    }

    // Expose authentication manager bean
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Used by spring security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Bean
    JwtTokenFilter jwtTokenFilter() throws Exception {
        JwtTokenFilter jwtTokenFilterBean =
            new JwtTokenFilter(userRepository, new AndRequestMatcher(new AntPathRequestMatcher("/**"),
                new NegatedRequestMatcher(new AntPathRequestMatcher("/login")),
                new NegatedRequestMatcher(new AntPathRequestMatcher("/user/registration"))));
        jwtTokenFilterBean.setAuthenticationManager(authenticationManagerBean());
        return jwtTokenFilterBean;
    }

    @Bean
    InitialAuthenticationFilter initialAuthenticationFilter() throws Exception {
        InitialAuthenticationFilter initialAuthenticationFilter = new InitialAuthenticationFilter();
        initialAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return initialAuthenticationFilter;
    }
}
