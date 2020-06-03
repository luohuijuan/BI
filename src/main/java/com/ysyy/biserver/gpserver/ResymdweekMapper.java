package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resymdweek;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResymdweekMapper {
    @Select("select * from  resymdweek ")
    List<Resymdweek> GetList();
}
