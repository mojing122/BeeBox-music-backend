package live.mojing.beebox.config;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.mojing.beebox.mapper.entity.RestBean;
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
                // 配置请求的权限
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index").hasRole("user")  // 用户角色为"user"可以访问 / 和 /index
                        .requestMatchers("/api/user/**").hasRole("admin")  // 用户角色为 "admin"可以访问 /api/user/**
                        .requestMatchers("/api/auth/**").permitAll()  // 所有用户都可以访问 /api/auth/**
                        .anyRequest().authenticated()  // 其他所有请求需要进行认证
                )
                // 配置登录功能
                .formLogin(formLogin -> formLogin
                        .loginProcessingUrl("/api/auth/login")  // 配置登录表单提交的URL
                        .successHandler(this::onAuthenticationSuccess)  // 登录成功时的处理器
                        .failureHandler(this::onAuthenticationFailure)  // 登录失败时的处理器
                )
                // 配置登出功能
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")  // 配置登出的URL
                        .logoutSuccessHandler(this::onAuthenticationSuccess)  // 登出成功时的处理器
                )
                // 配置记住我功能
                .rememberMe(
                        rememberMe -> rememberMe
                                .rememberMeParameter("remember")  // 配置记住我提交的参数名
                                .tokenRepository(tokenRepository)  // 配置记住我功能使用的tokenRepository
                                .tokenValiditySeconds(3600 * 24 * 7)  // 配置token的有效时长
                )
                // 配置用户认证业务对象
                .userDetailsService(authorizeService)
                // 配置异常处理
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(this::commence)
                )
                // 禁用跨站请求伪造保护
                .csrf(AbstractHttpConfigurer::disable)
                // 配置跨域资源共享
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
