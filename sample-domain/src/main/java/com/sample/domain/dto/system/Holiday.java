package com.sample.domain.dto.system;

import java.time.LocalDate;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "holidays")
@Entity
@Getter
@Setter
public class Holiday extends DomaDtoImpl {

    private static final long serialVersionUID = 2399051382620886703L;

    // 祝日ID
    @Id
    @Column(name = "holiday_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 祝日名
    String holidayName;

    // 日付
    LocalDate holidayDate;
}
