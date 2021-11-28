package com.google.house.security;

import com.google.house.domain.User;
import com.google.house.service.user.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

//当Spring security默认提供的实现类不能满足需求的时候可以实现AuthenticationProvider(认证供应)
//当前类是自定义的身份验证方式,账号和密码登录,是AuthFilter中具体认证的详细内容
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    //认证密码attemptAuthentication
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName();//获取网上的用户名
        String inputPassword = (String)authentication.getCredentials(); //获取网上的密码

        User user = userService.findUserByName(userName);
        if(user == null)
            throw new AuthenticationCredentialsNotFoundException("authError"); //用户名不正确

        //用数据库中的密文和网页生成的铭文匹配是否一致
        //第一个参数是前台传过来的,第二个参数是数据库的密文
        if (passwordEncoder.matches(inputPassword,user.getPassword()))
            return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

        throw new BadCredentialsException("authError"); //密码不正确

    }

    @Override
    //当上边的验证方式不能满足需要,是否寻找其它实现了AuthenticationProvider的验证类
    //返回true表示继续执行AuthFilter中attemptAuthentication中验证手机的流程
    //返回false表示,用户名和密码登录失败,不再执行AuthFilter中attemptAuthentication中验证手机的流程
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
