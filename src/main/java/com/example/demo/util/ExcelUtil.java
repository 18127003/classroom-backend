package com.example.demo.util;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentInfo;
import com.querydsl.core.Tuple;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * de xu ly cac tac vu lien quan den excel nhu tao, import file excel
 * da duoc autowire trong class AssignmentServiceImpl
 * **/
@Component
public class ExcelUtil {
    private static final String STUDENT_LIST_SHEET_NAME ="Student List" ;
    private static final String[] EXCEL_STUDENT_LIST_HEADERS = new String[]{"Student ID","Name"};
    private static final String[] EXCEL_ASSIGNMENT_GRADE_HEADERS = new String[]{"ID","Grades"};

    public List<StudentInfo> importStudentInfo(MultipartFile file, Classroom classroom) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        var sheet = workbook.getSheet(STUDENT_LIST_SHEET_NAME);
        List<StudentInfo> data = new ArrayList<>();
        sheet.forEach(row->{
            if(row.getRowNum()==0){
                return;
            }
            var studentId = getStringValue(row.getCell(0));
            var name = row.getCell(1).getStringCellValue();
            data.add(new StudentInfo(studentId, name, classroom));
        });
        return data;
    }

    public void exportAssignmentGrades(List<Tuple> data, SXSSFWorkbook workbook, CellStyle textStyle){
        var assignmentName = data.get(0).get(1,String.class);
        final SXSSFSheet sheet = workbook.createSheet(assignmentName);
        final var rowId = 0;
        final SXSSFRow headerRow = sheet.createRow(rowId);
        for (var i = 0; i < EXCEL_ASSIGNMENT_GRADE_HEADERS.length; i++) {
            headerRow.createCell(i, CellType.STRING).setCellValue(EXCEL_ASSIGNMENT_GRADE_HEADERS[i]);
            sheet.trackColumnForAutoSizing(i);
        }
        Tuple tuple;
        Integer grade;
        for(var i=0; i<data.size();++i){
            final  SXSSFRow submissionRow=sheet.createRow(i+1);
            var colId=0;
            tuple = data.get(i);
            final var studentIdCell = submissionRow.createCell(colId++,CellType.STRING);
            studentIdCell.setCellStyle(textStyle);
            studentIdCell.setCellValue(tuple.get(2, String.class));
            grade = tuple.get(3, Integer.class);
            if(grade==null){
                submissionRow.createCell(colId,CellType.NUMERIC).setCellValue("");
            } else {
                submissionRow.createCell(colId,CellType.NUMERIC).setCellValue(grade);
            }
        }

    }
    public void exportStudentList(SXSSFWorkbook workbook,List<StudentInfo> students, CellStyle textStyle){
        final SXSSFSheet sheet = workbook.createSheet(STUDENT_LIST_SHEET_NAME);
        final var rowId = 0;
        final SXSSFRow headerRow = sheet.createRow(rowId);
        for (var i = 0; i < EXCEL_STUDENT_LIST_HEADERS.length; i++) {
            headerRow.createCell(i, CellType.STRING).setCellValue(EXCEL_STUDENT_LIST_HEADERS[i]);
            sheet.trackColumnForAutoSizing(i);
        }
        for (var i=0;i<students.size();++i){
            final  SXSSFRow studentRow=sheet.createRow(i+1);
            var colId=0;
            final var student=students.get(i);
            final var studentIdCell = studentRow.createCell(colId++,CellType.STRING);
            studentIdCell.setCellStyle(textStyle);
            studentIdCell.setCellValue(student.getStudentId());
            studentRow.createCell(colId,CellType.STRING).setCellValue(student.getName());
        }
        for (var i = 0; i < EXCEL_STUDENT_LIST_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

    }
    public void exportTemplate(final HttpServletResponse response, Map<Long,List<Tuple>> data, List<StudentInfo> studentInfos) throws IOException {
        final var workbook = new SXSSFWorkbook();
        DataFormat fmt = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(fmt.getFormat("@"));
        exportStudentList(workbook,studentInfos, cellStyle);
        data.forEach((key, value) -> exportAssignmentGrades(value, workbook, cellStyle));
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
    }

    public Map<String, Integer> importSubmission(MultipartFile file, Assignment assignment) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        var sheet = workbook.getSheet(assignment.getName());
        Map<String, Integer> data = new HashMap<>();
        sheet.forEach(row->{
            if(row.getRowNum()==0){
                return;
            }
            var studentId = getStringValue(row.getCell(0));
            var grade = Integer.valueOf((int)row.getCell(1).getNumericCellValue());
            data.put(studentId, grade);
        });
        return data;
    }

    private String getStringValue(Cell cell) {
        String result;
        if(cell.getCellType().equals(CellType.NUMERIC)){
            result = String.valueOf(cell.getNumericCellValue());
        }
        else{
            result = cell.getStringCellValue();
        }
        return result;
    }
}
