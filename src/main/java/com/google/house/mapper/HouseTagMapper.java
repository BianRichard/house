package com.google.house.mapper;

import com.google.house.domain.HouseTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseTagMapper {
    HouseTag findByNameAndHouseId(@Param("name") String name, @Param("houseId") Long houseId);
    List<HouseTag> findAllByHouseId(Long id);
    List<HouseTag> findAllByHouseIdIn(List<Long> houseIds);
    int save(List<HouseTag> houseTags);
    int deleteById(Long id); //删除一个标签
}
