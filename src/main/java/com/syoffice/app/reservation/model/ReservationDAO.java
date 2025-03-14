package com.syoffice.app.reservation.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.syoffice.app.reservation.domain.ReservationVO;
import com.syoffice.app.reservation.domain.ResourceVO;

@Mapper 
public interface ReservationDAO {
   
   int getReservationCount(Map<String, String> paraMap);
   
   List<ReservationVO> getReservationList(Map<String, String> paraMap);

   List<ResourceVO> getResourceList(String category_no);

   int insertReservation(Map<String, String> paraMap);

   int updateReservation(Map<String, String> paraMap);

   int deleteReservation(String reserv_no);
}
