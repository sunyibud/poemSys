package com.poemSys.user.service.forum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

/**
 * 敏感词匹配屏蔽
 */
@Slf4j
@Service
public class ContentCheckService
{
    private static String[] banned_words;
    private static final String fileName = "/root/dist/banned_words.txt";
//    private static final String fileName = "www.amberlake.top/banned_word.txt";

    // 定义静态代码块类初始化时执行
    static
    {
        BufferedReader bufferedReader;
        try
        {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            log.info("fileName: "+fileName);
            if (!bufferedReader.ready())
            {
                log.error("文件无法读取......");
            }
            String line;
            StringBuilder lines = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
            {
                byte[] decodedBytes = Base64.getDecoder().decode(line);   // 将文件中的base64编码格式转换
                line = new String(decodedBytes);
                lines.append(line).append("、");
            }
            banned_words = lines.toString().split("、");
            bufferedReader.close();
        } catch (Exception e)
        {
            log.error("违禁词文件读取失败");
//            e.printStackTrace();
        }
    }

    /**
     * @param content 需要进行验证的内容， 内型为字符串
     * @return 若匹配到相关词，则将包含的屏蔽词返回, 若不包含屏蔽词则返回pass
     */
    public String KMPCheckout(String content)
    {
        int flag;
        for (String word : banned_words)
        {
            if (word.length() > 1)
            {
                flag = kmpMatch(content, word);
            } else
            {
                flag = content.indexOf(word);
            }
            if (flag != -1)
            {  //若返回的不是-1，则说明匹配到相关词，内容中包含违禁词
                return word;
            }
        }
        return "pass";
    }

    /**
     * @param content 需要进行验证的内容， 内型为字符串
     * @return 返回替代后的字符串
     */
    public String KMPReplace(String content)
    {
        int flag;
        for (String word : banned_words)
        {
            if (word.length() > 1)
            {
                flag = kmpMatch(content, word);
            } else
            {
                flag = content.indexOf(word);
            }
            if (flag != -1)
            {  //若返回的不是-1，则说明匹配到相关词，内容中包含违禁词
                String temp = ReplaceWord(word);
                content = content.replace(word, temp);
            }
        }
        return content;
    }

    /**
     * @param word 需要添加的词
     * @return true为添加成功， false为有重复的，添加失败
     * @throws IOException io异常
     */
    public boolean AddBannedWord(String word) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        // 判断词是否存在文件中
        boolean isContains = Arrays.asList(banned_words).contains(word);
        if (isContains) return false;
        else
        {
            //写入一个字符串
            word = Base64.getEncoder().encodeToString(word.getBytes());
            writer.write(word + "\n");
            //刷新流
            writer.flush();
            //关闭流
            writer.close();
            // 把新增的屏蔽词添加到静态变量中
            String[] temp = new String[banned_words.length + 1];
            System.arraycopy(banned_words, 0, temp, 0, banned_words.length);
            temp[banned_words.length] = word;
            banned_words = temp;
        }
        return true;
    }

    /**
     * 求出一个字符数组的next数组
     *
     * @param t 字符数组
     * @return next数组
     */
    private static int[] getNextArray(char[] t)
    {
        int[] next = new int[t.length];
        next[0] = -1;
        next[1] = 0;
        int k;
        for (int j = 2; j < t.length; j++)
        {
            k = next[j - 1];
            while (k != -1)
            {
                if (t[j - 1] == t[k])
                {
                    next[j] = k + 1;
                    break;
                } else
                {
                    k = next[k];
                }
                next[j] = 0;  //当k==-1而跳出循环时，next[j] = 0，否则next[j]会在break之前被赋值
            }
        }
        return next;
    }

    /**
     * 对主串s和模式串t进行KMP模式匹配
     *
     * @param s 主串
     * @param t 模式串
     * @return 若匹配成功，返回t在s中的位置（第一个相同字符对应的位置），若匹配失败，返回-1
     */
    private static int kmpMatch(String s, String t)
    {
        char[] s_arr = s.toCharArray();
        char[] t_arr = t.toCharArray();
        int[] next = getNextArray(t_arr);
        int i = 0, j = 0;
        while (i < s_arr.length && j < t_arr.length)
        {
            if (j == -1 || s_arr[i] == t_arr[j])
            {
                i++;
                j++;
            } else
                j = next[j];
        }
        if (j == t_arr.length)
            return i - j;
        else
            return -1;
    }

    /**
     * @param word 需要替换的字符串
     * @return 替换后
     */
    private String ReplaceWord(String word)
    {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < word.length(); i++)
        {
            temp.append("\\*");  // 变成相同长度的*号
        }
        return temp.toString();
    }
}
