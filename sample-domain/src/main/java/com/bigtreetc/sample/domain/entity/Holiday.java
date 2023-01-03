package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "holidays")
@Entity
@Getter
@Setter
public class Holiday extends DomaEntityImpl {

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
