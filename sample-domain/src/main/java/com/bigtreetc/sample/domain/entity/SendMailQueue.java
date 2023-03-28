package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "send_mail_queue")
@Entity
@Getter
@Setter
public class SendMailQueue extends DomaEntityImpl {

  private static final long serialVersionUID = -4135869799913706558L;

  @OriginalStates // 差分UPDATEのために定義する
  SendMailQueue originalStates;

  @Id
  @Column(name = "send_mail_queue_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "from_address")
  String from;

  @Column(name = "to_address")
  String to;

  @Column(name = "cc_address")
  String cc;

  @Column(name = "bcc_address")
  String bcc;

  LocalDateTime sentAt;

  String subject;

  String body;
}
