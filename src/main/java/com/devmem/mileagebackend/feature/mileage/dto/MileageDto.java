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

        @ApiModelProperty(value = "타입", example = "REVIEW")
        private String type;

        @ApiModelProperty(value = "액션 구분", example = "ADD")
        private String action;

        @ApiModelProperty(value = "리뷰 ID", example = "240a0658-dc5f-4878-9381-ebb7b2667772")
        private UUID reviewId;

        @ApiModelProperty(value = "리뷰 내용", example = "좋아요!")
        private String content;

        @ApiModelProperty(value = "첨부 파일 아이디 리스트", allowableValues = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8,afb0cef2-851d-4a50-bb07-9cc15cbdc332")
        private List<UUID> attachedPhotoIds;

        @ApiModelProperty(value = "사용자 ID", example = "3ede0ef2-92b7-4817-a5f3-0c575361f745")
        private UUID userId;

        @ApiModelProperty(value = "장소 ID", example = "2e4baf1c-5acb-4efb-a1af-eddada31b00f")
        private UUID placeId;
    }
}
