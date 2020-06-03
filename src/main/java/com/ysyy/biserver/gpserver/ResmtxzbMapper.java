package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resmtxzb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface ResmtxzbMapper {
    @Select("select * from  resmtxzb  where year=#{year}")
    List<Resmtxzb> GetYearTotal(@Param("year") int year);

    @Select("select * from  resmtxzb  where year=#{year} and month=#{month}")
    List<Resmtxzb> GetYearAndMonthTotal(@Param("year") int year, @Param("month") int month);

    @Select("select * from  resmtxzb  where year=#{year} and quarter=#{quarter}")
    List<Resmtxzb> GetYearAndQuarterTotal(@Param("year") int year, @Param("quarter") int quarter);
}
