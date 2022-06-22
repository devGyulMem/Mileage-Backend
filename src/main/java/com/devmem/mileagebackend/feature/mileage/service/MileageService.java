package com.devmem.mileagebackend.feature.mileage.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;
import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;
import com.devmem.mileagebackend.feature.mileage.domain.ReviewHistory;
import com.devmem.mileagebackend.feature.mileage.dto.MileageDto;
import com.devmem.mileagebackend.feature.mileage.dto.MileageHistoryDto;
import com.devmem.mileagebackend.feature.mileage.dto.MileageUserDto;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageHistoryRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.MileageUserRepo;
import com.devmem.mileagebackend.feature.mileage.persistence.ReviewHistoryRepo;
import com.devmem.mileagebackend.utils.ResponseMap;
import com.exception.ResponseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final MileageUserRepo mileageUserRepo;
    private final MileageHistoryRepo mileageHistoryRepo;
    private final ReviewHistoryRepo reviewHistoryRepo;

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap saveMileage(MileageDto.Request request) throws Throwable {

        ResponseMap result = new ResponseMap();

        reviewHistoryRepo.findByReviewId(request.getReviewId()).ifPresent(review -> { throw new ResponseException("같은 review id가 있어 리뷰를 더이상 작성할 수 없습니다.(already exist)", "01"); });
        reviewHistoryRepo.findByPlaceIdAndUserId(request.getPlaceId(), request.getUserId()).ifPresent(review -> { throw new ResponseException("해당 장소에 이미 작성한 리뷰가 있습니다.", "01"); });
        
        // 첫리뷰 검증
        List<ReviewHistory> reviewHistory = reviewHistoryRepo.findAllByPlaceId(request.getPlaceId());

        // 점수 산정
        long newPoints = calculatePoints(request, reviewHistory.isEmpty());

        // 저장
        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElse(MileageUser.builder().userId(request.getUserId()).build());
        mileageUser.setMileage(mileageUser.getMileage() + newPoints);
        mileageUserRepo.save(mileageUser);

        // reviewId, placeId, 첫번째 리뷰 여부 저장
        ReviewHistory newReviewHistory = ReviewHistory.builder()
                .placeId(request.getPlaceId())
                .reviewId(request.getReviewId())
                .userId(request.getUserId())
                .isFirstReview(reviewHistory.isEmpty() ? true : null)
                .build();

        reviewHistoryRepo.save(newReviewHistory);

        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), request.getContent(), newPoints);

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 리뷰 신규 등록 마일리지 적립");
        result.put("mileageUser", mileageUser);
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap updateMileage(MileageDto.Request request) throws Throwable{

        ResponseMap result = new ResponseMap();

        // 유저 정보를 찾음
        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new ResponseException("유저 정보를 찾을 수 없습니다.", "01"));

        // 첫 리뷰인지 확인
        ReviewHistory reviewHistory = reviewHistoryRepo.findByReviewId(request.getReviewId()).orElseThrow(()-> new ResponseException("수정할 리뷰를 찾을 수 없습니다.", "01"));

        // 가장 최근에 적립된 기록을 찾음
        MileageHistory latestData = mileageHistoryRepo.findFirstByReviewIdOrderByMileageHistoryIdDesc(request.getReviewId()).orElseThrow(()->new ResponseException("이전 기록을 찾을 수 없습니다.", "01"));

        long newPoints = calculatePoints(request, reviewHistory.getIsFirstReview());

        mileageUser.setMileage(mileageUser.getMileage() - latestData.getPoints() + newPoints);
        mileageUserRepo.save(mileageUser);

        // 이전 적립을 반환 후 새 포인트 부여
        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), "리뷰 수정 마일리지 반환", latestData.getPoints() * (-1));
        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(), "리뷰 수정 마일리지 적립", newPoints);
  
        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 리뷰 수정 : 마일리지 재적립");
        result.put("mileageUser", MileageUserDto.Response.of(mileageUser));
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public ResponseMap deleteMileage(MileageDto.Request request)throws Throwable{

        ResponseMap result = new ResponseMap();

        MileageUser mileageUser = mileageUserRepo.findByUserId(request.getUserId()).orElseThrow(()->new ResponseException("수정한 리뷰의 유저 마일리지 정보를 찾을 수 없습니다.", "01"));

        MileageHistory latestData = mileageHistoryRepo.findFirstByReviewIdOrderByMileageHistoryIdDesc(request.getReviewId()).orElseThrow(()-> new ResponseException("수정한 리뷰의 유저 마일리지의 정보를 찾을 수 없습니다.", "01"));

        mileageUser.setMileage(mileageUser.getMileage() - latestData.getPoints());
        ReviewHistory reviewHistory = reviewHistoryRepo.findByReviewId(request.getReviewId()).orElseThrow(() -> new ResponseException("삭제할 리뷰 내역을 찾을 수 없습니다.", "01"));
        reviewHistoryRepo.delete(reviewHistory);

        createMileageHistory(request.getUserId(), request.getPlaceId(), request.getReviewId(), request.getAction(),"리뷰 삭제 마일리지 반환", latestData.getPoints() * (-1) );
        
        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 리뷰 삭제 : 마일리지 반환");
        result.put("mileageUser", MileageUserDto.Response.of(mileageUser));
        return result;
    }


    @Transactional(rollbackOn = Exception.class)
    public ResponseMap createMileageHistory(UUID userId, UUID placeId, UUID reviewId,  String action, String content, long points){

        ResponseMap result = new ResponseMap();

        MileageHistory mileageHistory = MileageHistory.builder()
                .userId(userId)
                .placeId(placeId)
                .reviewId(reviewId)
                .action(action)
                .content(content)
                .points(points)
                .build();
        
        mileageHistoryRepo.save(mileageHistory);
        
        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 마일리지 내역 저장 완료");
        return result;
    }

    /**
     * getMileageUser() : 전체 사용자의 마일리지 정보를 반환합니다.
     * */
    @Transactional(rollbackOn = Exception.class)
    public ResponseMap getMileageUser(){

        ResponseMap result = new ResponseMap();

        List<MileageUserDto.Response> userList = mileageUserRepo.findAll().stream().map(MileageUserDto.Response::of).collect(Collectors.toList());

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 사용자 마일리지 정보 조회");
        result.put("mileageUser", userList.isEmpty() ? "사용자 마일리지 정보가 없습니다." : userList);
        return result;
    }


    /**
     * getMileageHistory() : 전체 마일리지 내역을 조회합니다.
     * */
    @Transactional(rollbackOn = Exception.class)
    public ResponseMap getMileageHistory(){

        ResponseMap result = new ResponseMap();

        mileageHistoryRepo.findAll();
        List<MileageHistoryDto.HistoryResponse> historyList = mileageHistoryRepo.findAll().stream().map(MileageHistoryDto.HistoryResponse::of).collect(Collectors.toList());

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 사용자 마일리지 전체 이용 내역 조회");
        result.put("historyList", historyList.isEmpty() ? "이용 내역이 없습니다." : historyList);
        
        return result;
    }

    /**
     * getMileageHistory(request) : 특정 사용자의 마일리지 내역을 조회합니다.
     * */
    @Transactional(rollbackOn = Exception.class)
    public ResponseMap getMileageHistory(MileageHistoryDto.HistoryRequest request){

        ResponseMap result = new ResponseMap();

        List<MileageHistoryDto.HistoryResponse> historyList = mileageHistoryRepo.findAllByUserId(request.getUserId()).stream().map(MileageHistoryDto.HistoryResponse::of).collect(Collectors.toList());

        result.put("rsltCd", "00");
        result.put("resltMsg", "Success - 사용자 마일리지 이용 내역 조회");
        result.put("historyList", historyList.isEmpty() ? "이용 내역이 없습니다." : historyList);
        
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
