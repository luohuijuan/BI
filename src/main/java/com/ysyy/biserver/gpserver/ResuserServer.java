package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resuser;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ResuserServer {
    List<Resuser> GetList();
}

