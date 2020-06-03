package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbydateareab2C;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResTotalbydateareab2CServer {
    List<ResTotalbydateareab2C> GetYearTotalArea(int year);

    List<ResTotalbydateareab2C> GetYearAndMonthTotalArea(int year, int month);

    List<ResTotalbydateareab2C> GetYearAndQuarterTotalArea(int year, int quarter);
}
