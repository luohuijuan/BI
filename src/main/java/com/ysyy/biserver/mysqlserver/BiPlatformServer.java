package com.ysyy.biserver.mysqlserver;

import org.springframework.stereotype.Service;

@Service
public interface BiPlatformServer {
    //获取列表
    String getBiPlatformList(String str);

    String AddBiPlatform(String str);

    String UpdateBiplatform(String str);
}
