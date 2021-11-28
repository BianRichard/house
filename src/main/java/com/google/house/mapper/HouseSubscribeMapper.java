package com.google.house.mapper;

import com.google.house.domain.HouseSubscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseSubscribeMapper {
    HouseSubscribe findByHouseIdAndUserId(@Param(value = "houseId") Long houseId, @Param(value = "loginUserId") Long loginUserId);
    List<HouseSubscribe> findAllByUserIdAndStatus(@Param(value = "userId")Long userId, @Param(value = "status" )int status); //for JPA: Page<HouseSubscribe> findAllByUserIdAndStatus(Long userId, int status, Pageable pageable);
    List<HouseSubscribe> findAllByAdminIdAndStatus(@Param(value = "adminId")Long adminId, @Param(value = "status")int status); //for JPA: Page<HouseSubscribe> findAllByAdminIdAndStatus(Long adminId, int status, Pageable pageable);
    HouseSubscribe findByHouseIdAndAdminId(Long houseId, Long adminId);
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") int status);
}
