package com.syoffice.app.reservation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.syoffice.app.common.PagingDTO;
import com.syoffice.app.employee.domain.EmployeeVO;
import com.syoffice.app.reservation.domain.ReservationVO;
import com.syoffice.app.reservation.domain.ResourceVO;
import com.syoffice.app.reservation.service.ReservationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/reservation/*")
public class ReservationController {

   @Autowired
   private ReservationService service;
   
    // 로그인한 사용자의 emp_id를 반환하는 메소드
    private String getEmpIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        EmployeeVO loginuser = (EmployeeVO) session.getAttribute("loginuser");
        return loginuser != null ? loginuser.getEmp_id() : null;
    }


   // === 예약 홈 페이지 === //
   @GetMapping("reservIndex")
   public ModelAndView meetingRoomReservation(HttpServletRequest request, ModelAndView mav) {

      Map<String,String> paraMap = new HashMap<>();
       String category_no = request.getParameter("category_no");
      
      if (category_no == null) {
          category_no = "1";
       }

      // 예약 화면에 노출될 제목 명을 위함
      request.setAttribute("category_no", category_no);
      // 조회를 위함
      paraMap.put("category_no", category_no);
      
      int totalRowCount = service.getReservationCount(paraMap);
      String param_curPage = request.getParameter("curPage");
      
      //////////////////////페이징 처리 /////////////////////////
      int curPage = 1;
      
      PagingDTO pagingDTO = new PagingDTO();   // Pagind DTO 초기화
      pagingDTO.setPageSize(5);   // 보여줄 페이지 수
      
      if (param_curPage != null) {
         curPage = Integer.parseInt(param_curPage);
      }
      
      pagingDTO.setRowSizePerPage(5);   // 한 페이지에 보여줄 행 수 가져오기
      pagingDTO.setCurPage(curPage);   // 현재 페이지 가져오기
      pagingDTO.setTotalRowCount(totalRowCount);   // 전체 행 개수 가져오기
      pagingDTO.pageSetting();   // 페이징 계산 공식
      mav.addObject("pagingDTO", pagingDTO);
      //////////////////////페이징 처리 /////////////////////////
      
      System.out.println("totalRowCount >>>>>" + totalRowCount);
      
      List<ReservationVO> reservationList = service.selectAllReservation(paraMap);
      mav.addObject("category_no", category_no);
      mav.addObject("reservationList", reservationList);

      return mav;
   }

   // === 내 예약 현황 페이지 === //
   @GetMapping("reservationList")
   public ModelAndView reservationList(HttpServletRequest request, ModelAndView mav) {

      String emp_id = getEmpIdFromSession(request);

      // 세션에 있는 아이디 값 담아주기
      // String emp_id = loginuser.getEmp_id();

      System.out.println("로그인 아이디 :: " + emp_id);
      
      //////////////////////페이징 처리 /////////////////////////
      int curPage = 1;
      
      try {
         // 존재하지 않는 페이지를 URL을 통해 접근했을 때 예외처리
         curPage = Integer.parseInt(request.getParameter("curPage"));
      } catch (NumberFormatException e) {
         // 1페이지로 보내기
         curPage = 1;
      }
      
      PagingDTO pagingDTO = new PagingDTO();   // Pagind DTO 초기화
      pagingDTO.setPageSize(5);   // 보여줄 페이지 수
      
      Map<String,String> paraMap = new HashMap<>();
      
      paraMap.put("fk_emp_id", emp_id);
      
      int totalRowCount = service.getReservationCount(paraMap);
      
      // URL을 통해 비정상적인 접근을 한 경우 1페이지로 보내기
      if(curPage > totalRowCount || curPage < 1) {
         curPage = 1;
      }
      
      pagingDTO.setRowSizePerPage(5);   // 한 페이지에 보여줄 행 수 가져오기
      
      pagingDTO.setCurPage(curPage);   // 현재 페이지 가져오기
      pagingDTO.setTotalRowCount(totalRowCount);   // 전체 행 개수 가져오기
      pagingDTO.pageSetting();   // 페이징 계산 공식
      
      // 시작행 번호
      String startRno = String.valueOf(pagingDTO.getFirstRow());
      // 마지막 행번호
      String endRno = String.valueOf(pagingDTO.getLastRow());
      
      // paraMap 에 넣어주기
      paraMap.put("startRno", startRno);
      paraMap.put("endRno", endRno);

      List<ReservationVO> reservationList = service.selectReservationByEmp(paraMap);
      mav.addObject("pagingDTO", pagingDTO);
      mav.addObject("paraMap", paraMap);
      //////////////////////페이징 처리 /////////////////////////

      mav.addObject("reservationList", reservationList);
      mav.setViewName("reservation/reservationList");

      return mav;
   }

   // === 자원 리스트 조회 === //
   @ResponseBody
   @GetMapping("selectResourceList")
   public List<ResourceVO> selectResourceList(@RequestParam(name = "category_no", defaultValue = "1") String category_no) {

      List<ResourceVO> resourceList = service.selectAllResource(category_no);

      return resourceList;
   }

   // === 전체 예약 현황 조회(페이징 적용 이전) === //
   /*
   @ResponseBody
   @GetMapping("selectReservationList")
   public List<ReservationVO> selectReservationList(HttpServletRequest request) {

      String category_no = request.getParameter("category_no");
      List<ReservationVO> list = service.selectAllReservation(category_no);

      return list;
   }
   */
   
   // === 전체 예약 현황 조회 === //
   @ResponseBody
   @GetMapping("selectReservationList")
   public List<ReservationVO> selectReservationList(HttpServletRequest request) {

       String category_no = request.getParameter("category_no");
       String param_curPage = request.getParameter("curPage");

       // === 페이징 처리 === //
       Integer curPage = 1;
       
       if (param_curPage != null) {
          curPage = Integer.parseInt(param_curPage);
       }

       // paraMap에 카테고리 번호 넣기
       Map<String, String> paraMap = new HashMap<>();
       paraMap.put("category_no", category_no);

       int totalRowCount = service.getReservationCount(paraMap); // 전체 예약 개수

       // URL을 통해 비정상적인 페이지 접근 시 1페이지로 보내기
       if (curPage > totalRowCount || curPage < 1) {
           curPage = 1;
       }


       // paraMap에 페이징 정보 넣기
       if (param_curPage != null) {
          PagingDTO pagingDTO = new PagingDTO();
          pagingDTO.setPageSize(5);  // 한 페이지에 보여줄 페이지 수 (여기서는 5로 설정)
          pagingDTO.setRowSizePerPage(5);  // 한 페이지에 보여줄 행 수
          pagingDTO.setCurPage(curPage);   // 현재 페이지
          pagingDTO.setTotalRowCount(totalRowCount); // 전체 행 개수
          pagingDTO.pageSetting();  // 페이징 계산
          
          // 시작행 번호
          String startRno = String.valueOf(pagingDTO.getFirstRow());
          // 마지막 행번호
          String endRno = String.valueOf(pagingDTO.getLastRow());
          paraMap.put("startRno", startRno);
          paraMap.put("endRno", endRno);
       }
       
       System.out.println("selectAllReservation :: " + paraMap);

       // 예약 리스트 조회
       List<ReservationVO> reservationList = service.selectAllReservation(paraMap);

       // 페이징 정보와 예약 리스트 반환
       return reservationList;
   }


   // === 예약 등록 ===
   @ResponseBody
   @PostMapping("insert")
   public String addReservation(HttpServletRequest request) {

      String emp_id = getEmpIdFromSession(request);

      String reserv_start = request.getParameter("reserv_start");
      String reserv_end = request.getParameter("reserv_end");
      String fk_resource_no = request.getParameter("fk_resource_no");

      Map<String, String> paraMap = new HashMap<String, String>();
      paraMap.put("reserv_start", reserv_start);
      paraMap.put("reserv_end", reserv_end);
      paraMap.put("fk_resource_no", fk_resource_no);
      paraMap.put("emp_id", emp_id);

      Integer n = service.insertReservation(paraMap);

      return n.toString();
   }

   // === 예약 수정 ===
   @ResponseBody
   @PutMapping("update")
   public String updateReservation(HttpServletRequest request) {

      String reserv_no = request.getParameter("reserv_no");
      String reserv_start = request.getParameter("reserv_start");
      String reserv_end = request.getParameter("reserv_end");
      String fk_resource_no = request.getParameter("fk_resource_no");

      Map<String, String> paraMap = new HashMap<String, String>();
      paraMap.put("reserv_no", reserv_no);
      paraMap.put("reserv_start", reserv_start);
      paraMap.put("reserv_end", reserv_end);
      paraMap.put("fk_resource_no", fk_resource_no);

      Integer n = service.updateReservation(paraMap);

      return n.toString();
   }

   // === 예약 삭제 ===
   @ResponseBody
   @DeleteMapping("delete")
   public String deleteReservation(HttpServletRequest request) {

      String reserv_no = request.getParameter("reserv_no");

      Integer n = service.deleteReservation(reserv_no);

      return n.toString();
   }

}
