package com.sample.web.admin.controller.html.users.users;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.DARK_GREEN;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.WHITE;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.val;
import org.apache.poi.ss.usermodel.*;

import com.sample.domain.dto.user.User;
import com.sample.web.base.view.ExcelView;

public class UserExcel implements ExcelView.Callback {

    @Override
    public void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook) {

        // シートを作成する
        Sheet sheet = workbook.createSheet("ユーザー");
        sheet.setDefaultColumnWidth(30);

        // フォント
        Font font = workbook.createFont();
        font.setFontName("メイリオ");
        font.setBold(true);
        font.setColor(WHITE.getIndex());

        // ヘッダーのスタイル
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(DARK_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("苗字");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("名前");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("メールアドレス");
        header.getCell(2).setCellStyle(style);

        // 明細
        @SuppressWarnings("unchecked")
        val users = (List<User>) data;

        int count = 1;
        for (User user : users) {
            Row userRow = sheet.createRow(count++);
            userRow.createCell(0).setCellValue(user.getLastName());
            userRow.createCell(1).setCellValue(user.getFirstName());
            userRow.createCell(2).setCellValue(user.getEmail());
        }
    }
}
