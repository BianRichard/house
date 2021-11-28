package com.google.house.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private Long id;
    private Long userId;
    private String name;

    public Role(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
