package com.google.house.mapper;

import com.google.house.domain.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    int save(House house);

    void updateCover(@Param(value = "id") Long id,
                     @Param(value = "cover") String cover);

    void updateStatus(@Param(value = "id") Long id,
                      @Param(value = "status") int status);

    void updateWatchTimes(@Param(value = "id") Long houseId);

    House findById(Long id);

}
