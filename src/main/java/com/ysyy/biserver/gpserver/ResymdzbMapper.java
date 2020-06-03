package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resymdzb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResymdzbMapper {
    @Select("select * from  resymdzb  where year=#{year}")
    List<Resymdzb> GetYearTotal(@Param("year") int year);

    @Select("select * from  resymdzb  where year=#{year} and month=#{month}")
    List<Resymdzb> GetYearAndMonthTotal(@Param("year") int year, @Param("month") int month);

    @Select("select * from  resymdzb  where year=#{year} and quarter=#{quarter}")
    List<Resymdzb> GetYearAndQuarterTotal(@Param("year") int year, @Param("quarter") int quarter);
}
