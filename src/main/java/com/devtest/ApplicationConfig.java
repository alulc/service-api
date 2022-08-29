package com.devtest;

import com.devtest.api.mapper.ModelMapper;
import com.devtest.api.util.ServiceUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ServiceUtils serviceUtils() {
        return new ServiceUtils();
    }
}
