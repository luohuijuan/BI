package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbyb2Cdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResTotalbyb2CdateMapper {
    @Select("select * from  res_totalbyb2cdate  where year=#{0}")
    List<ResTotalbyb2Cdate> GetYearTotal(int year);

    @Select("select * from  res_totalbyb2cdate  where year=#{year} and month=#{month}")
    List<ResTotalbyb2Cdate> GetYearAndMonthTotal(@Param("year") int year, @Param("month") int month);

    @Select("select * from  res_totalbyb2cdate  where year=#{year} and quarter=#{quarter}")
    List<ResTotalbyb2Cdate> GetYearAndQuarterTotal(@Param("year") int year, @Param("quarter") int quarter);

    @Select("select * from  res_totalbyb2cdate  where year=#{year} and b2c_name=#{b2c_name}")
    List<ResTotalbyb2Cdate> GetYearTotalb2c(@Param("year") int year,@Param("b2c_name") String b2c_name);

    @Select("select * from  res_totalbyb2cdate  where year=#{year} and month=#{month} and b2c_name=#{b2c_name}")
    List<ResTotalbyb2Cdate> GetYearAndMonthTotalb2c(@Param("year") int yea, @Param("month") int monthr,@Param("b2c_name") String b2c_name);

    @Select("select * from  res_totalbyb2cdate  where year=#{year} and month=#{quarter} and b2c_name=#{b2c_name}")
    List<ResTotalbyb2Cdate> GetYearAndQuarterTotalb2c(@Param("year") int yea, @Param("quarter") int monthr,@Param("b2c_name") String b2c_name);

    /**
     * 获取多个月
     * @param months
     * @return
     */
    @Select({
            "<script>",
            "select * from res_totalbyb2cdate ",
            "where year=#{year} and month in ",
            "<foreach collection='months' item='month' open='(' separator=',' close=')'>",
            " #{month} ",
            "</foreach>",
            "</script>"
    })
    List<ResTotalbyb2Cdate> GetYearAndMonthsTotal(@Param("year") int year, @Param("months") List<Integer> months);
}
