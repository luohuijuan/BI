package com.ysyy.biserver.gpserver.impls;


import com.ysyy.biserver.gpmodel.Resymdweek;
import com.ysyy.biserver.gpserver.ResymdweekMapper;
import com.ysyy.biserver.gpserver.ResymdweekServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResymdweekImpl implements ResymdweekServer {
    @Autowired
    private ResymdweekMapper mapper;

    @Override
    public List<Resymdweek> GetList() {

        return mapper.GetList();

    }
}
