package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbydateskuplat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResTotalbydateskuplatServer {
    List<ResTotalbydateskuplat> GetYearTotalArea(int year);

    List<ResTotalbydateskuplat> GetYearAndMonthTotalArea(int year, int month);

    List<ResTotalbydateskuplat> GetYearAndQuarterTotalArea(int year, int quarter);
}
