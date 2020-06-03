package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbydateareab2C;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResTotalbydateareab2CMapper {
    @Select("select * from  res_totalbydateareab2c  where year=#{year} ")
    List<ResTotalbydateareab2C> GetYearTotalArea(@Param("year") int year);

    @Select("select * from  res_totalbydateareab2c  where year=#{year} and month=#{month}")
    List<ResTotalbydateareab2C> GetYearAndMonthTotalArea(@Param("year") int year, @Param("month") int month);

    @Select("select * from  res_totalbydateareab2c  where year=#{year} and quarter=#{quarter} ")
    List<ResTotalbydateareab2C> GetYearAndQuarterTotalArea(@Param("year") int year, @Param("quarter") int quarter);
}
