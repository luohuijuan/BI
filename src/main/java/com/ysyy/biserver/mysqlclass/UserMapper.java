package com.ysyy.biserver.mysqlclass;


import com.ysyy.biserver.mysqlclass.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {

    @Select("select * from usertable order by user_id desc")
    List<User> getUserList();


    @Insert("insert into usertable(username,userpassword,age,sex) values(#{username},#{userpassword},#{age},#{sex})")
    int AddUser(User user);
}
