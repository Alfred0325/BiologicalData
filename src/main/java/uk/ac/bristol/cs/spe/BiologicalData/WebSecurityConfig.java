package uk.ac.bristol.cs.spe.BiologicalData;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomLogoutHandler logoutHandler;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select userEmail as username, userPwrdHash as password, userEnabled as enabled from user where userEmail=?")
                .authoritiesByUsernameQuery("select userEmail as username, userRole as role from user where userEmail=?")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        PortMapperImpl portMapper = new PortMapperImpl();
        portMapper.setPortMappings(Collections.singletonMap("8080","8080"));
        PortResolverImpl portResolver = new PortResolverImpl();
        portResolver.setPortMapper(portMapper);
        LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint(
                "/login");
        entryPoint.setPortMapper(portMapper);
        entryPoint.setPortResolver(portResolver);

        http.exceptionHandling().accessDeniedPage("/403")
                .authenticationEntryPoint(entryPoint)
                .and().authorizeRequests()
                .antMatchers("/signup/*").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/403").permitAll()
                .antMatchers("/404").permitAll()
                .and()
                .formLogin()//.loginPage("/login.html").permitAll()
                //.and()
                //.logout()//.addLogoutHandler(logoutHandler).permitAll()
                ;
    }
}
