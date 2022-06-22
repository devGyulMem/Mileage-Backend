package com.devmem.mileagebackend.feature.mileage.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devmem.mileagebackend.feature.mileage.dto.MileageDto;
import com.devmem.mileagebackend.feature.mileage.service.MileageService;
import com.devmem.mileagebackend.utils.ResponseMap;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = {"1. 마일리지"})
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping()
public class MileageController {

    private final MileageService mileageService;

    @ApiOperation(value = "마일리지 적립")
    @PostMapping("/events")
    public ResponseMap manageMileage(@RequestBody MileageDto.Request request) throws Throwable{
        
        String action = request.getAction();
        return action.equals("ADD") ? mileageService.saveMileage(request) : action.equals("MOD") ? mileageService.updateMileage(request) : mileageService.deleteMileage(request);
    }

    @ApiOperation(value = "마일리지 내역조회")
    @GetMapping("")
    public ResponseMap getMileageHistory(){
        return mileageService.getMileageHistory();
    }
    
    @ApiOperation(value = "마일리지 내역조회")
    @PostMapping("")
    public ResponseMap getMileageUserHistory(@RequestBody UUID userId){
        return mileageService.getMileageHistory(userId);
    }
}
