package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resymdzb;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResymdzbServer {
    List<Resymdzb> GetYearTotal(int year);

    List<Resymdzb> GetYearAndMonthTotal(int year, int month);

    List<Resymdzb> GetYearAndQuarterTotal(int year, int quarter);
}
