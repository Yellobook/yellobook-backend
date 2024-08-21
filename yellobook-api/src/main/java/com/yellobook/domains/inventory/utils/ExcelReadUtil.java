package com.yellobook.domains.inventory.utils;

import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.error.code.FileErrorCode;
import com.yellobook.error.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
public class ExcelReadUtil {
    /**
     * 1. 해당 파일이 엑셀 파일이 맞는지 확인
     * 2. B ~ G가 엑셀에 맞는 양식인지 확인
     * 3. 각 행을 읽으면서 값 유효성 확인 (and 만약 각 행중 어느 하나라도 값이 없거나 유효하지 않으면 예외 처리)
     *  3-a. 문자열, 정수가 맞는지 확인
     * 4. sku 중복 있는지 확인
     * 5. 유효성 확인이 끝났으면 제품들 record로 저장
     */
    public List<ExcelProductCond> read(MultipartFile excelFile) throws IOException{
        List<ExcelProductCond> productConds = new ArrayList<>();
        HashSet<Integer> skuSet = new HashSet<>();

        // 빈 파일은 아닌지, .xlsx의 확장자가 맞는지 확인
        InputStream file = excelFile.getInputStream();
        XSSFWorkbook workbook;
        try{
            // workbook 생성
            workbook = new XSSFWorkbook(file);
        } catch (EmptyFileException e){
            throw new CustomException(FileErrorCode.FILE_NOT_EXIST);
        } catch(NotOfficeXmlFileException e){
            throw new CustomException(FileErrorCode.FILE_NOT_EXCEL);
        }

        // sheet 가져오기
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() >= 2) {  // 세번째 row
                 ExcelProductCond productCond = validProductRow(row, 1, 6, skuSet);
                 if(productCond != null){
                     productConds.add(productCond);
                 }
            }
        }
        workbook.close();
        file.close();

        return productConds;
    }

    /**
     * 제품 값이 있는 row 조사
     */
    private ExcelProductCond validProductRow(Row row, int startCol, int endCol, HashSet<Integer> skuSet){
        // B열부터 G열까지 조사 (인덱스 1부터 6까지)
        String name = null;
        String subProduct = null;
        Integer sku = null;
        Integer purchasePrice = null;
        Integer salePrice = null;
        Integer amount = null;

        boolean isRowEmpty = true;

        for(int i = startCol; i<=endCol; i++){
            Cell cell = row.getCell(i);
            if(cell != null && cell.getCellType() != CellType.BLANK){
                isRowEmpty = false;
                if (i == startCol) {
                    name = getStringCellValue(cell);
                } else if (i == startCol + 1) {
                    subProduct = getStringCellValue(cell);
                } else if (i == 3) {
                    sku = getIntCellValue(cell);
                    isSKUDuplicate(sku, skuSet);
                } else if (i == 4) {
                    purchasePrice = getIntCellValue(cell);
                } else if (i == 5) {
                    salePrice = getIntCellValue(cell);
                } else if (i == 6) {
                    amount = getIntCellValue(cell);
                }
            }
        }
        // row가 비어있는지 확인
        if(isRowEmpty){
            log.info("[EXCEL] Row is Empty");
            return null;
        }
        // 빈 값이 있는지 없는지 확인
        if(isRowValid(name, subProduct, sku, purchasePrice, salePrice, amount)) {
            return ExcelProductCond.builder()
                    .name(name).subProduct(subProduct).sku(sku)
                    .purchasePrice(purchasePrice).salePrice(salePrice).amount(amount)
                    .build();
        }else{
            throw new CustomException(FileErrorCode.ROW_HAS_EMPTY_CELL);
        }
    }

    /**
     * 비어 있지 않는 row 중에서 빈 cell이 있는지 없는지 확인
     */
    private boolean isRowValid(String name, String subProduct, Integer sku, Integer purchasePrice, Integer salePrice, Integer amount) {
        return name != null && subProduct != null && sku != null &&
                purchasePrice != null && salePrice != null && amount != null;
    }

    /**
     * cell 읽고, 문자열로 변환
     */
    private String getStringCellValue(Cell cell){
        if(cell.getCellType() == CellType.STRING){
            return cell.getStringCellValue();
        }
        throw new CustomException(FileErrorCode.CELL_INVALID_TYPE);
    }

    /**
     * cell 읽고, 정수로 반환
     */
    private Integer getIntCellValue(Cell cell){
        if(cell.getCellType() == CellType.NUMERIC){
            int num = (int)cell.getNumericCellValue();
            if(num < 0){
                throw new CustomException(FileErrorCode.INT_OVER_ONE);
            }
            return num;
        }
        throw new CustomException(FileErrorCode.CELL_INVALID_TYPE);
    }

    /**
     * SKU 중복 확인
     */
    private void isSKUDuplicate(Integer sku, HashSet<Integer> skuSet){
        if(skuSet.contains(sku)){
            log.info("[EXCEL] dupSKU# : {}", sku);
            throw new CustomException(FileErrorCode.SKU_DUPLICATE);
        } else{
            skuSet.add(sku);
        }
    }

}
