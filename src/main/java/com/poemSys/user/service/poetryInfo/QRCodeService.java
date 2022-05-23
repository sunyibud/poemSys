package com.poemSys.user.service.poetryInfo;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 生成古诗词俩链接二维码
 */
public class QRCodeService
{
    /**
     * 不带log的二维码，以base64字节流传输
     *
     * @param content 生成二维码的所包含的内容
     */
    public String crateQRCode(String content) throws IOException, WriterException
    {
        int width = 150;
        int height = 150;
        String formatType = "png";

        String resultImage;
        if (!StringUtils.isBlank(content))
        {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距

            QRCodeWriter writer = new QRCodeWriter();
            System.out.println("width:" + width + ",height:" + height);
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, formatType, os);

            resultImage = "data:image/jpeg;base64," + Base64.encode(os.toByteArray());
            return resultImage;

        }
        return null;
    }
}
