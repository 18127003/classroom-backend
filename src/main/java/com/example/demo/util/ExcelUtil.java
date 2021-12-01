package com.example.demo.util;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.StudentInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
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

    public void exportAssignmentGrades(Assignment assignment, SXSSFWorkbook workbook){
        final SXSSFSheet sheet = workbook.createSheet(assignment.getName());
        final var rowId = 0;
        final SXSSFRow headerRow = sheet.createRow(rowId);
        for (var i = 0; i < EXCEL_ASSIGNMENT_GRADE_HEADERS.length; i++) {
            headerRow.createCell(i, CellType.STRING).setCellValue(EXCEL_ASSIGNMENT_GRADE_HEADERS[i]);
            sheet.trackColumnForAutoSizing(i);
        }
        final var  submissions=assignment.getSubmissions();
        for(var i=0; i<submissions.size();++i){
            final  SXSSFRow submissionRow=sheet.createRow(i+1);
            var colId=0;
            final var submission=submissions.get(i);
            submissionRow.createCell(colId++,CellType.STRING).setCellValue(submission.getStudentInfo().getStudentId());
            submissionRow.createCell(colId,CellType.NUMERIC).setCellValue(submission.getGrade());
        }

    }
    public void exportStudentList(SXSSFWorkbook workbook,List<StudentInfo> students){
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
            studentRow.createCell(colId++,CellType.STRING).setCellValue(student.getStudentId());
            studentRow.createCell(colId,CellType.STRING).setCellValue(student.getName());
        }
        for (var i = 0; i < EXCEL_STUDENT_LIST_HEADERS.length; i++) {
            sheet.autoSizeColumn(i);
        }

    }
    public void exportTemplate(final HttpServletResponse response, List<StudentInfo> studentInfos, List<Assignment> assignments) throws IOException {
        final var workbook = new SXSSFWorkbook();
        exportStudentList(workbook,studentInfos);
        for (Assignment assignment : assignments) {
            exportAssignmentGrades(assignment, workbook);
        }
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
