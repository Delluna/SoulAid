package com.example.soulaid.util;

import java.util.ArrayList;

public class CalculateUtil {
    private ArrayList scores;

    public CalculateUtil(ArrayList scores) {
        this.scores = scores;
    }

    public int calcuByName(String name) {
        int result = 0;
        switch (name) {
            case "大五人格问卷简式版(NEO-FFI)":
                result = calucByNEO_FFI();
                break;
            case "症状自评量表SCL-90":
                result = calucBySCL_90();
                break;
            case "人际关系综合诊断量表":
                result = calucByIRAS();
                break;
            case "亲密关系体验量表":
                result = calucByECR();
                break;
        }
        return result;
    }

    private int calucByNEO_FFI() {
        int result = 0;

        for (int i = 0; i < scores.size(); i++) {
            result += (int) scores.get(i);
        }
        return result;

    }

    private int calucBySCL_90() {
        int result = 0;
        for (int i = 0; i < scores.size(); i++) {
            result += (int) scores.get(i);
        }
        return result;
    }

    private int calucByIRAS() {
        int result = 0;
        for (int i = 0; i < scores.size(); i++) {
            result += (int) scores.get(i);
        }
        return result;
    }

    private int calucByECR() {
        int result = 0;
        for (int i = 0; i < scores.size(); i++) {
            result += (int) scores.get(i);
        }
        return result;
    }
}