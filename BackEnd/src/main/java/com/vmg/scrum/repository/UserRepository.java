package com.vmg.scrum.repository;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.excel.LogDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    User getById(Long id);
    Optional<User> findByUsername(String username);


  @Query(value = "select u from User u " +
            " where u.username = ?1 ")
    User findByUserName(String username);

  @Query(value = "select d.name from User u, Department d " +
          " where u.departments.id = d.id "+
            "and u.username = ?1")
  String findDepartmentByUserID(String username);



  @Query(value = "select u from User u " +
          " where u.username = ?1 ")
  User findByfullName(String username);

    User getByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByCode(String code);

    List<User> findAllByDepartments_Id(long id);

    List<User> findAll();

    User findByCode(String code);

    // MANAGE USER
    @Query(value = "select u from User u " +
            " order by u.id desc")
    @Override
    Page<User> findAll(Pageable pageable);
    @Query(value = "select u from User u " +
            " where u.avalible = ?1 " +
            " order by u.id desc")
    Page<User> findAll_Status(boolean status, Pageable pageable);
    @Query(value = "select u from User u " +
            " where ( u.fullName LIKE %?1% " +
            " or u.username LIKE %?1% ) " +
            " order by u.id desc")
    Page<User> findAll_Search(String search, Pageable pageable);

    @Query(value = "select u from User u " +
            " where (u.fullName LIKE %?1% " +
            " or u.username LIKE %?1% )" +
            " and u.avalible = ?2 " +
            " order by u.id desc")
    Page<User> findAll_Search_Status(String search,boolean status, Pageable pageable);

    @Query(value = "select u from User u " +
            " where u.departments.id = ?1 " +
            " order by u.id desc")
    Page<User> getUsersByDepartments_Id(long id, Pageable pageable);
    @Query(value = "select u from User u " +
            " where u.departments.id = ?1 " +
            " and u.avalible = ?2 " +
            " order by u.id desc")
    Page<User> getUsersByDepartments_Id_Status(long id, boolean status, Pageable pageable);

    @Query(value = "select u from User u " +
            " where u.departments.id = ?1 " +
            " and (u.fullName LIKE %?2% " +
            " or u.username LIKE %?2% ) " +
            " order by u.id desc")
    Page<User> getUsersByDepartments_Id_Search(long id,String search, Pageable pageable);

    @Query(value = "select u from User u " +
            " where u.departments.id = ?1 " +
            " and u.avalible = ?3 " +
            " and (u.fullName LIKE %?2% " +
            " or u.username LIKE %?2% ) " +
            " order by u.id desc ")
    Page<User> getUsersByDepartments_Id_Search_Status(long id,String search, boolean status, Pageable pageable);

  @Query(value = "select u from User u " +
          " where u.position.id = 1 "+
          "or u.position.id= 2"+
             "or u.position.id = 3"+
               "or u.position.id = 4")
  List<User> findUserByPositionID();
}
