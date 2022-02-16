package com.epam.bank.operatorinterface.configuration;

import com.epam.bank.operatorinterface.configuration.security.JwtAuthenticationEntryPoint;
import com.epam.bank.operatorinterface.configuration.security.filter.BasicAuthenticationFilter;
import com.epam.bank.operatorinterface.configuration.security.handler.BasicAuthenticationSuccessHandler;
import com.epam.bank.operatorinterface.configuration.security.handler.JwtAccessDeniedHandler;
import com.epam.bank.operatorinterface.configuration.security.provider.BasicAuthenticationProvider;
import com.epam.bank.operatorinterface.configuration.security.util.JwtUtil;
import com.epam.bank.operatorinterface.service.UserDetailsServiceImpl;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsServiceImpl;
    private JwtUtil jwtUtil;

    //set up credentials source for authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }

    //set up authorisation
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disable CSRF, enable CORS
        http.cors().disable().csrf().disable();

        //Stateless because user will be authorised by token
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .oauth2ResourceServer()
            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
            .accessDeniedHandler(new JwtAccessDeniedHandler())
            .jwt()
            .jwtAuthenticationConverter(new JwtAuthenticationConverter());

        http.authenticationProvider(initialAuthenticationProvider());
        http.addFilterBefore(initialAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
            .antMatchers("/**").authenticated()
            .antMatchers(HttpMethod.POST,"/login").permitAll()
            .antMatchers("/registration").permitAll();
    }

    //set up password encoder
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //create bean of authentication manager
    //application failing to start if this bean is removed
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        return new JwtDecoder() {
            @SneakyThrows
            @Override
            public Jwt decode(String token) throws JwtException {
                final JWT jwt = JWTParser.parse(token);
                Map<String, Object> claims = jwt.getJWTClaimsSet().getClaims();
                Map<String, Object> headers = jwt.getHeader().toJSONObject();
                jwtUtil.validateToken(token, userDetailsServiceImpl);
                return Jwt.withTokenValue(token)
                    .headers(h -> h.putAll(headers))
                    .claims(c -> c.putAll(claims))
                    .issuedAt(jwt.getJWTClaimsSet().getIssueTime().toInstant())
                    .expiresAt(jwt.getJWTClaimsSet().getExpirationTime().toInstant())
                    .build();
            }
        };
    }

    @Bean
    BasicAuthenticationSuccessHandler initialAuthenticationSuccessHandler() {
        return  new BasicAuthenticationSuccessHandler();
    }

    @Bean
    BasicAuthenticationFilter initialAuthenticationFilter() throws Exception {
        return new BasicAuthenticationFilter(authenticationManagerBean(),
            initialAuthenticationSuccessHandler());
    }

    @Bean
    BasicAuthenticationProvider initialAuthenticationProvider() {
        return new BasicAuthenticationProvider(userDetailsServiceImpl, getPasswordEncoder(), jwtUtil);
    }
}
