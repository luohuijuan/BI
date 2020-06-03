package com.ysyy.biserver.interfaces;

import com.ysyy.biserver.mysqlmodel.BiPlatform;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BiPlatformServer {
    //获取列表
    String getBiPlatformList(String str);

    String AddBiPlatform(String str);
}
