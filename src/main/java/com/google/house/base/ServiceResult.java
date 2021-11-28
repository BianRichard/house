package com.google.house.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceResult<T> {

    private boolean success; //一般情况下service层返回某个操作是否成功,也可以和ApiResponse保持一致
    private String message;
    private T result;

    public ServiceResult(boolean success) {
        this.success = success;
    }

    public ServiceResult(String message) {
        this.message = message;
    }

    public ServiceResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ServiceResult(String message, T result) {
        this.message = message;
        this.result = result;
    }

    public static <T>ServiceResult<T> notFound(){
        return new ServiceResult<>(false,Message.NOT_FOUND.getValue());
    }

    public static <T>ServiceResult <T> success(){
        return new ServiceResult<>(true,Message.LOGIN_SUCCESS.getValue());
    }

    public static <T>ServiceResult <T> success(String message,T result){
        return new ServiceResult<>(message,result);
    }

    //避免创建对象
    public static <T> ServiceResult<T> of(T code){
        ServiceResult<T> sr = new ServiceResult<>();
        sr.setResult(code);
        return sr;
    }

    public static  <T> ServiceResult<T> of(boolean success, String message){
        ServiceResult<T> sr = new ServiceResult<>();
        sr.setSuccess(success);
        sr.setMessage(message);
        return sr;
    }

    public enum Message{

        NOT_FOUND("Not Found Resource!"),
        NOT_LOGIN("User not Login!"),
        LOGIN_SUCCESS("User Login Success");

        private final String value;

        Message(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
