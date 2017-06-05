package org.wickedsource.coderadar.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wickedsource.coderadar.core.configuration.CoderadarConfiguration;
import org.wickedsource.coderadar.security.service.TokenService;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CoderadarSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private UserDetailsService userDetailsService;

  private TokenService tokenService;

  private CoderadarConfiguration coderadarConfiguration;

  @Autowired
  public CoderadarSecurityConfiguration(
      UserDetailsService userDetailsService,
      TokenService tokenService,
      CoderadarConfiguration coderadarConfiguration) {
    this.userDetailsService = userDetailsService;
    this.tokenService = tokenService;
    this.coderadarConfiguration = coderadarConfiguration;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // user CoderadarUserDetailService for authentication
    auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    return new AuthenticationTokenFilter(tokenService, new AccessDeniedHandlerImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();

    if (coderadarConfiguration.getAuthentication().getEnabled()) {
      http.authorizeRequests()
          // only these endpoints can be called without authentication
          .antMatchers("/actuator", "/user/auth", "/user/registration", "/user/refresh")
          .permitAll()
          .anyRequest()
          .authenticated();

      // put JSON Web Token authentication before other ones
      http.addFilterBefore(
          authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
  }
}
