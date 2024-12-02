package com.yellobook.domains.inventory.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.yellobook.domains.inventory.dto.cond.ExcelProductCond;
import com.yellobook.support.error.code.FileErrorCode;
import com.yellobook.support.error.exception.CustomException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExcelReadUtil Unit Test")
class ExcelReadUtilTest {
    @InjectMocks
    private ExcelReadUtil excelReadUtil;

    @Nested
    @DisplayName("read 메소드는")
    class Describe_Read {

        private MultipartFile createMockMultipartFile(List<List<Object>> content) throws IOException {
            try {
                Workbook workbook = new XSSFWorkbook(); // 엑셀 워크북 생성
                Sheet sheet = workbook.createSheet("sheet1"); // 시트 생성
                for (int i = 0; i < content.size(); i++) {
                    Row row = sheet.createRow(i + 2);
                    List<Object> value = content.get(i);
                    for (int j = 0; j < value.size(); j++) {
                        Cell cell = row.createCell(j + 1);
                        Object cellValue = value.get(j);

                        if (cellValue instanceof String) {
                            cell.setCellValue((String) cellValue);
                        } else if (cellValue instanceof Integer) {
                            cell.setCellValue((Integer) cellValue);
                        } else if (cellValue instanceof Double) {
                            cell.setCellValue((Double) cellValue);
                        } else {
                            cell.setCellValue(String.valueOf(cellValue)); // 기본적으로 문자열로 변환
                        }
                    }
                }

                // 워크북을 바이트 배열로 변환
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                workbook.write(outputStream);
                workbook.close();

                // 바이트 배열을 MultipartFile로 변환
                return new MockMultipartFile("file.xlsx", outputStream.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException("엑셀 파일 생성 실패", e);
            }
        }

        @Nested
        @DisplayName("파일 IO에 실패하면")
        class Context_File_IO_Fail {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                file = mock(MultipartFile.class);
                when(file.getInputStream()).thenThrow(new IOException());
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                Assertions.assertThrows(IOException.class, () -> excelReadUtil.read(file));
            }
        }

        @Nested
        @DisplayName("파일이 비어있으면")
        class Context_File_Empty {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                file = mock(MultipartFile.class);
                when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.FILE_NOT_EXIST, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("파일의 양식이 .xlsx가 아니면")
        class Context_File_Type_Not_Xlsx {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                file = mock(MultipartFile.class);
                byte[] invalidContent = "This is not an Excel file.".getBytes();
                when(file.getInputStream()).thenReturn(new ByteArrayInputStream(invalidContent));
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.FILE_NOT_EXCEL, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("정수여야 하는 cell이 정수가 아니면")
        class Context_Integer_Cell_Not_Integer {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", "하위제품1", 1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", "잘못된 값", 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.CELL_INVALID_TYPE, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("정수여야 하는 cell이 0보다 작으면")
        class Context_Integer_Cell_Less_Than_0 {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", "하위제품1", -1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", 5, 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.INT_OVER_ONE, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("문자열이어야 하는 cell이 문자열이 아니면")
        class Context_String_Cell_Not_String {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", 10, 1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", 5, 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.CELL_INVALID_TYPE, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("SKU 중복이 있으면")
        class Context_SKU_Duplicate {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", "하위제품1", 1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", 1, 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.SKU_DUPLICATE, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("비어 있지 않은 row에 빈 cell이 존재하면")
        class Context_Empty_Cell_Exist {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", "하위제품1", 1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("예외를 반환한다.")
            void it_throws_exception() {
                CustomException exception = Assertions.assertThrows(CustomException.class, () ->
                        excelReadUtil.read(file));
                Assertions.assertEquals(FileErrorCode.ROW_HAS_EMPTY_CELL, exception.getErrorCode());
            }
        }

        @Nested
        @DisplayName("제약 조건을 통과하면")
        class Context_File_Valid {
            MultipartFile file;

            @BeforeEach
            void setUpContext() throws IOException {
                List<List<Object>> content = Arrays.asList(
                        Arrays.asList("제품1", "하위제품1", 1, 2, 3, 4),
                        Arrays.asList("제품2", "하위제품2", 5, 6, 7, 8)
                );
                file = createMockMultipartFile(content);
            }

            @Test
            @DisplayName("제품 정보를 반환한다.")
            void it_returns_product_info() throws IOException {
                List<ExcelProductCond> response = excelReadUtil.read(file);

                assertThat(response.size()).isEqualTo(2);
            }
        }

    }

}