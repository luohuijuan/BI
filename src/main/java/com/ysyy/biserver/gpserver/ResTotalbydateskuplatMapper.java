package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbydateskuplat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResTotalbydateskuplatMapper {
    @Select("select * from  res_totalbydateskuplat  where year=#{year} ")
    List<ResTotalbydateskuplat> GetYearTotalArea(@Param("year") int year);

    @Select("select * from  res_totalbydateskuplat  where year=#{year} and month=#{month}")
    List<ResTotalbydateskuplat> GetYearAndMonthTotalArea(@Param("year") int year, @Param("month") int month);

    @Select("select * from  res_totalbydateskuplat  where year=#{year} and quarter=#{quarter} ")
    List<ResTotalbydateskuplat> GetYearAndQuarterTotalArea(@Param("year") int year, @Param("quarter") int quarter);

}
