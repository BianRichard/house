package com.google.house.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousePicture {
    private Long id;
    private Long houseId;
    private String path;
    private String cdnPrefix;
    private Integer width;
    private Integer height;
    private String location;

    public HousePicture(Long houseId, String path, String cdnPrefix, Integer width, Integer height, String location) {
        this.houseId = houseId;
        this.path = path;
        this.cdnPrefix = cdnPrefix;
        this.width = width;
        this.height = height;
        this.location = location;
    }
}
