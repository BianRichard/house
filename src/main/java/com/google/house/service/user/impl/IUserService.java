package com.google.house.service.user.impl;

import com.google.house.base.ServiceResult;
import com.google.house.domain.User;
import com.google.house.dto.UserDTO;

public interface IUserService {
    User findUserByName(String userName);
    User findUserByTelephone(String telephone);
    ServiceResult<UserDTO> findById(Long userId);
    User addUserByPhone(String telephone); //根据手机号码添加用户
    ServiceResult modifyUserProfile(String profile,String value); //用户的修改操作
}
