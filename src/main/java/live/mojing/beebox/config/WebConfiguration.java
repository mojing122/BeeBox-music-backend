package live.mojing.beebox.config;

import jakarta.annotation.Resource;
import live.mojing.beebox.interceptor.AuthorizeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    AuthorizeInterceptor authorizeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**")
        ;
    }
}
