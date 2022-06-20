package com.devmem.mileagebackend.feature.mileage.service;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;
import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;
import com.devmem.mileagebackend.feature.mileage.domain.ReviewByPlace;
import com.devmem.mileagebackend.feature.mileage.dto.MileageDto;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageHistoryRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageUserRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.ReviewByPlaceRepo;
import com.devmem.mileagebackend.utils.ResponseMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageUserRepo mileageUserRepo;
    private final MileageHistoryRepo mileageHistoryRepo;
    private final ReviewByPlaceRepo reviewByPlaceRepo;

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap saveMileage(MileageDto.Request request) throws Throwable {

        ResponseMap result = new ResponseMap();

        long newPoints = calculatePoints(request);

        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new Exception("Exception :: Cannot find mileage user"));
        mileageUser.setMileage(mileageUser.getMileage() + newPoints);

        mileageUserRepo.save(mileageUser);

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success");
        result.put("mileageUser", mileageUser);
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap updateMileage(MileageDto.Request request) throws Throwable{

        ResponseMap result = new ResponseMap();

        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new Exception("Exception :: Cannot find mileage user"));

        // 리뷰 ID를 통해 history에서 조회하여 몇 포인트를 부여받았는지 조회
        MileageHistory recentData = mileageHistoryRepo.findFirstByReviewIdOrderByRegDtmDesc(request.getReviewId()).orElseThrow(()->new Exception("No Review Data"));
       
        // userMileage에서 현재 mileage 가져 온 후 -points1
        // 새롭게 부여할 point 계산 후 userMileage에 +points2 하여 저장
        long newPoints = calculatePoints(request);

        mileageUser.setMileage(mileageUser.getMileage() + newPoints - recentData.getPoints()); 
        mileageUserRepo.save(mileageUser);

        // history 에서 -points1 + poinst2 계산하여 저장, 내용 : "리뷰 수정 마일리지 적립/반환"
        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), "리뷰 수정 마일리지 반환", recentData.getPoints());
  
        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), "리뷰 수정 마일리지 적립", newPoints);
  
        result.put("rsltCd", "00");
        result.put("resltMsg", "Success");
        result.put("mileageUser", mileageUser);
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap deleteMileage(MileageDto.Request request)throws Throwable{

        ResponseMap result = new ResponseMap();

        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new Exception("Exception :: Cannot find mileage user"));

        // 리뷰 ID를 통해 history 에서 조회하여 몇 포인트를 부여받았는지 가져온다.
        MileageHistory recentData = mileageHistoryRepo.findFirstByReviewIdOrderByRegDtmDesc(request.getReviewId()).orElseThrow(()->new Exception("No Review Data"));     

        // mileageUser에서 현재의 mileage를 가져온 후 -points
        mileageUser.setMileage(mileageUser.getMileage() - recentData.getPoints());
        // createHistory로 "리뷰 삭제" 명목으로 히스토리 추가

        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(),"리뷰 삭제 마일리지 반환", recentData.getPoints() * (-1) );

        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap createMileageHistory(UUID userId, UUID placeId, UUID reviewId,  String action, String content, long points){

        ResponseMap result = new ResponseMap();

        MileageHistory mileageHistory = MileageHistory.builder()
                .userId(userId)
                .placeId(placeId)
                .reviewId(reviewId)
                .content(content)
                .build();
        
        mileageHistoryRepo.save(mileageHistory);
        
        return result;
    }

    public long calculatePoints(MileageDto.Request request){
        long totalPoints = 0;

        // 1자 이상 텍스트 : 1점
        if (request.getContent().length() >= 1)
            totalPoints++;
        // 1장 이상 사진 첨부 : 1점
        if (request.getAttachedPhotoIds().size() >= 1)
            totalPoints++;
        // 보너스 점수 - 장소에 첫 리뷰 작성 : 1점

        // history에서 placeId로 리뷰를 남긴 흔적이 있는지 확인

        return totalPoints;
    }

}
