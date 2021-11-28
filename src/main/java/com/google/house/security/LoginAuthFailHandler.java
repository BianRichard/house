package com.google.house.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//登录验证失败处理器
public class LoginAuthFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginUrlEntryPoint urlEntryPoint;

    public LoginAuthFailHandler(LoginUrlEntryPoint urlEntryPoint) {
        this.urlEntryPoint = urlEntryPoint;
    }

    @Override
    //onAuthenticationFailure:身份验证失败,根据当前角色的路径,进入正确的登录页面
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = urlEntryPoint.determineUrlToUseForThisRequest(request,response,exception);
        targetUrl += "?" + exception.getMessage();
        System.out.println("targetUrl" + targetUrl);
        super.setDefaultFailureUrl(targetUrl); //设置正确的登录页面
        super.onAuthenticationFailure(request, response, exception); //进入认证流程,是否认证成功
    }

}
