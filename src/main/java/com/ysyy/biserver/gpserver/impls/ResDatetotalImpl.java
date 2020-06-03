package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResDatetotal;
import com.ysyy.biserver.gpserver.ResDatetotalMapper;
import com.ysyy.biserver.gpserver.ResDatetotalServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResDatetotalImpl implements ResDatetotalServer {

    @Autowired
    private ResDatetotalMapper mapper;
    @Override
    public List<ResDatetotal> GetYearTotal(int year){

      return mapper.GetYearTotal(year);

    }

    @Override
    public List<ResDatetotal> GetYearAndMonthTotal(int year,int month){

        return mapper.GetYearAndMonthTotal(year,month);

    }

    @Override
    public List<ResDatetotal> GetYearAndQuarterTotal(int year,int quarter){

        return mapper.GetYearAndQuarterTotal(year,quarter);

    }

    @Override
    public List<ResDatetotal> GetYearAndMonthsTotal(int year, List<Integer> months){
        return mapper.GetYearAndMonthsTotal(year,months);
    }

    ;
}
