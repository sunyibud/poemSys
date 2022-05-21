package com.poemSys.common.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.poemSys.common.bean.Const;
import com.poemSys.common.bean.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 生成返回验证码
 */
@RestController
public class AuthController extends BaseController
{
    @Autowired
    Producer producer;

    @RequestMapping("/api/captcha")
    public Result captcha() throws IOException
    {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());

        boolean isOk = redisUtil.hset(Const.CAPTCHA_KEY, key, code, 60);//验证码有效期为60秒
        if(!isOk)
            return new Result(-1, "服务器异常，redis操作出错", null);
        return new Result(0, "验证码图片获取成功", MapUtil.builder()
                .put("key", key)
                .put("base64Img", base64Img)
                .build());
    }
}
