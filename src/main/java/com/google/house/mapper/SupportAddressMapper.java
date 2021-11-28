package com.google.house.mapper;


import com.google.house.domain.SupportAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SupportAddressMapper {
    //通过某一个行政级别(city/region),查询所有同级的行政区
    List<SupportAddress> findAllByLevel(String level);
    //通过某一个行政级别和它下属的行政区名称,查询当前的行政区
    SupportAddress findByEnNameAndLevel(@Param("enName") String enName, @Param("level") String level);
    //通过某一个行政级别名称和描述,确定是哪个地方的行政区
    SupportAddress findByEnNameAndBelongTo(@Param("enName") String enName, @Param("belongTo") String belongTo);
    //通过某一个行政级别和描述,返回当前行政区下属的所有行政区
    List<SupportAddress> findAllByLevelAndBelongTo(@Param(value = "level") String level, @Param(value = "belongTo") String belongTo);

    //错误的
    //通过某一个行政区的英文名称,获得当前行政区的英文名称
    //SupportAddress findByEnName(String enName);
    //通过某一个行政区的英文名称和上级行政区的名称,获得当前行政区下属的所有行政区
    //List<SupportAddress> findAllByEnNameAndBelongTo(@Param(value = "enName") String enName, @Param(value = "belongTo") String belongTo);
}
