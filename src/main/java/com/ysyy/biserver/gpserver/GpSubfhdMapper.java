package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.GpSubfhd;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface GpSubfhdMapper {
    @Select("select * from  gp_subfhd limit 10")
    List<GpSubfhd> GetAllList();
}
