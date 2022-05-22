## 第一次提交修改内容为：
### 添加的类
>1. QRcodeImpl 位于(package com.poemSys.user.service.impl)
>+ 包含方法有：`crateQRCode(String content)` content为二维码所包含的内容
>+ 返回类型为**base64**，例如
```
data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAAJYAAACWAQAAAAAUekxPAAABI0lEQVR42uWWMa6EMAxEB1Gk5Ai5yXKxSEHiYnCTHCElBcJ/nLAroW3tZj9VeBSjOM8OkK/nxE+zCmCQVYLUxCWyB8siuyxRNqQofPVgCWGvufDDGTE4MuCYvVkumGtyY1q/OhYez7Omlkw9YCTDn24YsvYwV67p6bgh07SBrh2YTl35sMhI9kwTjuE+rLkm20TXXr2mxow5oclw8YBC98CaVWjuWA4tYtjgwqiA5rJnshyvXj9rxldgFHqwlndN7VnoG5SFQ6b6sEIFlqg9yUnTfTZmvS8p9a67HFzYfW8Vvbfi8y6zY21OJkiv5L1fY9bmvc4wrd/tswvjtmZ6EC5HRp95PEUPyoO1/4hV2C7pM1+sWbu3Ws8wd4MH+2//rN/sD2sxO18wzT2AAAAAAElFTkSuQmCC
```
### 依赖包添加
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
### 配置文件添加
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