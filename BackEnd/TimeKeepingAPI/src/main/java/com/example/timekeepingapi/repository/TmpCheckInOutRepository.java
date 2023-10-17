//package com.example.timekeepingapi.repository;
//
//import com.example.timekeepingapi.models.TmpCheckInOut;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface TmpCheckInOutRepository extends JpaRepository<TmpCheckInOut, Long> {
//    @Query(value = "select * from tmpCHECKINOUT where  badgenumber=49 order by checktime desc", nativeQuery = true)
//    List<TmpCheckInOut> findByBadgeNumber(Long badgeNumber);
//}
