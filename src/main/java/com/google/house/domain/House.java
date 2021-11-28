package com.google.house.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {
    private Long id;
    private String title;
    private Long adminId;
    private Integer price;
    private Integer area;
    private Integer room;
    private Integer parlour;
    private Integer bathroom;
    private Integer floor;
    private Integer totalFloor;
    private Integer watchTimes;
    private Integer buildYear;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private String cityEnName;
    private String regionEnName;
    private String street;
    private String district;
    private Integer direction;
    private String cover;
    private Integer distanceToSubway;

    public House(String title, Long adminId, Integer price, Integer area, Integer room, Integer parlour, Integer bathroom, Integer floor, Integer totalFloor, Integer watchTimes, Integer buildYear, Integer status, LocalDateTime createTime, LocalDateTime lastUpdateTime, String cityEnName, String regionEnName, String street, String district, Integer direction, String cover, Integer distanceToSubway) {
        this.title = title;
        this.adminId = adminId;
        this.price = price;
        this.area = area;
        this.room = room;
        this.parlour = parlour;
        this.bathroom = bathroom;
        this.floor = floor;
        this.totalFloor = totalFloor;
        this.watchTimes = watchTimes;
        this.buildYear = buildYear;
        this.status = status;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
        this.cityEnName = cityEnName;
        this.regionEnName = regionEnName;
        this.street = street;
        this.district = district;
        this.direction = direction;
        this.cover = cover;
        this.distanceToSubway = distanceToSubway;
    }
}
