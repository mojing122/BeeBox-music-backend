package live.mojing.beebox.config;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.mojing.beebox.entity.RestBean;
import live.mojing.beebox.service.AuthorizeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository tokenRepository) throws Exception{
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index").hasRole("user")
                        .requestMatchers("/api/user/**").hasRole("admin")
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onAuthenticationSuccess)
                )
                .rememberMe(
                        rememberMe -> rememberMe
                                .rememberMeParameter("remember")
                                .tokenRepository(tokenRepository)
                                .tokenValiditySeconds(3600 * 24 * 7)
                )
                .userDetailsService(authorizeService)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(this::commence))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .build();
    }

    /**
     * 配置跨域
     * @return source
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }



    /**
     * 配置密码加密器
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 登陆、退出成功操作
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        if(request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功")));
        else if (request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录")));
    }

    /**
     * 登陆失败操作
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));
    }

    /**
     * 未登录访问操作
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,authException.getMessage())));
    }

    /**
     * 通过token实现记住登录
     */
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
}
