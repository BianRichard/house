package com.google.house.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

//认证入口点(异常处理器),在用户请求处理过程中遇到认证异常的时候,被ExceptionTranslationFilter捕获
//ExceptionTranslationFilter使用一个AuthenticationEntryPoint(接口)用于开启表单特定认证方案的认证流程
//AuthenticationEntryPoint缺省实现就是这里的LoginUrlAuthenticationEntryPoint:认证错误,引导二次认证
//这个请求提交给UsernamePasswordAuthenticationEntryPoint负责处理
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final String API_FREFIX = "/api"; //表示house中敏感的操作或高权限的操作
    private static final String API_CODE_403 = "{\"code\": 403}";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8"; //返回json格式的数据
    private final PathMatcher pathMatcher = new AntPathMatcher(); //路径匹配器的缺省实现,可以匹配ant风格的路径(精简的匹配模式,用于URL路径和目录的匹配)
    private final Map<String, String> authEntryPointMap; //认证入口点集合

    public LoginUrlEntryPoint(String loginFormUrl) {
        //这个参数(登录页面的url)指向表单登录页面的位置,可以基于这个属性构建一个登录页面重定向的URL
        super(loginFormUrl);
        authEntryPointMap = new HashMap<>();
        authEntryPointMap.put("/user/**", "/user/login"); //匹配任意层级的路径或目录,此处就是/user路径下所有的内容
        authEntryPointMap.put("/admin/**", "/admin/login");
    }


    /**
     * 如果是Api接口 返回json数据 否则按照一般流程处理
     */
    @Override
    //request是遇到了认证异常的用户请求,response是返回给用户的响应,我们设置这个方法主要是为了引导用户进入正确的网址
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        //如果用户遇到需要认证的超链接以/api开头,那么这一定是一个非法访问,我们就将响应设置为403
        //响应的类型被我们设置为了JSON格式的返回
        if (uri.startsWith(API_FREFIX)) { //检测字符串是否以指定的前缀开始
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(CONTENT_TYPE);

            PrintWriter pw = response.getWriter();
            pw.write(API_CODE_403);
            pw.close();
        } else {
            super.commence(request, response, authException); //按照一般流程处理,开始登录认证流程,重定向或者forward到页面的url,再次认证
        }

    }

    /**
     * 根据请求跳转到指定的页面，父类是默认使用loginFormUrl
     */
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String uri = request.getRequestURI().replace(request.getContextPath(), ""); //uri获取工程根路径到地址结尾(获取端口号之后的路径),url获取请求的地址链接(获取全路径 获取的是请求路径中的ip和端口号),replace()用新字符串replacement替换所有的 旧字符串target
        System.out.println("uri: " + request.getRequestURI() + " : " + request.getContextPath()); //getContextPath获得工程的根路径(端口号之后的路径和工程名称)

        //Map.Entry接口:表示一个映射项,包含获取key和获取value的方法
        //entrySet()的返回值也是返回一个Set集合,此集合的类型为Map.Entry
        for (Map.Entry<String, String> authEntry : this.authEntryPointMap.entrySet()) {
            if (this.pathMatcher.match(authEntry.getKey(), uri)) {
                return authEntry.getValue();
            }
        }
        return super.determineUrlToUseForThisRequest(request, response, exception); //确定登录页面的url,如果都不匹配,则返回getLoginFormUrl
    }

}
