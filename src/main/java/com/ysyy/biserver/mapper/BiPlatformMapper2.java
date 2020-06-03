package com.ysyy.biserver.mapper;


import com.ysyy.biserver.mysqlmodel.BiPlatform;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BiPlatformMapper2 {
    @Select("select * from  bi_platform order by id asc")
    List<BiPlatform> getBiPlatformList();

    @Insert("insert into bi_platform(number,name,nature,is_self,state) values(#{number},#{name},#{nature},#{isSelf},#{state})")
    int AddBiPlatform(BiPlatform model);
}
