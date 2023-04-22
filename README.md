上线网址:
http://sunyi.asia/pomsys


## 第一次提交修改内容为：
### 1. 类变动

#### 1.1 QRcodeImpl 

​	~~**位于(package com.poemSys.user.service.impl)**~~

>+ 包含方法有：`crateQRCode(String content)` content为二维码所包含的内容
>+ 返回类型为**base64**，例如

```
data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAABI0lEQVR42uWWMa6EMAxEB1Gk5Ai5yXKxSEHiYnCTHCElBcJ/nLAroW3tZj9VeBSjOM8OkK/nxE+zCmCQVYLUxCWyB8siuyxRNqQofPVgCWGvufDDGTE4MuCYvVkumGtyY1q/OhYez7Omlkw9YCTDn24YsvYwV67p6bgh07SBrh2YTl35sMhI9kwTjuE+rLkm20TXXr2mxow5oclw8YBC98CaVWjuWA4tYtjgwqiA5rJnshyvXj9rxldgFHqwlndN7VnoG5SFQ6b6sEIFlqg9yUnTfTZmvS8p9a67HFzYfW8Vvbfi8y6zY21OJkiv5L1fY9bmvc4wrd/tswvjtmZ6EC5HRp95PEUPyoO1/4hV2C7pM1+sWbu3Ws8wd4MH+2//rN/sD2sxO18wzT2AAAAAAElFTkSuQmCC
```
### 2. 依赖包变动
```xml
       <!--二维码依赖-->
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>3.3.0</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>3.3.0</version>
        </dependency>
```
### 3. ~~配置文件添加变动~~
```yaml
# 位于spring父级下
qrcode:
    # 生成二维码的类型 png jpg jpeg等
    formatType: "png"
    # 二维码的宽度
    width: 150
    # 二维码的高度
    height: 150
```
***

## 第二次提交修改内容为：

### 1. 类变动

#### 1.1 KMPImpl

**位于：src/main/java/com/poemSys/user/service/forum/KMPImpl.java**

##### 1.1.1 KMPCheckout(String content)

```java
/**
     *
     * @param content  需要进行验证的内容， 内型为字符串
     * @return   若匹配到相关词，则将包含的屏蔽词返回, 若不包含屏蔽词则返回pass
     */
    public String KMPCheckout(String content){
        int flag;
        int number = 1;
        for(String word: banned_words){
            if (word.length()>1) {
                flag = kmpMatch(content, word);
            }else{
                flag = content.indexOf(word);
            }
            if (flag != -1) {  //若返回的不是-1，则说明匹配到相关词，内容中包含违禁词
                return word;
            }
        }
        return "pass";
    }
}
```
##### 1.1.2  AddBannedWord(String word)

```java
/**
     *
     * @param word 需要添加的词
     * @return true为添加成功， false为有重复的，添加失败
     * @throws IOException
     */
    public boolean AddBannedWord(String word) throws IOException {
        BufferedWriter writer=new BufferedWriter(new FileWriter(fileName, true));
        // 判断词是否存在文件中
        boolean isContains = Arrays.asList(banned_words).contains(word);
        if (isContains) return false;
        else {
            //写入一个字符串
            word = Base64.getEncoder().encodeToString(word.getBytes());
            writer.write(word + "\n");
            //刷新流
            writer.flush();
            //关闭流
            writer.close();
            // 把新增的屏蔽词添加到静态变量中
            String[] temp = new String[banned_words.length + 1];
            for (int i = 0; i < banned_words.length; i++) {
                temp[i] = banned_words[i];
            }
            temp[banned_words.length] = word;
            banned_words = temp;
        }
        return true;
    }
```

###### 注意事项

> 通过此方法添加到屏蔽词文件中，KMPImpl类中的静态变量不会立即添加新增的屏蔽词。

##### 1.1.3 **KMPReplace(String content)**

```java
/**
     *
     * @param content   需要进行验证的内容， 内型为字符串
     * @return   返回替代后的字符串
     */
    public String KMPReplace(String content){
        int flag;
        for(String word: banned_words){
            if (word.length() > 1) {
                flag = kmpMatch(content, word);
            }else{
                flag = content.indexOf(word);
            }
            if (flag != -1) {  //若返回的不是-1，则说明匹配到相关词，内容中包含违禁词
                String temp = ReplaceWord(word);
                content = content.replace(word, temp);
            }
        }
        return content;
    }
```

### 2. 文件变动

####  **banned_words.txt**

**位于： src/main/resources/static/files/banned_words.txt**

**说明**

>  屏蔽词以base64编码后存储在此文件中，前端可以通过`AddBannedWord(String word)`方法将屏蔽词输入到文件中。
