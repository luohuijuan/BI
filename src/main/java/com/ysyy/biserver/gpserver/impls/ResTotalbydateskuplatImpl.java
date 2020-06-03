package com.ysyy.biserver.gpserver.impls;


import com.ysyy.biserver.gpmodel.ResTotalbydateskuplat;
import com.ysyy.biserver.gpserver.ResTotalbydateskuplatMapper;
import com.ysyy.biserver.gpserver.ResTotalbydateskuplatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResTotalbydateskuplatImpl implements ResTotalbydateskuplatServer {

    @Autowired
    private ResTotalbydateskuplatMapper mapper;

    @Override
    public List<ResTotalbydateskuplat> GetYearTotalArea(int year){

        return mapper.GetYearTotalArea( year);
    }

    @Override
    public List<ResTotalbydateskuplat> GetYearAndMonthTotalArea(int year, int month){
        return mapper.GetYearAndMonthTotalArea(  year,  month);

    }

    @Override
    public List<ResTotalbydateskuplat> GetYearAndQuarterTotalArea(int year, int quarter){
        return mapper.GetYearAndQuarterTotalArea(  year,  quarter);

    }
}
