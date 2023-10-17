package com.vmg.scrum.repository;

import com.vmg.scrum.model.excel.LogCheckInOut;
import com.vmg.scrum.model.excel.LogInLateOutEarly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LogInLateOutEarlyRepository extends JpaRepository<LogInLateOutEarly, Long> {
    @Query(value = "select l from LogInLateOutEarly l\n" +
            " join l.user u " +
            " where (:code is null or :code = '' or u.code = :code ) " +
            " and ( :departmentId = -1 or u.departments.id = :departmentId ) " +
            " and (:search is null or :search = '' or u.fullName LIKE %:search% or u.departments.name LIKE %:search% or u.username LIKE %:search%)" +
            " and l.dateCheck >= :from and l.dateCheck <= :to " +
            " order by l.dateCheck desc ")
    Page<LogInLateOutEarly> getData(String code, Integer departmentId, String search, LocalDate from, LocalDate to, Pageable pageable);
}
