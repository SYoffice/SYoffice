package com.syoffice.app.reservation.service;

import java.util.List;
import java.util.Map;

import com.syoffice.app.reservation.domain.ReservationVO;
import com.syoffice.app.reservation.domain.ResourceVO;

public interface ReservationService {
   
   // === 전체/내 예약 현황 총 개수 === //
   int getReservationCount(Map<String, String> paraMap);
   
   // === 내 예약 현황 페이지 === //
   List<ReservationVO> selectReservationByEmp(Map<String, String> paraMap);

   // === 전체 예약 현황 페이지 === //
   List<ReservationVO> selectAllReservation(Map<String, String> paraMap);

   // === 카테고리 별 전체 리소스 조회 === //
   List<ResourceVO> selectAllResource(String category_no);

   // === 예약 등록 === //
   int insertReservation(Map<String, String> paraMap);

   // === 예약 수정 === //
   int updateReservation(Map<String, String> paraMap);

   // === 예약 삭제 === //
   int deleteReservation(String reserv_no);
}
