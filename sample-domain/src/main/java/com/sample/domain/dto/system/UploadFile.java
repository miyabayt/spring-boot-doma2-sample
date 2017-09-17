package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.BZip2Data;
import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.MultipartFileConvertible;

import lombok.Getter;
import lombok.Setter;

@Table(name = "upload_files")
@Entity
@Getter
@Setter
public class UploadFile extends DomaDtoImpl implements MultipartFileConvertible {

    private static final long serialVersionUID = 1738092593334285554L;

    @OriginalStates // 差分UPDATEのために定義する
    UploadFile originalStates;

    @Id
    @Column(name = "upload_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID<UploadFile> id;

    // ファイル名
    @Column(name = "file_name")
    String filename;

    // オリジナルファイル名
    @Column(name = "original_file_name")
    String originalFilename;

    // コンテンツタイプ
    String contentType;

    // コンテンツ
    BZip2Data content;
}
