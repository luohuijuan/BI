package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResTotalbydateareab2C;
import com.ysyy.biserver.gpserver.ResTotalbydateareab2CMapper;
import com.ysyy.biserver.gpserver.ResTotalbydateareab2CServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResTotalbydateareab2CImpl implements ResTotalbydateareab2CServer {
    @Autowired
    private ResTotalbydateareab2CMapper mapper;

    @Override
    public List<ResTotalbydateareab2C> GetYearTotalArea(int year){

        return mapper.GetYearTotalArea( year);
    }

    @Override
    public List<ResTotalbydateareab2C> GetYearAndMonthTotalArea(int year, int month){
        return mapper.GetYearAndMonthTotalArea(  year,  month);

    }

    @Override
    public List<ResTotalbydateareab2C> GetYearAndQuarterTotalArea(int year, int quarter){
        return mapper.GetYearAndQuarterTotalArea(  year,  quarter);

    }
}
