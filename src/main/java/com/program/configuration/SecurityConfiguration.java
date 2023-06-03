package com.program.configuration;

import com.program.detail.UserDetailsServiceImpl;
import com.program.helper.jwt.AuthEntryPointJwt;
import com.program.helper.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

//    @Override
//    protected void configure(HttpSecurity security) throws Exception
//    {
//        security.httpBasic().disable();
//        security.csrf().disable()
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }







//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
////        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.
//                jdbcAuthentication()
//                .usersByUsernameQuery("select email as principal, password as credentials, true from user where email?")
//                .authoritiesByUsernameQuery("select u.email as principal, r.role from user u inner join user_role ur on(u.user_id=ur.user_id) inner join role r on(ur.role_id=r.role_id) where u.email=?")
//                .rolePrefix("ROLE_");
//
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.
//                jdbcAuthentication()
//                .usersByUsernameQuery("select email as principal, password as credentials, true from user where email=?")
//                .authoritiesByUsernameQuery("select u.email as principal, r.role as role from user u inner join user_role ur on(u.user_id=ur.user_id) inner join role r on(ur.role_id=r.role_id) where u.email=?")
//                .dataSource(dataSource)
//                .rolePrefix("ROLE_");
//    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable() // Disable CSRF protection (for simplicity, you may enable it in production)
//                .authorizeRequests()
//                .antMatchers("/category/add").hasRole("Admin")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/profile")
//                .and()
//                .logout()
//                .permitAll();
//    }



}
