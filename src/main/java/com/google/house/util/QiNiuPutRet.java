package com.google.house.util;

import lombok.Data;

@Data
//获得网页上的内容
public class QiNiuPutRet {
    public String key;
    public String hash;
    public String bucket;
    public int width;
    public int height;
}
