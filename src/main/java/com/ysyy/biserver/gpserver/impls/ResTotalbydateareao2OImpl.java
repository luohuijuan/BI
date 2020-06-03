package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResTotalbydateareao2O;
import com.ysyy.biserver.gpserver.ResTotalbydateareao2OMapper;
import com.ysyy.biserver.gpserver.ResTotalbydateareao2OServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResTotalbydateareao2OImpl implements ResTotalbydateareao2OServer {
    @Autowired
    private ResTotalbydateareao2OMapper mapper;

    @Override
    public List<ResTotalbydateareao2O> GetYearTotalArea(int year){

        return mapper.GetYearTotalArea( year);
    }

    @Override
    public List<ResTotalbydateareao2O> GetYearAndMonthTotalArea(int year, int month){
        return mapper.GetYearAndMonthTotalArea(  year,  month);

    }

    @Override
    public List<ResTotalbydateareao2O> GetYearAndQuarterTotalArea(int year, int quarter){
        return mapper.GetYearAndQuarterTotalArea(  year,  quarter);

    }


    @Override
    public List<ResTotalbydateareao2O> GetYearAndMonthsTotal(int year, List<Integer> months){
        return mapper.GetYearAndMonthsTotal(year,months);
    }

}
