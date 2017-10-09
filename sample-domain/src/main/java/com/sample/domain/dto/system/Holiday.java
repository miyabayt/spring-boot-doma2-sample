package com.sample.domain.dto.system;

import java.time.LocalDate;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.ID;

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
    ID<Holiday> id;

    // 祝日名
    String holidayName;

    // 日付
    LocalDate holidayDate;
}
