package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbydateareao2O;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ResTotalbydateareao2OServer {

    List<ResTotalbydateareao2O> GetYearTotalArea(int year);

    List<ResTotalbydateareao2O> GetYearAndMonthTotalArea(int year, int month);

    List<ResTotalbydateareao2O> GetYearAndQuarterTotalArea(int year, int quarter);

    List<ResTotalbydateareao2O> GetYearAndMonthsTotal(int year, List<Integer> months);


}
