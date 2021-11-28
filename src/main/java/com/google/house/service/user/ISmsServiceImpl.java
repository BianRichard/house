package com.google.house.service.user;

import com.google.house.base.ServiceResult;
import com.google.house.service.user.impl.ISmsService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ISmsServiceImpl implements ISmsService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate; //RedisTemplate是SpringData框架(操作不同数据库的框架)中操作Redis数据库的子框架
    private final static String SMS_CODE_CONTENT_PREFIX =  "SMS::CODE::CONTENT"; //分区redis中存在同一个手机号,存在不同功能的,手机号既是登录用的,也是保存验证码的

    private static String generateRandomSmsCode(){ //生成6个随机数方法
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
            sb.append(random.nextInt(10)); //0~9之间的随机数
        return sb.toString();
    }

    @Override
    public ServiceResult<String> sendSms(String telephone) {
        String code = generateRandomSmsCode();//生成6个随机数的方法
        HttpClient client = new HttpClient(); //处理网页
        //以下内容都是第三方发送验证码的网站提供的写法
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn"); //Post方式提交
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        //post链接中拼接参数
        NameValuePair[] data ={
                new NameValuePair("Uid", "bian"),
                new NameValuePair("Key", "d41d8cd98f00b204e980"),
                new NameValuePair("smsMob",telephone),
                new NameValuePair("smsText","验证码:" + code)
        };
        post.setRequestBody(data);

        try {
            client.executeMethod(post);//执行
            String result = new String(post.getResponseBodyAsString().getBytes("gbk"));//接收网页返回的内容
            post.releaseConnection();//关闭连接
            //存入redis,opsForValue:表示操作String类型的命令,RedisTemplate将每个数据类型的命令都进行了归类
            redisTemplate.opsForValue().set("SMS::CODE::CONTENT" + telephone,code,10, TimeUnit.MINUTES);
            return ServiceResult.of(true,"短信发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            return new ServiceResult<>(false,"短信发送失败");
        }
    }

    @Override
    public String getSmsCode(String telephone) {
        return redisTemplate.opsForValue().get(SMS_CODE_CONTENT_PREFIX + telephone); //redisTemplate封装了redis的常用命令,通用命令直接调用,其它的命令分类使用,opsForValue()中是所有String的命令
    }

    @Override
    public void remove(String telephone) {
        redisTemplate.delete(SMS_CODE_CONTENT_PREFIX + telephone);
    }
}
