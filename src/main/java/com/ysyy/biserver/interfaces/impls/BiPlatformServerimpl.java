package com.ysyy.biserver.interfaces.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.common.Validate;
import com.ysyy.biserver.interfaces.BiPlatformServer;
import com.ysyy.biserver.mapper.BiPlatformMapper2;
import com.ysyy.biserver.mysqlmodel.BiPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class BiPlatformServerimpl implements BiPlatformServer {
    @Autowired
    private BiPlatformMapper2 mapper;
    private Validate v=new Validate();
    @Override
    public String getBiPlatformList(String str) {
        JSONObject json=new JSONObject();
        try {
            json= JSON.parseObject(str);
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return "{\n" +
                            "\t\"msg\": \"非法调用\",\n" +
                            "\t\"error_code\": 2002\n" +
                            "}";
                }
            }
            else {
                return "{\n" +
                        "\t\"msg\": \"非法调用\",\n" +
                        "\t\"error_code\": 2002\n" +
                        "}";
            }

            List<BiPlatform> list = mapper.getBiPlatformList();
            json=new JSONObject();
            json.put("error_code",0);
            json.put("data",list);
            return  json.toString();
        }
        catch (Exception e)
        {
            json=new JSONObject();
            json.put("error_code",2004);
            json.put("msg","参数错误");

            return json.toString();
        }
    }

    @Override
    public String AddBiPlatform(String str) {
        JSONObject json=new JSONObject();
        try {
            json = JSON.parseObject(str);
            if (json != null && json.get("userinfo") != null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return "{\n" +
                            "\t\"msg\": \"非法调用\",\n" +
                            "\t\"error_code\": 2002\n" +
                            "}";
                }
            } else {
                return "{\n" +
                        "\t\"msg\": \"非法调用\",\n" +
                        "\t\"error_code\": 2002\n" +
                        "}";
            }

            json=JSON.parseObject(json.get("params").toString());
            BiPlatform model = new BiPlatform();
            model.setName(json.get("name").toString());
            model.setNumber(json.get("number").toString());
            model.setNature(json.get("nature").toString());
            model.isSelf = Integer.parseInt(json.get("is_self").toString());


            int i = mapper.AddBiPlatform(model);
            json=new JSONObject();
            json.put("error_code", 0);
            json.put("msg", "添加成功 " + i + " 条数据");

            return json.toString();
        }
        catch (Exception e)
        {
            json=new JSONObject();
            json.put("error_code",2004);
            json.put("msg","参数错误");

            return json.toString();
        }
    }
}
