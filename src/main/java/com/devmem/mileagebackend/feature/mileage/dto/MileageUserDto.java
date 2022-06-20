package com.devmem.mileagebackend.feature.mileage.dto;

import java.util.UUID;

import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;
import com.devmem.mileagebackend.utils.ObjectMapperUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MileageUserDto {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Response {

        private UUID mileageUserId;
        private UUID userId;
        private long mileage;

        public static Response of(MileageUser user){
            
            final Response dto = ObjectMapperUtils.map(user, Response.class);
            return dto;
            
        }

    }
    
}
