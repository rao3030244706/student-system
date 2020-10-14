package com.example.demo.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSumArr {
    private int[] arrTarget;

    public ForkJoinSumArr(int[] arrTarget) {
        this.arrTarget = arrTarget;
    }

    static class SumArr extends RecursiveTask<Integer> {
        private int[] arr;
        private int start;
        private int end;
        private final static int THRESHOLD = 2;


        public SumArr(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                //接受一个任务的结果
                int sum = 0;
                for (int i = start; i <=end; i++) {
                    sum+=arr[i];
                }
                return sum;//返回这个任务的结果
            } else {
                int mid = (end + start) / 2;
                SumArr left = new SumArr(arr, start, mid);
                SumArr right = new SumArr(arr, mid + 1, end);
                invokeAll(left, right);
                int sum = 0;
                try {
                    Integer leftResult = left.get();
                    Integer rightResult = right.get();
                    sum = leftResult + rightResult;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return sum;
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] ints = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
//        int[] ints = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SumArr sumArr = new SumArr(ints, 0, ints.length-1 );
        Integer invoke = pool.invoke(sumArr);
        System.out.println(invoke);
    }
}
