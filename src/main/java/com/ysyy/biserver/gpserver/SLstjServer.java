package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.SLstj;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SLstjServer {
    List<SLstj> GetYearTotal(int year);

    List<SLstj> GetYearAndMonthTotal(int year, int month);

    List<SLstj> GetYearAndQuarterTotal(int year, int quarter);
}
