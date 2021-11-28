package com.google.house.mapper;

import java.util.List;

import com.google.house.domain.HousePicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HousePictureMapper {
    List<HousePicture> findAllByHouseId(Long id);
    int save(@Param("housePictures") List<HousePicture> housePictures);

    HousePicture findById(Long id);
    int deleteById(Long id);
}
