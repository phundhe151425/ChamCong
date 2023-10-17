package com.vmg.scrum.model.excel;

import com.vmg.scrum.model.BaseEntity;
import com.vmg.scrum.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class LogInLateOutEarly extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_inLateOutEarly_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    private LocalDate dateCheck;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private String IOKind;

    private LocalTime duration;

    private String EmployeeId;

    private String Reason;
}
