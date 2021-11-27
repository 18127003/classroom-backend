package com.example.demo.util;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelUtil {
    public List<StudentInfo> importStudentInfo(MultipartFile file, Classroom classroom) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        var sheet = workbook.getSheet("Student");
        List<StudentInfo> data = new ArrayList<>();
        sheet.forEach(row->{
            if(row.getRowNum()==0){
                return;
            }
            var studentIdCell = row.getCell(0);
            String studentId;
            if(studentIdCell.getCellType().equals(CellType.NUMERIC)){
                studentId = String.valueOf(studentIdCell.getNumericCellValue());
            }
            else{
                studentId = row.getCell(0).getStringCellValue();
            }
            var name = row.getCell(1).getStringCellValue();
            data.add(new StudentInfo(studentId, name, classroom));
        });
        return data;
    }
}
