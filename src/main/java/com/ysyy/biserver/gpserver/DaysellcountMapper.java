package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Daysellcount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface DaysellcountMapper {
    @Select("select * from  daysellcount ")
    List<Daysellcount> GetList();
}
