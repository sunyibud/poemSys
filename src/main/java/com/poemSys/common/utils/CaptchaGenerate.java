package com.poemSys.common.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 随机生成验证码
 */
@Service
public class CaptchaGenerate
{

    private final Integer verificationLength = 5;
    // 第一种方法
    public String NumVerification() {
        //1，用随机生成数方法，生成验证码
        //定义一个随机生成数技术，用来生成随机数
        Random random = new Random();
        //定义一个空的String变量用来接收生成的验证码
        String verificationCode = "";
        //循环5次每次生成一位，5位验证码
        for (int i = 0; i < verificationLength; i++) {
            //验证码包括数字、大小写字母组成
            int a = random.nextInt(3);
            switch (a) {
                //a:    0       1       2
                case 0:
                    //      数字   小写字母   大写字母
                    char s = (char) (random.nextInt(26) + 65);
                    verificationCode = verificationCode + s;
                    break;
                case 1:
                    char s1 = (char) (random.nextInt(26) + 97);
                    verificationCode = verificationCode + s1;
                    break;
                case 2:
                    int s2 = random.nextInt(10);
                    verificationCode = verificationCode + s2;
                    break;
            }
        }
        return verificationCode;
    }

    // 第二种方法
    public String LetterNumVerification() {
        //定义一个随机生成数技术，用来生成随机数
        Random random = new Random();
        //2，用String常用API-charAit生成验证码
        //定义一个String变量存放需要的数据，一共58位
        String tempList = "1234567890abcdefghijklmnopqrstuvwxwzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String verificationCode = "";//定义一个空的Atring变量用来接收生成的验证码
        for (int i = 0; i < verificationLength; i++) {
            int a = random.nextInt(58);//随机生成0-57之间的数，提供索引位置
            verificationCode += tempList.charAt(a);//用get 和提供的索引找到相应位置的数据给变量
        }
        return verificationCode;
    }
}
