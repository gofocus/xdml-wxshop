package com.gofocus.wxshop.config;

import com.gofocus.wxshop.service.UserService;
import com.gofocus.wxshop.service.VerificationCodeCheckService;
import com.gofocus.wxshop.shiro.ShiroRealm;
import com.gofocus.wxshop.shiro.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @Author: GoFocus
 * @Date: 2020-06-14 19:26
 * @Description: Shiro的配置类
 */
@Configuration
public class ShiroConfig implements WebMvcConfigurer {

/*    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/api/logout", "anon");
        chainDefinition.addPathDefinition("/api/code", "anon");
        chainDefinition.addPathDefinition("/api/login", "anon");
        chainDefinition.addPathDefinition("/api/status", "anon");
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }*/

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroLoginFilter shiroLoginFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //todo new HashMap()初始容量设置为100，请求就返回401，这是为什么？初始容量大于150或者不设置初始容量，请求正常返回200
        Map<String, String> patterns = new HashMap<>(100);
        patterns.put("/api/logout", "anon");
        patterns.put("/api/code", "anon");
        patterns.put("/api/login", "anon");
        patterns.put("/api/status", "anon");
        patterns.put("/**", "authc");

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("shiroLoginFilter", shiroLoginFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(patterns);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm);
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        securityManager.setSessionManager(new DefaultWebSessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }


    @Bean
    public ShiroRealm myShiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new ShiroRealm(verificationCodeCheckService);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                UserContext.remove();
            }
        });
    }

/*    @ModelAttribute(name = "subject")
    public Subject subject() {
        return SecurityUtils.getSubject();
    }*/


/*
    @Bean
    public Authorizer authorizer(ShiroRealm shiroRealm) {
        return shiroRealm;
    }*/
}
