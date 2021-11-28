package com.google.house.mapper;

import java.util.List;

import com.google.house.domain.HouseDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HouseDetailMapper {
    HouseDetail findByHouseId(Long houseId);
    List<HouseDetail> findAllByHouseIdIn(List<Long> houseIds);
    void save(HouseDetail houseDetail);
}
