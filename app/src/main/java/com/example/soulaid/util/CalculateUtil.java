package com.example.soulaid.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class CalculateUtil {

    private ArrayList<Integer> scores;  //存放原始数据   //layoutPosition从0开始，若规则从1开始，则需要修改scores(amendment(value))再计算
    private HashMap<String,Integer> result;  //存放结果

    //构造方法
    public CalculateUtil(ArrayList<Integer> scores) {
        this.scores = scores;
        result=new HashMap<>();
    }

    public HashMap<String,Integer> calcuByName(String name) {
        for (int i=0;i<scores.size();i++){
            System.out.println(i+":"+(int)scores.get(i));
        }
        switch (name) {
            case "大五人格问卷简式版(NEO-FFI)":
                calcuByNEO_FFI();
                break;
            case "症状自评量表SCL-90":
                calcuBySCL_90();
                break;
            case "人际关系综合诊断量表":
                calcuByIRAS();
                break;
            case "亲密关系体验量表":
                calcuByECR();
                break;
        }
        return result;
    }

    private void amendment(int value){
        for (int i=0;i<scores.size();i++){
            scores.set(i, value+(int)scores.get(i));
        }
    }

    private void reverse(int max, int pos){
        scores.set(pos,max-(int)scores.get(pos));
    }

    private void calcuByNEO_FFI() {
        //分数从1-5
        amendment(1);
        int a,b,c,d,e;
        a=b=c=d=e=0;

        reverse(6,0);
        reverse(6,2);
        reverse(6,8);
        reverse(6,11);
        reverse(6,13);
        reverse(6,14);
        reverse(6,15);
        reverse(6,17);
        reverse(6,18);
        reverse(6,22);
        reverse(6,23);
        reverse(6,26);
        reverse(6,29);
        reverse(6,30);
        reverse(6,32);
        reverse(6,38);
        reverse(6,41);
        reverse(6,44);
        reverse(6,45);
        reverse(6,47);
        reverse(6,53);
        reverse(6,54);
        reverse(6,55);
        reverse(6,58);

        for (int i=0;i<scores.size();i++){
            switch (i%5){               //这里之前有问题 应为switch(i%5)  之前写的是switch(scores.size()/5)
                case 0:
                    a+=(int)scores.get(i);
                    break;
                case 1:
                    b+=(int)scores.get(i);
                    break;
                case 2:
                    c+=(int)scores.get(i);
                    break;
                case 3:
                    d+=(int)scores.get(i);
                    break;
                case 4:
                    e+=(int)scores.get(i);
                    break;
            }
        }

        result.put("神经质",a);
        result.put("外向性",b);
        result.put("开放性",c);
        result.put("宜人性",d);
        result.put("责任心",e);

        print();
    }

    private void calcuBySCL_90() {
        //分数从1-5
        amendment(1);
        int a,b,c, d,e,f, g,h,i, j;
        a=((int)scores.get(0))+
                ((int)scores.get(3))+
                ((int)scores.get(11))+
                ((int)scores.get(26))+
                ((int)scores.get(39))+
                ((int)scores.get(41))+
                ((int)scores.get(47))+
                ((int)scores.get(48))+
                ((int)scores.get(51))+
                ((int)scores.get(52))+
                ((int)scores.get(55))+
                ((int)scores.get(57));
        b= ((int)scores.get(2))+
                ((int)scores.get(8))+
                ((int)scores.get(9))+
                ((int)scores.get(27))+
                ((int)scores.get(37))+
                ((int)scores.get(44))+
                ((int)scores.get(45))+
                ((int)scores.get(50))+
                ((int)scores.get(54))+
                ((int)scores.get(64));
        c= ((int)scores.get(5))+
                ((int)scores.get(20))+
                ((int)scores.get(33))+
                ((int)scores.get(35))+
                ((int)scores.get(36))+
                ((int)scores.get(40))+
                ((int)scores.get(60))+
                ((int)scores.get(68))+
                ((int)scores.get(72));
        d= ((int)scores.get(4))+
                ((int)scores.get(13))+
                ((int)scores.get(14))+
                ((int)scores.get(19))+
                ((int)scores.get(21))+
                ((int)scores.get(25))+
                ((int)scores.get(28))+
                ((int)scores.get(29))+
                ((int)scores.get(30))+
                ((int)scores.get(31))+
                ((int)scores.get(53))+
                ((int)scores.get(70))+
                ((int)scores.get(78));
        e= ((int)scores.get(1))+
                ((int)scores.get(16))+
                ((int)scores.get(22))+
                ((int)scores.get(32))+
                ((int)scores.get(38))+
                ((int)scores.get(56))+
                ((int)scores.get(71))+
                ((int)scores.get(77))+
                ((int)scores.get(79))+
                ((int)scores.get(85));
        f= ((int)scores.get(10))+
                ((int)scores.get(23))+
                ((int)scores.get(62))+
                ((int)scores.get(66))+
                ((int)scores.get(73))+
                ((int)scores.get(80));
        g= ((int)scores.get(12))+
                ((int)scores.get(24))+
                ((int)scores.get(46))+
                ((int)scores.get(49))+
                ((int)scores.get(69))+
                ((int)scores.get(74))+
                ((int)scores.get(81));
        h= ((int)scores.get(7))+
                ((int)scores.get(17))+
                ((int)scores.get(42))+
                ((int)scores.get(67))+
                ((int)scores.get(75))+
                ((int)scores.get(82));
        i= ((int)scores.get(6))+
                ((int)scores.get(15))+
                ((int)scores.get(34))+
                ((int)scores.get(61))+
                ((int)scores.get(76))+
                ((int)scores.get(83))+
                ((int)scores.get(84))+
                ((int)scores.get(86))+
                ((int)scores.get(87))+
                ((int)scores.get(89));
        j= ((int)scores.get(18))+
                ((int)scores.get(43))+
                ((int)scores.get(58))+
                ((int)scores.get(59))+
                ((int)scores.get(63))+
                ((int)scores.get(65))+
                ((int)scores.get(88));
        result.put("躯体化",a);
        result.put("强迫症状",b);
        result.put("人际关系敏感",c);
        result.put("抑郁",d);
        result.put("焦虑",e);
        result.put("敌对",f);
        result.put("恐怖",g);
        result.put("偏执",h);
        result.put("精神病性",i);
        result.put("睡眠及饮食情况",j);

        print();
    }

    private void calcuByIRAS() {
        //分数从1-0
        int a,b,c,d,e;
        a=b=c=d=0;
        for (int i=0;i<scores.size();i++){
            reverse(1,i);
            switch (i%4){
                case 0:
                    a+=scores.get(i);
                    break;
                case 1:
                    b+=scores.get(i);
                    break;
                case 2:
                    c+=scores.get(i);
                    break;
                case 3:
                    d+=scores.get(i);
                    break;
            }
        }
        //e=a+b+c+d;
        result.put("交谈",a);
        result.put("交际",b);
        result.put("待人接物",c);
        result.put("异性交往",d);
        //result.put("总评",e);

        print();
    }

    private void calcuByECR() {
        //分数从1-7
        amendment(1);

        int a,b;
        a=b=0;

        //设置需要反向计分的项  若为1，则记7
        reverse(8,2);
        reverse(8,14);
        reverse(8,21);
        reverse(8,24);
        reverse(8,26);
        reverse(8,28);
        reverse(8,32);
        reverse(8,34);

        for (int i=0;i<scores.size();i++){
            switch (i%2){
                case 0:
                    a+=(int)scores.get(i);
                    break;
                case 1:
                    b+=(int)scores.get(i);
                    break;
            }
        }
        result.put("依恋回避",a);
        result.put("依恋焦虑",b);

        print();
    }

    private void print(){
        Set<String> keySet=result.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()){
            String key = iter.next();
            System.out.println(key+":"+(result.get(key)));
        }
    }
}