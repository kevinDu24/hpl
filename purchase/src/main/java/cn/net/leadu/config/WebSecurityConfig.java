package cn.net.leadu.config;


import cn.net.leadu.security.RestAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by LEO on 2015/9/12.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }


    @Bean
    RestAuthenticationEntryPoint restAuthenticationEntryPoint(){

        return new RestAuthenticationEntryPoint();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, a) -> response.setStatus(HttpServletResponse.SC_NO_CONTENT))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/tpl/**", "/l10n/**", "/assets/**", "/fonts/**","/libs/**","/index.html","/", "/img/**", "/system/sendCode",
                "/system/verifyCode" ,"/attachment/**","/approval/**","hpl_vehicle_attachment.html", "/files/**","/order/**"
                );

    }
}
