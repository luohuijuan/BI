package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbyb2Cdate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResTotalbyb2CdateServer {
    List<ResTotalbyb2Cdate> GetYearTotal(int year);

    List<ResTotalbyb2Cdate> GetYearAndMonthTotal(int year,int month);

    List<ResTotalbyb2Cdate> GetYearAndQuarterTotal(int year, int quarter);


    List<ResTotalbyb2Cdate> GetYearTotalb2c (int year, String b2c_name);


    List<ResTotalbyb2Cdate> GetYearAndMonthTotalb2c( int year,int month,String b2c_name);


    List<ResTotalbyb2Cdate> GetYearAndQuarterTotalb2c( int year,int quarter, String b2c_name);

    List<ResTotalbyb2Cdate> GetYearAndMonthsTotal(int year, List<Integer> months);
}
