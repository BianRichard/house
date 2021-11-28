package com.google.house.config;

import com.google.house.security.AuthFilter;
import com.google.house.security.AuthProvider;
import com.google.house.security.LoginAuthFailHandler;
import com.google.house.security.LoginUrlEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //启用Spring security框架
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() { //PasswordEncoder密码加密
        return new BCryptPasswordEncoder(); //使用BCryptPasswordEncoder加密方式
    }

    //将security中的类交给Spring管理
    @Bean
    public LoginUrlEntryPoint urlEntryPoint() {
        return new LoginUrlEntryPoint("/user/login");
    }

    @Bean
    public AuthProvider authProvider() {
        return new AuthProvider();
    }

    @Bean
    public LoginAuthFailHandler authFailHandler() {
        return new LoginAuthFailHandler(urlEntryPoint());
    }

    @Bean
    public AuthFilter authFilter() {
        AuthFilter authFilter = new AuthFilter();
        authFilter.setAuthenticationManager(authenticationManager()); //设置身份验证处理器
        authFilter.setAuthenticationFailureHandler(authFailHandler()); //设置认证失败处理器
        return authFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        AuthenticationManager authenticationManager = null;
        try {
            authenticationManager = super.authenticationManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager;
    }

    //设置一个内存中临时的管理员账号用于测试登录
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").and();
//    }

    @Autowired
    public void configGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider()).eraseCredentials(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
        // 资源访问权限
        http.authorizeRequests()
                //permitAll:允许直接访问,不是敏感信息需要登录等操作
                .antMatchers("/admin/login").permitAll() // 管理员登录入口
                .antMatchers("/static/**").permitAll() // 静态资源
                .antMatchers("/user/login").permitAll() // 用户登录入口
                .antMatchers("/admin/**").hasRole("ADMIN") //hasRole("ADMIN")需要认证
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                //.and()表示并且
                .and()
                //.formLogin() 表示登录配置
                .formLogin()
                .loginProcessingUrl("/login") // 配置角色登录处理入口,security默认的登录入口
                //.failureHandler 登录失败处理器
                .failureHandler(authFailHandler())
                .and()
                .logout() //注销用户
                // .logoutUrl 表示注销的页面
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout/page") //注销后成功后跳转的页面
                .deleteCookies("JSESSIONID") //注销后删除当前用户的session
                .invalidateHttpSession(true) //注销后立即失效
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(urlEntryPoint()) //出现认证异常进行认证
                .accessDeniedPage("/403"); //如果依然认证异常跳转到/403网页
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
    }


}


