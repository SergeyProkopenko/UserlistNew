package com.project.userlistnew.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                    .authorizeRequests()
                    .antMatchers( "/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/logoutSuccessfulPage")
                    .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService()
//        auth.inMemoryAuthentication().withUser("user").password("{noop}password").roles("USER");

    }
}
