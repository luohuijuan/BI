package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResTotalbydayareao2O;
import com.ysyy.biserver.gpserver.ResTotalbydayareao2OMapper;
import com.ysyy.biserver.gpserver.ResTotalbydayareao2OServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ResTotalbydayareao2OImpl implements ResTotalbydayareao2OServer {
    @Autowired
    private ResTotalbydayareao2OMapper mapper;

    @Override
    public List<ResTotalbydayareao2O> GetList() {

        return mapper.GetList();

    }
}
