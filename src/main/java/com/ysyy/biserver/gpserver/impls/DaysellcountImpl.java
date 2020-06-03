package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.Daysellcount;
import com.ysyy.biserver.gpserver.DaysellcountMapper;
import com.ysyy.biserver.gpserver.DaysellcountServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class DaysellcountImpl implements DaysellcountServer {
    @Autowired
    private DaysellcountMapper mapper;

    @Override
    public List<Daysellcount> GetList() {

        return mapper.GetList();

    }
}
