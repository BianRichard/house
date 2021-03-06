package com.google.house.mapper;

import java.util.List;

import com.google.house.domain.Subway;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubwayMapper {
    List<Subway> findAllByCityEnName(String cityEnName);
    Subway findByCityEnName(Long subwayId);
}
