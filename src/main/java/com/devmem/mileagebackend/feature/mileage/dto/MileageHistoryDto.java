package com.devmem.mileagebackend.feature.mileage.dto;

import java.util.UUID;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;
import com.devmem.mileagebackend.utils.ObjectMapperUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MileageHistoryDto {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class HistoryRequest {

        @ApiModelProperty(value = "사용자 ID", example = "3ede0ef2-92b7-4817-a5f3-0c575361f745")
        private UUID userId;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class HistoryResponse {

        private String mileageHistoryId;
        private UUID userId;
        private UUID placeId;
        private UUID reviewId;
        private String action;
        private String content;
        private long points;

        public static HistoryResponse of(MileageHistory mileageHistory){
            
            final HistoryResponse dto = ObjectMapperUtils.map(mileageHistory, HistoryResponse.class);
            dto.setMileageHistoryId(mileageHistory.getMileageHistoryId().toString());
            return dto;
        }
    }
}
