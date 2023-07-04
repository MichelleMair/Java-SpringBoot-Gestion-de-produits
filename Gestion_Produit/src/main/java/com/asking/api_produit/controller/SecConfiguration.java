package com.asking.api_produit.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.asking.api_produit.service.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecConfiguration extends WebSecurityConfigurerAdapter
{
     
    @Bean
    public UserDetailsService userDetailsService() 
    {
        return new CustomUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() 
    {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() 
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception 
    {
        auth.authenticationProvider(authenticationProvider());
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("Charbel")
		.password(passwordEncoder().encode("Charbel050503")).roles("admin");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http.authorizeRequests()
            .antMatchers("/listeAvecCon","/creation/","/saveProduct","/maj/*","/delete/*").authenticated()
            .antMatchers("/users","/deleteUser/*").hasRole("admin")
            .anyRequest().permitAll()
            .and()
            .formLogin()
                .usernameParameter("email")
                .defaultSuccessUrl( "/listeAvecCon")
                .permitAll()
            .and()
            .logout().logoutSuccessUrl("/").permitAll();
    }   
}