package com.ysyy.biserver.gpserver;



import com.ysyy.biserver.gpmodel.ResDatetotal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResDatetotalMapper {
    @Select("select * from  res_datetotal  where year=#{0}")
    List<ResDatetotal> GetYearTotal(int year);

    @Select("select * from  res_datetotal  where year=#{year} and month=#{month}")
    List<ResDatetotal> GetYearAndMonthTotal(@Param("year") int year,@Param("month") int month);

    @Select("select * from  res_datetotal  where year=#{year} and quarter=#{quarter}")
    List<ResDatetotal> GetYearAndQuarterTotal(@Param("year") int year,@Param("quarter") int quarter);


    /**
     * 获取多个月
     * @param months
     * @return
     */
    @Select({
            "<script>",
            "select * from res_datetotal ",
            "where year=#{year} and month in ",
            "<foreach collection='months' item='month' open='(' separator=',' close=')'>",
            " #{month} ",
            "</foreach>",
            "</script>"
    })
    List<ResDatetotal> GetYearAndMonthsTotal(@Param("year") int year,@Param("months") List<Integer> months);
}
