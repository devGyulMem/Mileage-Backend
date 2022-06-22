package com.devmem.mileagebackend.feature.mileage.service;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;
import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;
import com.devmem.mileagebackend.feature.mileage.domain.ReviewHistory;
import com.devmem.mileagebackend.feature.mileage.dto.MileageDto;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageHistoryRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageUserRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.ReviewHistoryRepo;
import com.devmem.mileagebackend.utils.ResponseMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageUserRepo mileageUserRepo;
    private final MileageHistoryRepo mileageHistoryRepo;
    private final ReviewHistoryRepo reviewHistoryRepo;

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap saveMileage(MileageDto.Request request) throws Throwable {

        ResponseMap result = new ResponseMap();

        List<ReviewHistory> reviewHistory = reviewHistoryRepo.findAllByPlaceId(request.getPlaceId());

        long newPoints = calculatePoints(request, reviewHistory == null);

        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new Exception("Exception :: Cannot find mileage user"));
        mileageUser.setMileage(mileageUser.getMileage() + newPoints);
        mileageUserRepo.save(mileageUser);

        ReviewHistory newReviewHistory = ReviewHistory.builder()
                .placeId(request.getPlaceId())
                .reviewId(request.getReviewId())
                .build();

        if (reviewHistory == null)
            newReviewHistory.setIsFirstReview(true);

        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), request.getContent(), newPoints);

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success");
        result.put("mileageUser", mileageUser);
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap updateMileage(MileageDto.Request request) throws Throwable{

        ResponseMap result = new ResponseMap();

        // 유저 정보를 찾음
        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new Exception("Exception :: Cannot found mileage user"));

        // ReviewByPlace 에서 첫리뷰인지 확인
        ReviewHistory reviewHistory = reviewHistoryRepo.findByReviewId(request.getReviewId());

        MileageHistory latestData = mileageHistoryRepo.findFirstByReviewIdOrderByRegDtmDesc(request.getReviewId()).orElseThrow(()->new Exception("Exception :: Cannot found mileage history"));

        long newPoints = calculatePoints(request, reviewHistory.getIsFirstReview());

        mileageUser.setMileage(mileageUser.getMileage() - latestData.getPoints() + newPoints);
        mileageUserRepo.save(mileageUser);

        // history 에서 -points1 + poinst2 계산하여 저장, 내용 : "리뷰 수정 마일리지 적립/반환"
        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), "리뷰 수정 마일리지 반환", latestData.getPoints());
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
        MileageHistory latestData = mileageHistoryRepo.findFirstByReviewIdOrderByRegDtmDesc(request.getReviewId()).orElseThrow(()->new Exception("No Review Data"));

        // mileageUser에서 현재의 mileage를 가져온 후 -points
        mileageUser.setMileage(mileageUser.getMileage() - latestData.getPoints());
        // createHistory로 "리뷰 삭제" 명목으로 히스토리 추가
        // TODO reviewByPlace가 아예 비게 되면 컬럼 자체를 삭제하는 로직 필요
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

    public long calculatePoints(MileageDto.Request request, Boolean isFirstReview){
        long totalPoints = 0;

        // 1자 이상 텍스트 : 1점
        if (request.getContent().length() >= 1)
            totalPoints++;
        // 1장 이상 사진 첨부 : 1점
        if (request.getAttachedPhotoIds().size() >= 1)
            totalPoints++;
        // 보너스 점수 - 장소에 첫 리뷰 작성 : 1점
        if (isFirstReview)
            totalPoints++;

        return totalPoints;
    }

}
