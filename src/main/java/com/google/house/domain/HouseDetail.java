package com.google.house.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseDetail {
    private Long id;
    private Long houseId;
    private String description;
    private String layoutDesc;
    private String traffic;
    private String roundService;
    private Integer rentWay;
    private String detailAddress;
    private Long subwayLineId;
    private Long subwayStationId;
    private String subwayLineName;
    private String subwayStationName;

    public HouseDetail(Long houseId, String description, String layoutDesc, String traffic, String roundService, Integer rentWay, String detailAddress, Long subwayLineId, Long subwayStationId, String subwayLineName, String subwayStationName) {
        this.houseId = houseId;
        this.description = description;
        this.layoutDesc = layoutDesc;
        this.traffic = traffic;
        this.roundService = roundService;
        this.rentWay = rentWay;
        this.detailAddress = detailAddress;
        this.subwayLineId = subwayLineId;
        this.subwayStationId = subwayStationId;
        this.subwayLineName = subwayLineName;
        this.subwayStationName = subwayStationName;
    }
}
