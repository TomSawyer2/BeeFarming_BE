package com.bf.common.util;

public class ConfidenceLevel {
    public static double computeConfidenceLevel(int NumsOfAWin, int NumsOfBWin) {
        double p = tTest(NumsOfAWin, NumsOfBWin);
        double q = tTest(NumsOfBWin, NumsOfAWin);
        if (p > 0) {
            return p * 100;
        } else {
            if (q > 0) {
                return q * 100;
            } else {
                return 0.00;
            }
        }
    }

    /**
     * @param NumsOfAWin A赢的次数
     * @param NumsOfBWin B赢的次数
     * @return
     */
    public static double tTest(int NumsOfAWin, int NumsOfBWin) {
        //H0:A比B厉害，即总体均值\mu大于等于0.5
        //样本均值
        int N = NumsOfAWin + NumsOfBWin;
        double Xbar = (NumsOfAWin) / (double) (NumsOfAWin + NumsOfBWin);
        double[][] ts = new double[][]{
                {1, 3.078, 6.314, 12.706, 31.821, 63.657, 127.321, 318.309, 636.619},
                {0.727, 1.476, 2.015, 2.571, 3.365, 4.032, 4.773, 5.893, 6.869},
                {0.7, 1.372, 1.812, 2.228, 2.764, 3.169, 3.581, 4.144, 4.587},
                {0.687, 1.325, 1.725, 2.086, 2.528, 2.845, 3.153, 3.552, 3.85},
                {0.679, 1.299, 1.676, 2.009, 2.403, 2.678, 2.937, 3.261, 3.496},
                {0.677, 1.29, 1.66, 1.984, 2.364, 2.626, 2.871, 3.174, 3.39},
                {0.676, 1.286, 1.653, 1.972, 2.345, 2.601, 2.839, 3.131, 3.34},
                {0.675, 1.283, 1.648, 1.965, 2.334, 2.586, 2.82, 3.107, 3.31}
        };
        double[] toP = new double[]{0.25, 0.1, 0.05, 0.025, 0.01, 0.005, 0.0025, 0.001, 0.0005};
        int[] toN = new int[]{1, 5, 10, 20, 50, 100, 200, 500};
        double sigma = Math.sqrt(Xbar - Xbar * Xbar) * Math.sqrt((double) (N) / (double) (N + 1));
        double tt = (Xbar - 0.5) / (sigma / Math.sqrt(N));
        int i = 0;
        for (i = 0; N != toN[i] && i < 7; i++) ;
        int j = 8;
        for (j = 8; j > -1 && tt < ts[i][j]; j--) ;
        if (j == -1) {
            return -1;
        } else {
            double p = 1 - toP[j];
            return p;
        }
    }
}
