package com.egg.News;

import com.egg.News.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Hernan
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    // instanciar un usuario servicio
    @Autowired
    public UsuarioServicio usuarioServicio;

    // posteriormente debemos generar un metodo configureGlobal con la notacion autowired
    // va a recibir un objeto del tipo Authentication
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // los recursos alvergados en esta carpeta son accesibles para todos sin necesidad de logueo o permiso. 
        //        http.authorizeRequests().antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll(); codigo inicial ahora añadimmos nuevos permisos
        http.
                authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/periodista/*").access("hasRole('ROLE_PERIODISTA') or hasRole('ROLE_ADMIN')")
                .antMatchers("/css/*", "/js/*", "/img/*", "/**")
                .permitAll()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/inicio")
                .permitAll()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
                .and().csrf()
                .disable();

    } //nota --> para lograr el acceso tanto del ADMIN como del PERIDISTA agregue la sentencia 
      // andMatchert("/periodista/*").acces("hasRole('ROLE_PERIODISTA') or hasRole('ROLE_ADMIN')"
}