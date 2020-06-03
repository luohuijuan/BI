package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResTotalbyb2Cdate;
import com.ysyy.biserver.gpserver.ResTotalbyb2CdateMapper;
import com.ysyy.biserver.gpserver.ResTotalbyb2CdateServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResTotalbyb2CdateImpl implements ResTotalbyb2CdateServer {

    @Autowired
    private ResTotalbyb2CdateMapper mapper;

    @Override
    public List<ResTotalbyb2Cdate> GetYearTotal(int year) {

        return mapper.GetYearTotal(year);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearAndMonthTotal(int year, int month) {

        return mapper.GetYearAndMonthTotal(year, month);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearAndQuarterTotal(int year, int quarter) {

        return mapper.GetYearAndQuarterTotal(year, quarter);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearTotalb2c(int year, String b2c_name) {

        return mapper.GetYearTotalb2c(year,b2c_name);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearAndMonthTotalb2c(int year,int month,String b2c_name) {

        return mapper.GetYearAndMonthTotalb2c(year,month,b2c_name);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearAndQuarterTotalb2c(int year,int quarter, String b2c_name) {

        return mapper.GetYearAndQuarterTotalb2c(year,quarter,b2c_name);

    }

    @Override
    public List<ResTotalbyb2Cdate> GetYearAndMonthsTotal(int year, List<Integer> months){
        return mapper.GetYearAndMonthsTotal(year,months);
    }
}

