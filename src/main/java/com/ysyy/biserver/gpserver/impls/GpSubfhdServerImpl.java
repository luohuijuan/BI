package com.ysyy.biserver.gpserver.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.common.Validate;
import com.ysyy.biserver.gpmodel.GpSubfhd;
import com.ysyy.biserver.gpserver.GpSubfhdMapper;
import com.ysyy.biserver.gpserver.GpSubfhdServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GpSubfhdServerImpl implements GpSubfhdServer {
    @Autowired
    private GpSubfhdMapper mapper;
    private Validate v=new Validate();
    @Override
    public String GetAllList(String str) {
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

            List<GpSubfhd> list = mapper.GetAllList();
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

}
