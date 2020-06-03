package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.Resmtxzb;
import com.ysyy.biserver.gpserver.ResmtxzbMapper;
import com.ysyy.biserver.gpserver.ResmtxzbServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResmtxzbImpl implements ResmtxzbServer {

    @Autowired
    private ResmtxzbMapper mapper;
    @Override
    public List<Resmtxzb> GetYearTotal(int year){

        return mapper.GetYearTotal(year);

    }

    @Override
    public List<Resmtxzb> GetYearAndMonthTotal(int year, int month){

        return mapper.GetYearAndMonthTotal(year,month);

    }

    @Override
    public List<Resmtxzb> GetYearAndQuarterTotal(int year,int quarter){

        return mapper.GetYearAndQuarterTotal(year,quarter);

    }
}
