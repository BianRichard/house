package com.google.house.service.user;

import com.google.common.collect.Lists;
import com.google.house.base.ServiceResult;
import com.google.house.domain.Role;
import com.google.house.domain.User;
import com.google.house.dto.UserDTO;
import com.google.house.mapper.RoleMapper;
import com.google.house.mapper.UserMapper;
import com.google.house.service.user.impl.IUserService;
import com.google.house.util.LoginUserUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //查询用户权限信息
    private User rolesShow(User user) {
        //List:某个用户可能是admin、user
        List<Role> roles = roleMapper.findRolesByUserId(user.getId()); //admin user
        if (roles == null || roles.isEmpty())
            throw new DisabledException("权限非法");
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(
                //security框架规定权限前面必须拼接ROLE_作为前缀,或者直接在数据库权限信息中拼接ROLE_作为前缀
                new SimpleGrantedAuthority("ROLE_" + role.getName())
//                for(Role role : roles)
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        ));
        //将用户权限所有信息填充到User中的List<GrantedAuthority> authorityList中
        user.setAuthorityList(authorities);
        return user;
    }

    @Override
    public User findUserByName(String userName) {
        User user = userMapper.findByName(userName);
        if (user == null)
            return null;
        return rolesShow(user);
    }

    @Override
    public User findUserByTelephone(String telephone) {
        User user = userMapper.findUserByPhoneNumber(telephone);
        if (user == null)
            return null;
        return rolesShow(user);
    }

    @Override
    public ServiceResult<UserDTO> findById(Long userId) {
        User user = userMapper.findOne(userId);
        if (user == null)
            return ServiceResult.notFound();
        //modelMapper:将xxxO之间互相转换的,这里是将user和UserDTO互相转换,避免了set
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ServiceResult.of(userDTO);
    }

    @Override
    @Transactional //声明式事务,事务处理,此处保证两个表同时执行或者都不执行
    public User addUserByPhone(String telephone) {

        //根据手机号创建一个用户
        User user = new User();
        user.setPhoneNumber(telephone);
        user.setName(telephone.substring(0,3) + "****" + telephone.substring(7));
        user.setStatus(0); //0:启用 1:禁用
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setLastUpdateTime(now);
        userMapper.save(user);

        //创建用户的同时给用户创建一个权限
        Role role = new Role();
        role.setName("USER");
        role.setUserId(user.getId());
        roleMapper.save(role);
        //将role权限信息添加到User中的List<GrantedAuthority> authorityList
        //SimpleGrantedAuthority:
        user.setAuthorityList(Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER")));

        return user;
    }

    @Override
    //profile:网页中需要修改内容的属性(修改名称栏、修改邮箱栏、修改密码栏)
    public ServiceResult modifyUserProfile(String profile, String value) {
        Long userId = LoginUserUtil.getLoginUserId();
        if (profile == null || profile.isEmpty())
            return ServiceResult.of(false,"属性不能为空");

        switch (profile){
            case "name":
                userMapper.updateUsername(userId,value);
                break;
            case "email":
                userMapper.updateEmail(userId,value);
                break;
            case "password":
                //userMapper.updatePassword(userId,passwordEncoder.encodePassword(value)); //老版本的写法
                //passwordEncoder.encode():密码编码,密码存入是经过加密的
                userMapper.updatePassword(userId,passwordEncoder.encode(value));
                break;
            default:
                return ServiceResult.of(false,"不支持的属性");
        }

        return ServiceResult.success();

    }
}

