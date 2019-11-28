package net.chrisgrollier.cloud.infra.admin.server.config;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

/**
 * A simple security configuration using http basic authentication to prevent
 * undesired client registration to our admin server. Relies on an in memory
 * authentication with 2 declared users, one for the clients and the other for
 * the ui. These users/password are 'client/password' and 'admin/password'.
 * 
 * @author Atos
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AdminServerProperties adminServer;

	public WebSecurityConfig(AdminServerProperties adminServer) {
		super();
		this.adminServer = adminServer;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(this.adminServer.getContextPath() + "/");
		// @formatter:off
		http
			.authorizeRequests()
				.antMatchers(this.adminServer.getContextPath() + "/assets/**").permitAll()
				.antMatchers(this.adminServer.getContextPath() + "/login").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage(this.adminServer.getContextPath() + "/login")
				.successHandler(successHandler)
				.and()
			.logout()
				.logoutUrl(this.adminServer.getContextPath() + "/logout")
				.and()
			.httpBasic()
				.and()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringRequestMatchers(
					new AntPathRequestMatcher(this.adminServer.getContextPath() + "/instances",
							HttpMethod.POST.toString()),
					new AntPathRequestMatcher(this.adminServer.getContextPath() + "/instances/*",
							HttpMethod.DELETE.toString()),
					new AntPathRequestMatcher(this.adminServer.getContextPath() + "/actuator/**"))
				.and()
			.rememberMe()
				.key(UUID.randomUUID().toString())
				.tokenValiditySeconds(1209600);
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// @formatter:off
		auth.inMemoryAuthentication()
				.withUser("client").password("{noop}secret").roles("USER")
			.and()
				.withUser("admin").password("{noop}secret").roles("USER", "ADMIN");
		// @formatter:on
	}

}
