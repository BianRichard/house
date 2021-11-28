package com.google.house.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {
    @Bean
    public ModelMapper modelMapper(){ //ModelMapper实体类之间转换(互相传输数据的)
        return new ModelMapper();
    }
}
