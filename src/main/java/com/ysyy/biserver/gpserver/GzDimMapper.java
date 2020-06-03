package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.GzDim;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GzDimMapper {
    @Select("select * from  gz_dim ")
    List<GzDim> GetList();
}
