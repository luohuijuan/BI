package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbyo2Odate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


    @Mapper
    @Repository
    public interface ResTotalbyo2OdateMapper {
        @Select("select * from  res_totalbyo2odate  where year=#{0}")
        List<ResTotalbyo2Odate> GetYearTotal(int year);

        @Select("select * from  res_totalbyo2odate  where year=#{year} and month=#{month}")
        List<ResTotalbyo2Odate> GetYearAndMonthTotal(@Param("year") int year, @Param("month") int month);

        @Select("select * from  res_totalbyo2odate  where year=#{year} and quarter=#{quarter}")
        List<ResTotalbyo2Odate> GetYearAndQuarterTotal(@Param("year") int year, @Param("quarter") int quarter);
    }

