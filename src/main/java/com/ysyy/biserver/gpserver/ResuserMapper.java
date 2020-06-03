package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resuser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ResuserMapper {
    @Select("select * from  resuser ")
    List<Resuser> GetList();
}
