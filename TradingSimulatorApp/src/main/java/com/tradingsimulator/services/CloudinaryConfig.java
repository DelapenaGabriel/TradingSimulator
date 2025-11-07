package com.tradingsimulator.services;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dujc4iuu8",
                "api_key", "856964489283459",
                "api_secret", "MEoz3hNWMvhTTtzqnBNdpdJ73tc",
                "secure", true
        ));
    }
}