package com.google.house.service.user.impl;

import com.google.house.base.ServiceResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface ISmsService {

    //发送验证码,验证码和手机号码绑定在一起,之后进入Redis
    ServiceResult<String>sendSms(String telephone);

    //获取Redis缓存中的验证码
    String getSmsCode(String telephone);

    //强制让验证码被删除,从Redis中删除
    void remove(String telephone);


}
