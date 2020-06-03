package com.ysyy.biserver.mysqlclass.serverimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.mysqlclass.UserMapper;
import com.ysyy.biserver.mysqlclass.UserServer;
import com.ysyy.biserver.mysqlclass.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserServerimpl implements UserServer {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> getUserList() {

        try {
            List<User> users = userMapper.getUserList();

            return  users;
        }
        catch (Exception e)
        {
            throw e;
//            return null;
        }
    }

    @Override
    public String AddUser(User user) {
        JSONObject json=new JSONObject();
        try {

            int i = userMapper.AddUser(user);
            json.put("error_code",0);
            json.put("msg","添加成功 "+ i +" 条数据");
            System.out.println(json.toString());
            return json.toString();
        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
