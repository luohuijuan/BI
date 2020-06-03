package com.ysyy.biserver.gpserver.impls;

import com.ysyy.biserver.gpmodel.ResTotalbyo2Odate;
import com.ysyy.biserver.gpserver.ResTotalbyo2OdateMapper;
import com.ysyy.biserver.gpserver.ResTotalbyo2OdateServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResTotalbyo2OdateImpl implements ResTotalbyo2OdateServer {

    @Autowired
    private ResTotalbyo2OdateMapper mapper;

    @Override
    public List<ResTotalbyo2Odate> GetYearTotal(int year) {

        return mapper.GetYearTotal(year);

    }

    @Override
    public List<ResTotalbyo2Odate> GetYearAndMonthTotal(int year, int month) {

        return mapper.GetYearAndMonthTotal(year, month);

    }

    @Override
    public List<ResTotalbyo2Odate> GetYearAndQuarterTotal(int year, int quarter) {

        return mapper.GetYearAndQuarterTotal(year, quarter);

    }
}

