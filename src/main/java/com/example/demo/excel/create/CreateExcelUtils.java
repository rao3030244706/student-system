package com.example.demo.excel.create;

import com.example.demo.annotation.ExcelExport;
import com.example.demo.entity.EStudent;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo 导出excel文件，下载excel文件
 * 在excel文件--->remember (0,0)就是A1，
 * row是行,即A,B,C-->x，column是列,即1,2,3-->y
 */

@Slf4j
public class CreateExcelUtils {
    /*public static void main(String[] args) {
        Class<EStudent> eStudentClass = EStudent.class;
        for (Field declaredField : eStudentClass.getDeclaredFields()) {
            System.out.print(declaredField.getName()+" ");
        }
        System.out.println("==============");
        Field[] declaredFields = eStudentClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            System.out.print(declaredFields[i].getName()+" ");
        }
        System.out.println("==============");
        Stream<Field> stream = Arrays.stream(declaredFields);
        stream.map(Field::getName).forEach(System.out::println);
    }*/

    public static void main(String[] args) {
        createEntityUserUseExcel(EStudent.class, 4);
    }

    /**
     * todo 这个是用于生成导出Entity的信息excel文件
     * 合并单元格占用两列
     *
     * @param list         list里的一个元素会被弄成一行记录
     * @param aClass
     * @param maxCreateRow 一个sheet里面最多写入多个row即多少个list的元素
     */
    public static void createEntityExcel(List<?> list, Class<?> aClass, int maxCreateRow) {
        try {
            //创建excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //excel的样式
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            //字体居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // Math.ceil向上取整，根据list.size与maxCreateRow可以计算到一个excel可以有多少个sheet，
            int sheetCount = (int) Math.ceil((list.size() * 1.0) / maxCreateRow);

            //记录list的第几个元素录入的excel
            int listIndex = 0;

            Field[] declaredFields = aClass.getDeclaredFields();


            ApiModel annotation = aClass.getAnnotation(ApiModel.class);
            System.out.println(annotation);
            List<String> filedNames = new ArrayList<>();
            long excelExportCount = Arrays.stream(declaredFields).
                    filter(field -> {
                        boolean result = field.getAnnotation(ExcelExport.class) != null;
                        if (result) {
                            filedNames.add(field.getName());
                        }
                        return result;
                    }).
                    count();
            //在excel文件中，字母代表X轴cell，数字代表Y轴row，指定合并单元格的区间
            CellRangeAddress address = CellRangeAddress.valueOf("A1:" + (char) ('A' + excelExportCount - 1) + "2");
            for (int i = 0; i < sheetCount; i++) {
                HSSFSheet sheet = workbook.createSheet(aClass.getSimpleName() + "-" + i);
                sheet.addMergedRegion(address);
                HSSFCell addressCell = sheet.createRow(address.getFirstColumn())//y
                        .createCell(address.getFirstRow());//x
                addressCell.setCellStyle(cellStyle);
                addressCell.setCellValue(annotation.description());
                //设置行宽
                sheet.setDefaultColumnWidth(18);

                //将含有@ExcelExport的Field的name录入到excel的一行去
                int filedNameRowIndex = address.getLastRow() + 1;
                HSSFRow filedNameRow = sheet.createRow(filedNameRowIndex);
                for (int n = 0; n < filedNames.size(); n++) {
                    HSSFCell cell = filedNameRow.createCell(n);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(filedNames.get(n));
                }
                //todo 在java中，sheet，row，cell与数组一样，都是从0开始的
                //address.getLastRow()+1从合并单元格的下一行开始，后面的+1是，filedName的填充到了一行
                for (int idxRow = address.getLastRow() + 1 + 1; idxRow < maxCreateRow + address.getLastRow() + 1 + 1; idxRow++) {
                    if (listIndex == list.size()) {//list的元素--全部被导入完了
                        log.debug("00-----list的元素全部被录入完-----00");
                        break;
                    }
                    HSSFRow row = sheet.createRow(idxRow);

                    for (int f = 0, count = 0; f < declaredFields.length; f++) {
                        declaredFields[f].setAccessible(true);
                        Object o = declaredFields[f].get(list.get(listIndex));

                        //被ExcelExport标记了，才需要导出
                        if (declaredFields[f].getAnnotation(ExcelExport.class) == null)
                            continue;

                        HSSFCell cell = row.createCell(count);
                        count++;
                        if (o != null) {
                            cell.setCellStyle(cellStyle);
                            cell.setCellValue(o.toString());
                        }
                    }
                    //使用了一个list里面的元素，就要+1
                    listIndex++;
                }
            }
            String property = System.getProperty("user.dir");
            String descPath = property + "\\src\\main\\resources";
            workbook.write(new File(descPath, "demo.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * todo 这个是用于生成导出Entity的信息excel文件
     * 合并单元格占用两列
     *
     * @param list         list里的一个元素会被弄成一行记录
     * @param aClass
     * @param maxCreateRow 一个sheet里面最多写入多个row即多少个list的元素
     */
    public static void createEntityExcelDownload(HttpServletResponse response, List<?> list, Class<?> aClass, int maxCreateRow) {
        try {
            //创建excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //excel的样式
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            //字体居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // Math.ceil向上取整，根据list.size与maxCreateRow可以计算到一个excel可以有多少个sheet，
            int sheetCount = (int) Math.ceil((list.size() * 1.0) / maxCreateRow);

            //记录list的第几个元素录入的excel
            int listIndex = 0;

            Field[] declaredFields = aClass.getDeclaredFields();


            ApiModel annotation = aClass.getAnnotation(ApiModel.class);
            System.out.println(annotation);
            List<String> filedNames = new ArrayList<>();
            long excelExportCount = Arrays.stream(declaredFields).
                    filter(field -> {
                        boolean result = field.getAnnotation(ExcelExport.class) != null;
                        if (result) {
                            filedNames.add(field.getName());
                        }
                        return result;
                    }).
                    count();
            //在excel文件中，字母代表X轴cell，数字代表Y轴row，指定合并单元格的区间
            CellRangeAddress address = CellRangeAddress.valueOf("A1:" + (char) ('A' + excelExportCount - 1) + "2");
            for (int i = 0; i < sheetCount; i++) {
                HSSFSheet sheet = workbook.createSheet(aClass.getSimpleName() + "-" + i);
                sheet.addMergedRegion(address);
                HSSFCell addressCell = sheet.createRow(address.getFirstColumn())//y
                        .createCell(address.getFirstRow());//x
                addressCell.setCellStyle(cellStyle);
                addressCell.setCellValue(annotation.description());
                //设置行宽
                sheet.setDefaultColumnWidth(18);

                //将含有@ExcelExport的Field的name录入到excel的一行去
                int filedNameRowIndex = address.getLastRow() + 1;
                HSSFRow filedNameRow = sheet.createRow(filedNameRowIndex);
                for (int n = 0; n < filedNames.size(); n++) {
                    HSSFCell cell = filedNameRow.createCell(n);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(filedNames.get(n));
                }
                //todo 在java中，sheet，row，cell与数组一样，都是从0开始的
                //address.getLastRow()+1从合并单元格的下一行开始，后面的+1是，filedName的填充到了一行
                for (int idxRow = address.getLastRow() + 1 + 1; idxRow < maxCreateRow + address.getLastRow() + 1 + 1; idxRow++) {
                    if (listIndex == list.size()) {//list的元素--全部被导入完了
                        log.debug("00-----list的元素全部被录入完-----00");
                        break;
                    }
                    HSSFRow row = sheet.createRow(idxRow);

                    for (int f = 0, count = 0; f < declaredFields.length; f++) {
                        declaredFields[f].setAccessible(true);
                        Object o = declaredFields[f].get(list.get(listIndex));

                        //被ExcelExport标记了，才需要导出
                        if (declaredFields[f].getAnnotation(ExcelExport.class) == null)
                            continue;

                        HSSFCell cell = row.createCell(count);
                        count++;
                        if (o != null) {
                            cell.setCellStyle(cellStyle);
                            cell.setCellValue(o.toString());
                        }
                    }
                    //使用了一个list里面的元素，就要+1
                    listIndex++;
                }
            }
            String property = System.getProperty("user.dir");
            String descPath = property + "\\src\\main\\resources";
            //浏览器下载excel
            buildExcelDocument("abbot.xlsx", workbook, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 浏览器下载excel
     *
     * @param fileName
     * @param wb
     * @param response
     */

    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * todo 这个是用于生成给客户填写信息excel文件
     * 合并单元格占用两列
     *
     * @param aClass
     */
    public static void createEntityUserUseExcel(Class<?> aClass, int sheetCount) {
        try {
            //创建excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //excel的样式
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            //字体居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            Field[] declaredFields = aClass.getDeclaredFields();

            ApiModel annotation = aClass.getAnnotation(ApiModel.class);
            List<String> filedNames = new ArrayList<>();
            /*long excelExportCount*/
            Arrays.stream(declaredFields).
                    skip(8).//跳过REVISION, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME, IS_DELETE, ID
                    forEach(field -> filedNames.add(field.getName())
            );
            int size = filedNames.size();
            //在excel文件中，字母代表X轴cell，数字代表Y轴row，指定合并单元格的区间
            CellRangeAddress address = CellRangeAddress.valueOf("A1:" + (char) ('A' + size - 1) + "2");
            for (int i = 0; i < sheetCount; i++) {
                HSSFSheet sheet = workbook.createSheet(aClass.getSimpleName() + "-" + i);
                sheet.addMergedRegion(address);
                HSSFCell addressCell = sheet.createRow(address.getFirstColumn())//y
                        .createCell(address.getFirstRow());//x
                addressCell.setCellStyle(cellStyle);
                addressCell.setCellValue(annotation.description());
                //设置行宽
                sheet.setDefaultColumnWidth(18);

                //将含有Field的name录入到excel的一行去
                int filedNameRowIndex = address.getLastRow() + 1;
                HSSFRow filedNameRow = sheet.createRow(filedNameRowIndex);
                for (int n = 0; n < filedNames.size(); n++) {
                    HSSFCell cell = filedNameRow.createCell(n);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(filedNames.get(n));
                }
            }
            String property = System.getProperty("user.dir");
            String descPath = property + "\\src\\main\\resources";
            workbook.write(new File(descPath, "demoUserJiShu.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
