package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resmtxzb;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResmtxzbServer {
    List<Resmtxzb> GetYearTotal(int year);

    List<Resmtxzb> GetYearAndMonthTotal(int year, int month);

    List<Resmtxzb> GetYearAndQuarterTotal(int year, int quarter);
}
