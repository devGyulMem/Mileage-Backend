package com.devmem.mileagebackend.feature.mileage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

public class MileageDto {

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Request {

        @ApiModelProperty(value = "타입")
        private String type;

        @ApiModelProperty(value = "액션 구분")
        private String action;

        @ApiModelProperty(value = "리뷰 ID")
        private UUID reviewId;

        @ApiModelProperty(value = "리뷰 내용")
        private String content;

        @ApiModelProperty(value = "첨부 파일 아이디 리스트")
        private List<UUID> attachedPhotoIds;

        @ApiModelProperty(value = "사용자 ID")
        private UUID userId;

        @ApiModelProperty(value = "장소 ID")
        private UUID placeId;
    }
}
