package com.vmg.scrum.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Setter
@Getter
public class SignupRequest {
    @NotBlank(message = "Chưa nhập email")
    @Size(min = 3, max = 50)
    private String username;

    private Set<String> role;

    @NotBlank(message = "Chưa nhập tên")
    @Size(min = 6, max = 50)
    private String fullName;
    @NotNull(message = "Chưa nhập mã nhân viên")
    private String code;
    private long position;
    @NotBlank(message = "Chưa chọn phòng ban")
    private String department;
    @NotBlank(message = "Chưa chọn giới tính")
    private String gender;
    private MultipartFile cover;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startWork;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endWork;

    private String badgeNumber;
    private String ssn;
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }


    public LocalDate getStartWork() {
        return startWork;
    }

    public void setStartWork(LocalDate startWork) {
        this.startWork = startWork;
    }

    public LocalDate getEndWork() {
        return endWork;
    }

    public void setEndWork(LocalDate endWork) {
        this.endWork = endWork;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
