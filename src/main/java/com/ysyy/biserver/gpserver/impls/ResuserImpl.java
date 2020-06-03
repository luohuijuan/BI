package com.ysyy.biserver.gpserver.impls;



import com.ysyy.biserver.gpmodel.Resuser;
import com.ysyy.biserver.gpserver.ResuserMapper;
import com.ysyy.biserver.gpserver.ResuserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResuserImpl implements ResuserServer {
    @Autowired
    private ResuserMapper mapper;

    @Override
    public List<Resuser> GetList() {

        return mapper.GetList();

    }
}
