package com.example.demo.excel.parse;

import com.example.demo.entity.EStudent;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * todo 使用Fork-Join去解析Excel文件，以row为拆分单位
 * todo row是属于sheet的    我这里只能做到一个sheet对应一个pool.invoke(MyExcelTask);而MyExcelTask由这个解析每一个sheet
 * todo 只拆分解析一个sheet的所有row就很简单了，多个的话就麻烦，麻烦在于任务的拆分
 */
@Slf4j
public class ForkJoinParseExcelRowUtil {
    public static void main(String[] args) throws Exception {
        /*todo 使用多线程去解析Excel文件*/
        String property = System.getProperty("user.dir");
        String excelPath = property + "\\src\\main\\resources\\demoUserOuShu.xls";
        ForkJoinPool pool = new ForkJoinPool();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(excelPath));

        MyExcelTask innerFind =
                new MyExcelTask(workbook, 0, 0, workbook.getSheetAt(0).getPhysicalNumberOfRows() - 1, EStudent.class);
        long start = System.currentTimeMillis();
        List<Object> invoke = pool.invoke(innerFind);
        System.out.println(invoke);
        System.out.println("size----" + invoke.size());
        System.out.println("fork-join spend time:" + (System.currentTimeMillis() - start) + "ms");

        /*todo 使用单线程去解析excel文件*/
       /* System.out.println("---------0000000---------");
        long start1 = System.currentTimeMillis();
        HSSFWorkbook workbook1 = new HSSFWorkbook(new FileInputStream(excelPath));
        for (int i = 0; i < workbook1.getNumberOfSheets(); i++) {
            System.out.println(ParseExcelOneThreadUtils.parseSheet(workbook1.getSheetAt(i), EStudent.class));
        }
        System.out.println("main spend time:" + (System.currentTimeMillis() - start1) + "ms");
*/

    }


    static class MyExcelTask extends RecursiveTask<List<Object>> {
        private Workbook workbook;
        private int sheetAt;
        private int start;
        private int end;
        private final static int THRESHOLD = 2;
        private Class aClass;


        /**
         * @param workbook 要接的excel文件
         * @param sheetAt  要解析的sheet所处的位置
         * @param start    开始解析的row，我这里跳过的第一个(index=0)的合并单元格+1+1 开始的，即有效数据行
         * @param end      这个sheetAt所对应的有效row的最大数，跟数组一样-1
         * @param aClass
         */
        public MyExcelTask(Workbook workbook, int sheetAt, int start, int end, Class aClass) {
            this.workbook = workbook;
            this.sheetAt = sheetAt;
            this.start = start;
            this.end = end;
            this.aClass = aClass;
        }

        @Override
        protected List<Object> compute() {
            if (end - start <= THRESHOLD) {
                List<Object> objectList = new ArrayList<>();
                for (int i = start; i <= end; i++) {
                    Sheet sheetAt = workbook.getSheetAt(this.sheetAt);
                    //先拿到每一个合并单元格的
                    CellRangeAddress mergedRegion = sheetAt.getMergedRegion(0);
                    //从数据行开始
                    int lastRow = mergedRegion.getLastRow() + 1 + 1;
                    Object o = ParseExcelOneThreadUtils.parseRow(sheetAt.getRow(lastRow + i), aClass);
                    if (o != null)
                        objectList.add(o);
                }
                return objectList;
            } else {
                List<Object> objectList = new ArrayList<>();
                //fromIndex....mid.....toIndex
                int mid = (end + start) / 2;
                MyExcelTask leftTask = new MyExcelTask(workbook, sheetAt, start, mid, aClass);
                MyExcelTask rightTask = new MyExcelTask(workbook, sheetAt, mid + 1, end, aClass);
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
