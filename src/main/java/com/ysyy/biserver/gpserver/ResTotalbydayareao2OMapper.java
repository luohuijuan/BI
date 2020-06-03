package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.ResTotalbydayareao2O;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResTotalbydayareao2OMapper {
    @Select("select * from  res_totalbydayareao2o ")
    List<ResTotalbydayareao2O> GetList();
}
