package com.google.house.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//当service返回多个结果
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMultiResult<T> {

    private long total;
    private List<T> result;

    public static <T>ServiceMultiResult<T> of(List<T> result){
        ServiceMultiResult<T> serviceResult = new ServiceMultiResult<>();
        serviceResult.setResult(result);
        return serviceResult;
    }


    public int getResultSize() {
        if (this.result == null)
            return 0;
        return this.result.size();
    }



}
