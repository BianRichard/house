package com.google.house.controller;

import com.google.house.base.ApiResponse;
import com.google.house.base.ServiceResult;
import com.google.house.service.user.impl.ISmsService;
import com.google.house.util.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private ISmsService iSmsService;

    @GetMapping(value = {"/","/index"})
    public String index(Model model){
        return "index";
    }

    @GetMapping("/404")
    public String notFoundPage(){
        return "404";
    }

    @GetMapping("/403")
    public String accessError(){
        return "403";
    }

    @GetMapping("/500")
    public String logoutPage(){
        return "logout";
    }

    @GetMapping(value = "sms/code")
    @ResponseBody //将对象转成json格式的数据,使用ResponseBody不会再走视图处理器,而是直接将数据写入到输入流中
    public ApiResponse smsCode(@RequestParam("telephone") String telephone){

        if (!LoginUserUtil.checkTelephone(telephone))
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), "请输入正确的手机号");

        ServiceResult<String> result = iSmsService.sendSms(telephone);
        if (result.isSuccess())
            return ApiResponse.ofSuccess("");
        else
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());

    }


}
