package com.ysyy.biserver.control;

import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.gpserver.impls.GpSubfhdServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
public class GpSubfhdControler {
    @Autowired
    private GpSubfhdServerImpl server;
    @PostMapping("/getgpsubfhdlist")
    public String GetGpsubfhdList(@RequestBody Map params){
        JSONObject jsonObj=new JSONObject(params);
        return server.GetAllList(jsonObj.toString());

    }
}
