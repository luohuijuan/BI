package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.SLstj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface SLstjMapper {
    @Select("select * from  s_lstj  where year=#{year}")
    List<SLstj> GetYearTotal(@Param("year") int year);

    @Select("select * from  s_lstj  where year=#{year} and month=#{month}")
    List<SLstj> GetYearAndMonthTotal(@Param("year") int year, @Param("month") int month);

    @Select("select * from  s_lstj  where year=#{year} and quarter=#{quarter}")
    List<SLstj> GetYearAndQuarterTotal(@Param("year") int year, @Param("quarter") int quarter);
}
