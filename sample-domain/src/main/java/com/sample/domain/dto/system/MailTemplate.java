package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;

import lombok.Getter;
import lombok.Setter;

@Table(name = "mail_templates")
@Entity
@Getter
@Setter
public class MailTemplate extends DomaDtoImpl {

    private static final long serialVersionUID = -2997823123579780864L;

    @OriginalStates // 差分UPDATEのために定義する
    MailTemplate originalStates;

    @Id
    @Column(name = "mail_template_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // カテゴリキー
    String categoryKey;

    // メールテンプレートキー
    String templateKey;

    // メールタイトル
    String subject;

    // メール本文
    String templateBody;
}
