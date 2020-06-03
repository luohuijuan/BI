package com.ysyy.biserver.mysqlclass;

import com.ysyy.biserver.mysqlclass.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServer {
    List<User> getUserList();

    String AddUser(User user);

}
