package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbyo2Odate;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ResTotalbyo2OdateServer {
    List<ResTotalbyo2Odate> GetYearTotal(int year);

    List<ResTotalbyo2Odate> GetYearAndMonthTotal(int year,int month);

    List<ResTotalbyo2Odate> GetYearAndQuarterTotal(int year, int quarter);
}
