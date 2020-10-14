package com.example.demo.excel.parse;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * todo 因为excel文件是自己提供的，所以你懂的
 * todo 坐标绑定--使用properties文件，key对应的field的Name.set(cell.Value)
 * <p>
 * 导入excel文件，就是解析上传的excel文件
 */
@Slf4j
public class ParseExcelOneThreadUtils {
    /*把所有的entity的XXXpositionY.properties文件的内容填充到positionY中去*/
    private final static Properties positionY = new Properties();

    private static String path;

    static {
        path = System.getProperty("user.dir") + "\\src\\main\\resources\\entityProperties";
        try {
            loadPositionYProperties(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPositionYProperties(String path) throws Exception {
        File srcFile = new File(path);
        File[] files = null;
        if (srcFile != null) {
            files = srcFile.listFiles(file -> file.getName().contains("positionY.properties"));
        }
        if (files != null) {
            for (File file : files) {
                FileReader fileReader = new FileReader(file);
                positionY.load(fileReader);
            }
        }
    }

    private static final String xls = "xls";
    private static final String xlsx = "xlsx";
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /*跟构造器的入参的格式有关。
        99.55--->999.5500 55.66--->055.6600   55.66661--->055.6666
     */
    //没有小数点的使用这个
    private final static DecimalFormat decimalFormat = new DecimalFormat("0");
    //有小数点的数字使用这个
    private final static DecimalFormat decimalFormatHavePoint = new DecimalFormat("0");

    public static List<Object> parseExcelFile(MultipartFile file, Class entityClass) {
        return parseExcelFile(file, null, entityClass);
    }

    public static List<Object> parseExcelFile(File file, Class entityClass) {
        return parseExcelFile(null, file, entityClass);
    }

    /**
     * 解析将每一行封装成一个entity对象，由list统一管理
     *
     * @param file
     * @param entityClass
     * @return
     */
    private static List<Object> parseExcelFile(MultipartFile uploadFile, File file, Class entityClass) {
        if (uploadFile != null && entityClass != null) {
            Workbook workbook = null;
            String fileName = uploadFile.getOriginalFilename();
            if (fileName.endsWith(xls)) {
                //2003
                try {
                    workbook = new HSSFWorkbook(uploadFile.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (fileName.endsWith(xlsx)) {
                //2007
                try {
                    workbook = new XSSFWorkbook(uploadFile.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return parseWorkbook(workbook, entityClass);
        } else if (file != null && entityClass != null) {
            Workbook workbook = null;
            String fileName = file.getName();
            if (fileName.endsWith(xls)) {
                //2003
                try {
                    workbook = new HSSFWorkbook(new FileInputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (fileName.endsWith(xlsx)) {
                //2007
                try {
                    workbook = new XSSFWorkbook(new FileInputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return parseWorkbook(workbook, entityClass);
        } else {
            return null;
        }
    }

    private static List<Object> parseWorkbook(Workbook workbook, Class entityClass) {
        int numberOfSheets = workbook.getNumberOfSheets();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheetAt = workbook.getSheetAt(i);
            if (sheetAt != null) {
                List<Object> objects = parseSheet(sheetAt, entityClass);
                list.addAll(objects);
            }
        }
        return list;
    }

    public static int count = 0;

    public static List<Object> parseSheet(Sheet sheet, Class entityClass) {
        if (sheet == null) {
            return null;
        }
        //检测一下fork-join是否使用正确
        log.debug("被解析的excel->sheet是===>{}", sheet.getSheetName());
        count++;
        List<Object> list = new ArrayList<>();
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        //sheet.getMergedRegion(0).getLastRow()获取合并单元格的最后一行 + 1 + 1，
        for (int i = sheet.getMergedRegion(0).getLastRow() + 1 + 1; i < numberOfRows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Object object = parseRow(row, entityClass);
                list.add(object);
            }
        }
        return list;
    }

    /**
     * @param row         将row解析成一个Entity Instance
     * @param entityClass
     * @return
     */
    public static Object parseRow(Row row, Class entityClass) {
        if (row == null)
            return null;
        log.debug("被解析的excel->row所处的sheet是-{},s所在行是===>{}",
                row.getSheet().getSheetName(), row.getRowNum());
//        int numberOfCells = row.getPhysicalNumberOfCells();
        Object instance = null;
        try {
            Constructor constructor = entityClass.getConstructor();
            instance = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //row.getPhysicalNumberOfCells()根据这个就不行，可以读取配置文件student-system.properties去获取
        for (int i = 0; i < /*numberOfCells*/ Arrays.stream(entityClass.getDeclaredFields()).skip(8).count(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                parseCell(cell, entityClass, instance);
            }
        }
        return instance;
    }


    /**
     * @param cell
     * @param entityClass 用于entityClass.getDeclareFiled()
     * @param instance    用于Filed.set(instance,param2)
     */
    private static void parseCell(Cell cell, Class entityClass, Object instance) {
        if (cell == null) {
            return;
        }
        //拿到y轴
        int y = cell.getColumnIndex();
        //凑成positionY.properties文件的key
        String pY = y + "_" + entityClass.getSimpleName();

        if (!positionY.containsKey(pY)) {
            return;
        }
        try {
            //根据坐标配置文件获取到entity对应的Filed，突然发现entityFiledType配置文件，没有意义
            Field declaredField = entityClass.getDeclaredField(positionY.getProperty(pY));
            declaredField.setAccessible(true);
            switch (cell.getCellTypeEnum()) {
                //单元格的格式为文本格式
                case STRING:
                    declaredField.set(instance, cell.getStringCellValue());
                    break;
                //数值(单元格的格式不能是文本格式)
                case NUMERIC:
                    //日期
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //将Date转LocalDateTime
                        Date date = cell.getDateCellValue();
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                        declaredField.set(instance, localDateTime);
                        //除日期的其他数值
                    } else {
                        double numericCellValue = cell.getNumericCellValue();
                        String numberFormat = decimalFormat.format(numericCellValue);
                        declaredField.set(instance, numberFormat);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\gain\\Documents\\Tencent Files\\1839139886\\FileRecv\\student-system\\src\\main\\resources\\demoUserJiShu.xls";
        List<Object> objects = parseExcelFile(new File(filePath), EStudent.class);
        System.out.println(objects);
        System.out.println(Arrays.toString(positionY.keySet().toArray()));
    }*/
}
