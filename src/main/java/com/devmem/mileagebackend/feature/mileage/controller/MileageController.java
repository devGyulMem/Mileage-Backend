package com.devmem.mileagebackend.feature.mileage.controller;

import com.devmem.mileagebackend.feature.mileage.dto.MileageDto;
import com.devmem.mileagebackend.utils.ResponseMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. 마일리지"})
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MileageController {

    @ApiOperation(value = "마일리지 적립")
    @PostMapping("/events")
    public ResponseMap manageMileage(MileageDto.Request request){
        return new ResponseMap();
    }

    @ApiOperation(value = "마일리지 내역조회")
    @GetMapping("")
    public ResponseMap getMileageHistory(MileageDto.Request request){
        return new ResponseMap();
    }
}
