package com.syoffice.app.reservation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.reservation.domain.ReservationVO;
import com.syoffice.app.reservation.domain.ResourceVO;
import com.syoffice.app.reservation.model.ReservationDAO;

@Service
public class ReservationService_imple implements ReservationService {

   @Autowired
   private ReservationDAO mapper_dao;

   // === 전체/내 예약 현황 총 개수 === //
   @Override
   public int getReservationCount(Map<String, String> paraMap) {
      return mapper_dao.getReservationCount(paraMap);
   }

   // === 내 예약 현황 조회 === //
   @Override
   public List<ReservationVO> selectReservationByEmp(Map<String, String> paraMap) {

      List<ReservationVO> reservList = mapper_dao.getReservationList(paraMap);

      for (ReservationVO reservVO : reservList) {
         reservVO.setPosibleCancel();
         reservVO.setPassed();
      }

      return reservList;
   }

   // === 전체 예약 현황 조회 === //
   @Override
   public List<ReservationVO> selectAllReservation(Map<String, String> paraMap) {

      List<ReservationVO> reservList = mapper_dao.getReservationList(paraMap);

      for (ReservationVO reservVO : reservList) {
         reservVO.setPosibleCancel();
         reservVO.setPassed();
      }

      return reservList;
   }

   // === 카테고리(회의실, 차량) 별 자원 리스트 조회 === //
   @Override
   public List<ResourceVO> selectAllResource(String category_no) {

      if (category_no == null) {
         category_no = "1";
      }

      return mapper_dao.getResourceList(category_no);
   }

   // === 예약 등록 === //
   @Override
   public int insertReservation(Map<String, String> paraMap) {
      return mapper_dao.insertReservation(paraMap);
   }

   // === 예약 수정 === //
   @Override
   public int updateReservation(Map<String, String> paraMap) {
      return mapper_dao.updateReservation(paraMap);
   }

   // === 예약 삭제 === //
   @Override
   public int deleteReservation(String reserv_no) {
      return mapper_dao.deleteReservation(reserv_no);
   }

}
