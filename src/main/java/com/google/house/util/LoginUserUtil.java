package com.google.house.util;

import com.google.house.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.regex.Pattern;

//这个类是一个工具类：手机，邮箱验证
public class LoginUserUtil {

    // \\d:表示数字格式 {8}:表示8位
    private static final String PHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    // +:表示至少一个指定的内容  \.:表示.
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    //检查输入的手机号和正则表达式中的是否一致
    public static boolean checkTelephone(String target) {
        return PHONE_PATTERN.matcher(target).matches();
    }

    //检查输入的邮箱和正则表达式中的是否一致
    public static boolean checkEmail(String target) {
        return EMAIL_PATTERN.matcher(target).matches();
    }

    //读取一个用户
    private static User load() {
        //SecurityContextHolder.getContext():获取Security的上下文,可以操作全部内容
        Object principal = SecurityContextHolder.getContext().
                //getAuthentication():获得用户是否通过认证、getPrincipal():获得用户全部信息
                getAuthentication().getPrincipal();
        //principal:判断是否得到了一个用户,如果没有通过认证,则无法获得到一个用户
        if (principal instanceof User) {
            return (User) principal;
        }
        return null; //没有获得到用户则返回null
    }

    //读取一个用户中的id
    public static Long getLoginUserId() {
        User user = load();
        if (user == null) {
            return -1L; //此时表示用户不存在
        }
        return user.getId();
    }

}
