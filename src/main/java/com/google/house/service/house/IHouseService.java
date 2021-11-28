package com.google.house.service.house;

import com.google.house.base.ServiceMultiResult;
import com.google.house.base.ServiceResult;
import com.google.house.dto.HouseDTO;
import com.google.house.dto.HouseSubscribeDTO;
import com.google.house.form.DatatableSearch;
import com.google.house.form.HouseForm;
import com.google.house.form.MapSearch;
import com.google.house.form.RentSearch;

import java.util.Date;
public interface IHouseService {

    ServiceResult<HouseDTO> save(HouseForm houseForm);

    ServiceResult update(HouseForm houseForm);

    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

    ServiceResult<HouseDTO> findCompleteOne(Long id);

    ServiceResult removePhoto(Long id);

    ServiceResult updateCover(Long coverId, Long targetId);

    ServiceResult addTag(Long houseId, String tag);

    ServiceResult removeTag(Long houseId, String tag);

    ServiceResult updateStatus(Long id, int status);

    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);
}