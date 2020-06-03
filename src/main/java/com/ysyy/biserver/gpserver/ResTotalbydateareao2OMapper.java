package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbydateareao2O;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResTotalbydateareao2OMapper {
    @Select("select * from  res_totalbydateareao2o  where year=#{year} ")
    List<ResTotalbydateareao2O> GetYearTotalArea(@Param("year") int year);

    @Select("select * from  res_totalbydateareao2o  where year=#{year} and month=#{month}")
    List<ResTotalbydateareao2O> GetYearAndMonthTotalArea(@Param("year") int year, @Param("month") int month);

    @Select("select * from  res_totalbydateareao2o  where year=#{year} and quarter=#{quarter} ")
    List<ResTotalbydateareao2O> GetYearAndQuarterTotalArea(@Param("year") int year, @Param("quarter") int quarter);

    /**
     * 获取多个月
     * @param months
     * @return
     */
    @Select({
            "<script>",
            "select * from res_totalbydateareao2o ",
            "where year=#{year} and month in ",
            "<foreach collection='months' item='month' open='(' separator=',' close=')'>",
            " #{month} ",
            "</foreach>",
            "</script>"
    })
    List<ResTotalbydateareao2O> GetYearAndMonthsTotal(int year, List<Integer> months);


}