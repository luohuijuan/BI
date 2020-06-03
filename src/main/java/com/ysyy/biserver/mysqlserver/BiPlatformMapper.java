package com.ysyy.biserver.mysqlserver;


import com.ysyy.biserver.mysqlmodel.BiPlatform;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BiPlatformMapper {
    @Select("select * from  bi_platform order by id asc")
    List<BiPlatform> getBiPlatformList();

    @Insert("insert into bi_platform(number,name,nature,is_self,state) values(#{number},#{name},#{nature},#{isSelf},#{state})")
    int AddBiPlatform(BiPlatform model);

    @Update("update bi_platform set number=#{number} ,name=#{name},nature=#{nature} ,is_self=#{isSelf},state=#{state} where id=#{id}")
    boolean UpdateBiPlatform(BiPlatform model);
}
