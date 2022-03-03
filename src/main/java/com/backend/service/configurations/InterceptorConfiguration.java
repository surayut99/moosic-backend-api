package com.backend.service.configurations;

import com.backend.service.interceptors.AuthorizationInterceptor;
import com.backend.service.interceptors.CustomTokenRegisterInterceptor;
import com.backend.service.interceptors.TokensCheckerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
  @Autowired
  private CustomTokenRegisterInterceptor customTokenRegisterInterceptor;
  @Autowired
  private TokensCheckerInterceptor tokensCheckerInterceptor;
  @Autowired
  private AuthorizationInterceptor authorizationInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(customTokenRegisterInterceptor)
        .excludePathPatterns("/swagger-ui.html")
        .addPathPatterns("/auth/oauth/token/register");
    registry.addInterceptor(tokensCheckerInterceptor)
        .excludePathPatterns("/auth/oauth/sign-in", "/auth/oauth/token/register", "/swagger-ui");
    registry.addInterceptor(authorizationInterceptor)
        .excludePathPatterns("/auth/oauth/sign-in", "/auth/oauth/token/register", "/swagger-ui");
//        registry.addInterceptor(new AuthorizationInterceptor())
//                .excludePathPatterns("/users/update", "/auth/oauth/**/*");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
