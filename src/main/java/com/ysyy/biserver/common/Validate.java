package com.ysyy.biserver.common;

import com.alibaba.fastjson.JSONObject;

public class Validate {
     public  boolean ValidateUser(JSONObject  userInfo){
         try {

             if(userInfo.get("User_name").equals("qhhh") && userInfo.get("token").equals("sdasdasd1231232131"))
             {
                 return  true;
             }
             else {
                 return  false;
             }
         }
         catch (Exception e){

             return  false;

         }
     }

}
