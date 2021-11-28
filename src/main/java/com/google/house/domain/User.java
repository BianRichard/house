package com.google.house.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private Integer status;
    private LocalDateTime createTime; //DATETIME/TIMESTAMP
    private LocalDateTime lastLoginTime;
    private LocalDateTime lastUpdateTime;
    private String avatar;
    private List<GrantedAuthority> authorityList;
    //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String name, String password, String email, String phoneNumber, Integer status, LocalDateTime createTime, LocalDateTime lastLoginTime, LocalDateTime lastUpdateTime, String avatar) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.lastUpdateTime = lastUpdateTime;
        this.avatar = avatar;
    }
}
