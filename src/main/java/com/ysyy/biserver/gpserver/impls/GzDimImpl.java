package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.GzDim;
import com.ysyy.biserver.gpserver.GzDimMapper;
import com.ysyy.biserver.gpserver.GzDimServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GzDimImpl implements GzDimServer {
    @Autowired
    private GzDimMapper mapper;

    @Override
    public List<GzDim> GetList(){

        return mapper.GetList();

    }

}
