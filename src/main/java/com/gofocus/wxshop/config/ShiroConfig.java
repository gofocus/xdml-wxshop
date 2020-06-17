package com.gofocus.wxshop.config;

import com.gofocus.wxshop.service.UserService;
import com.gofocus.wxshop.service.VerificationCodeCheckService;
import com.gofocus.wxshop.shiro.ShiroRealm;
import com.gofocus.wxshop.shiro.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author: GoFocus
 * @Date: 2020-06-14 19:26
 * @Description: Shiro的配置类
 */
@Configuration
public class ShiroConfig implements WebMvcConfigurer {

    @Autowired
    private UserService userService;

/*
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleException(AuthorizationException e, Model model) {
        Map<String, Object> map = new HashMap<String, Object>(100);
        map.put("status", HttpStatus.FORBIDDEN.value());
        map.put("message", "No message available");
        model.addAttribute("errors", map);

        return "error";
    }

    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("joe.coder=password,user\n" +
                "jill.coder=password,admin");

        realm.setRoleDefinitions("admin=read,write\n" +
                "user=read");
        realm.setCachingEnabled(true);
        return realm;
    }
*/

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login.html", "authc");
        chainDefinition.addPathDefinition("/logout", "logout");
        chainDefinition.addPathDefinition("/api/code", "anon");
        chainDefinition.addPathDefinition("/api/login", "anon");
        return chainDefinition;
    }

    @ModelAttribute(name = "subject")
    public Subject subject() {
        return SecurityUtils.getSubject();
    }

/*    @Bean
    public DefaultWebSecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }*/

    @Bean
    public Authorizer authorizer(ShiroRealm shiroRealm) {
        return shiroRealm;
    }

    @Bean
    public ShiroRealm customShiroRealm(VerificationCodeCheckService verificationCodeCheckService) {
        return new ShiroRealm(verificationCodeCheckService);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor());
    }

    @Bean
    public UserLoginInterceptor userLoginInterceptor() {
        return new UserLoginInterceptor(userService);
    }

    private static class UserLoginInterceptor implements HandlerInterceptor {

        private final UserService userService;

        private UserLoginInterceptor(UserService userService) {
            this.userService = userService;
        }


        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
/*            Object userTel = SecurityUtils.getSubject().getPrincipal();
            if (userTel != null) {
                User user = userService.getUserByTel(userTel.toString());
                UserContext.setCurrentUser(user);
            }*/

            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
            UserContext.setCurrentUser(null);
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        }
    }
}
