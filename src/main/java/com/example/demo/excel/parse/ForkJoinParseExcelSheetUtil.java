package com.example.demo.excel.parse;

import com.example.demo.entity.EStudent;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * todo 使用Fork-Join去解析Excel文件，以sheet为拆分单位
 */
@Slf4j
public class ForkJoinParseExcelSheetUtil {
    public static void main(String[] args) throws Exception {
        String property = System.getProperty("user.dir");
//        String excelPath = property + "\\src\\main\\resources\\demoUserJiShu.xls";
        String excelPath = property + "\\src\\main\\resources\\demoUserOushu.xls";
        ForkJoinPool pool = new ForkJoinPool();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));
        MyExcelTask innerFind = new MyExcelTask(workbook, 0, workbook.getNumberOfSheets() - 1, EStudent.class);
        long start = System.currentTimeMillis();

        List<Object> invoke = pool.invoke(innerFind);
        System.out.println(invoke);
        System.out.println(invoke.size());
        System.out.println("fork-join spend time:" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(ParseExcelOneThreadUtils.count);
    }


    static class MyExcelTask extends RecursiveTask<List<Object>> {
        //看成数组一样
        private Workbook workbook;
        private int start;
        private int end;
        private final static int THRESHOLD = 1;
        private Class aClass;

        public MyExcelTask(Workbook workbook, int start, int end, Class aClass) {
            this.workbook = workbook;
            this.start = start;
            this.end = end;
            this.aClass = aClass;
        }

        @Override
        protected List<Object> compute() {
            if (end - start <= THRESHOLD) {
                List<Object> objectList = new ArrayList<>();
                for (int i  = start; i <= end; i++) {
                    List<Object> objects = ParseExcelOneThreadUtils.parseSheet(workbook.getSheetAt(i), aClass);
                    objectList.addAll(objects);
                }
                return objectList;
            } else {
                List<Object> objectList = new ArrayList<>();
                //fromIndex....mid.....toIndex
                int mid = (end + start) / 2;
                MyExcelTask leftTask = new MyExcelTask(workbook, start, mid, aClass);
                MyExcelTask rightTask = new MyExcelTask(workbook, mid + 1, end, aClass);
                invokeAll(leftTask, rightTask);
                List<Object> leftJoin = leftTask.join();
                List<Object> rightJoin = rightTask.join();
                objectList.addAll(leftJoin);
                objectList.addAll(rightJoin);
                return objectList;
            }
        }
    }
}
