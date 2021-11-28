package com.google.house.mapper;


import com.google.house.domain.SubwayStation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubwayStationMapper {
    List<SubwayStation> findAllBySubwayId(Long subwayId);
    SubwayStation findBySubwayId(Long subwayId);
}
