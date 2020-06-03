package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResDatetotal;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ResDatetotalServer {
    List<ResDatetotal> GetYearTotal(int year);

    List<ResDatetotal> GetYearAndMonthTotal(int year,int month);

    List<ResDatetotal> GetYearAndQuarterTotal(int year, int quarter);

    List<ResDatetotal> GetYearAndMonthsTotal(int year, List<Integer> months);
}
