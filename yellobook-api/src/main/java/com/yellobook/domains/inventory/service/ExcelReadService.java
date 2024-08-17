package com.yellobook.domains.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
public class ExcelReadService {
    public void read(MultipartFile excelFile){
        /**
         * 1. 해당 파일이 엑셀 파일이 맞는지 확인
         * 2. 엑셀 파일이 맞다면 해당 파일의 이름으로 제목 추출
         * 3. 2B ~ 2G가 엑셀에 맞는 양식인지 확인
         * 4. 각 행을 읽으면서 값 유효성 확인 (and 만약 각 행중 어느 하나라도 값이 없거나 유효하지 않으면 예외 처리)
         *  4-a. 제품 코드 유효성 검사...ㅅㅂ
         *  4-b. 문자열, 정수가 맞는지 확인
         * 5. 유효성 확인이 끝났으면 인벤토리 생성 및 각 제품 저장
         */
        try{


            String fileName = excelFile.getName();
            log.info("[EXCEL] fileName : {}", fileName);


            InputStream file = excelFile.getInputStream();
            // workbook 생성
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            // sheet 가져오기
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 모든 2번째 row 조회 - 맞는 양식인지 확인 (ex. 제품 이름, ...)
            for (Row row : sheet) {
                if (row.getRowNum() == 1) {  // 두번째 row
                    validTitleRow(row, 1, 6);
                } else if (row.getRowNum() >= 2) {
                    validProductRow(row, 1, 6);
                }
            }
            workbook.close();
            file.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void validTitleRow(Row row, int startCol, int endCol){
        // B열부터 G열까지 조사 (인덱스 1부터 6까지)
        for(int i = startCol; i<=endCol; i++){
            Cell cell = row.getCell(i);
            if(cell != null){
                printCellValue(cell);
            } else {
                System.out.print("N/A\t"); // 셀이 비어있는 경우
            }
        }
        System.out.println();
    }

    private static void validProductRow(Row row, int startCol, int endCol){
        // B열부터 G열까지 조사 (인덱스 1부터 6까지)
        for(int i = startCol; i<=endCol; i++){
            Cell cell = row.getCell(i);
            if(cell != null){
                printCellValue(cell);
            } else {
                System.out.print("N/A\t"); // 셀이 비어있는 경우
            }
        }
        System.out.println();
    }

    private static void printCellValue(Cell cell){
        switch (cell.getCellType()) {
            case NUMERIC -> System.out.print((int) cell.getNumericCellValue() + "\t");
            case STRING -> System.out.print(cell.getStringCellValue() + "\t");
            default -> System.out.print("N/A\t"); // 지원하지 않는 타입의 경우
        }
    }
}
