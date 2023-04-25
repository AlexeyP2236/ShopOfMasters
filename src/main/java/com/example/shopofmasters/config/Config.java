package com.example.shopofmasters.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    //Путь для хранения файлов
    @Value("${upload.path}")
    private String uploadPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Если обратиться по пути
        registry.addResourceHandler("/img/**")
                //то нужно идти по этому пути (file:/// для windows, file:// для Linux и masOS)
                .addResourceLocations("file:///" + uploadPath + "/");

    }
}
