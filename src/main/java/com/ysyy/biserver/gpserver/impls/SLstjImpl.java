package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.SLstj;
import com.ysyy.biserver.gpserver.SLstjMapper;
import com.ysyy.biserver.gpserver.SLstjServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SLstjImpl implements SLstjServer {

    @Autowired
    private SLstjMapper mapper;
    @Override
    public List<SLstj> GetYearTotal(int year){

        return mapper.GetYearTotal(year);

    }

    @Override
    public List<SLstj> GetYearAndMonthTotal(int year, int month){

        return mapper.GetYearAndMonthTotal(year,month);

    }

    @Override
    public List<SLstj> GetYearAndQuarterTotal(int year,int quarter){

        return mapper.GetYearAndQuarterTotal(year,quarter);

    }
}
