package com.ysyy.biserver.control;

import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.mysqlserver.impls.BiPlatformServerimpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
public class BiPlatformControler {

    @Autowired
    private BiPlatformServerimpl2 server;

    @PostMapping("/getbiplatformlist")
    public String getBiPlatformList(@RequestBody Map params){
        JSONObject jsonObj=new JSONObject(params);
        return server.getBiPlatformList(jsonObj.toString());

    }

    @PostMapping("/addbiplatform")
    public String AddBiPlatform(@RequestBody Map params){
        JSONObject jsonObj=new JSONObject(params);
        return server.AddBiPlatform(jsonObj.toString());
    }

    @PostMapping("/updatebiplatform")
    public String UpdateBiplatform(@RequestBody Map params){
        JSONObject jsonObj=new JSONObject(params);
        return server.UpdateBiplatform(jsonObj.toString());
    }

}
