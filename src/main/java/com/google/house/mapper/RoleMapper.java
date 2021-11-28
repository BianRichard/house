package com.google.house.mapper;

import com.google.house.domain.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    int save(Role role);
    List<Role> findRolesByUserId(Long userId);
}
