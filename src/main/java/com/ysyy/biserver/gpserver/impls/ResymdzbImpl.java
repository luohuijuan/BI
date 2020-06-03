package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.Resymdzb;
import com.ysyy.biserver.gpserver.ResymdzbMapper;
import com.ysyy.biserver.gpserver.ResymdzbServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResymdzbImpl implements ResymdzbServer {

    @Autowired
    private ResymdzbMapper mapper;
    @Override
    public List<Resymdzb> GetYearTotal(int year){

        return mapper.GetYearTotal(year);

    }

    @Override
    public List<Resymdzb> GetYearAndMonthTotal(int year, int month){

        return mapper.GetYearAndMonthTotal(year,month);

    }

    @Override
    public List<Resymdzb> GetYearAndQuarterTotal(int year,int quarter){

        return mapper.GetYearAndQuarterTotal(year,quarter);

    }
}
