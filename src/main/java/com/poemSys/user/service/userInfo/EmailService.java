package com.poemSys.user.service.userInfo;


import com.poemSys.common.utils.CaptchaGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 发送邮件工具类 MailUtil
 */
@Slf4j
@Service
public class EmailService{

    //Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CaptchaGenerate captchaGenerate;

    private final Integer verificationLength = 5;

    // 配置文件中我的163邮箱
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * 简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     */
    public void sendSimpleMail(String to, String subject) {
        // 生成验证码
        String verification = captchaGenerate.NumVerification();
/*        String content = "\n" +
                "Hello ["+ to + "]\n" +
                "\n" +
                "You registered an account on [customer portal], before being able to use your account you need to verify that this is your email address by clicking here: [link]\n" +
                "\n" +
                "Kind Regards, [company]";*/
       /* String content = "<h1><a href=\"www.amberlake.top\" style=\"text-decoration-line: none; color: #00C5CD\">古诗文管理系统</a></h1>\n" +
                "\t<p><b style=\"font-size: 25px;\">Hi, Friend, 欢迎来到古诗词网</b></p>\n" +
                "\t<p style=\"font-size: 22px\">以下是<b>5位验证码</b>，请于<b style=\" \">3分钟</b>内在网页填写以通过注册：</p><div style=\"background: #5BAFDE; width: 150px\"><h3 style=\"text-align: center; font-size: 30px; color: white\">" +
                verification +"</h3></div>\n" +
                "\t<p style=\"color:gray; font-size: 15px;\">(若您从未请求发送过验证码，请忽略此邮件。)</p>";*/
        String content = "您的验证码为\n" + verification + "\n请于三分钟内注册。\n若您从未请求发送过验证码，请忽略此邮件。";
        //创建SimpleMailMessage对象
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件发送人
        message.setFrom(from);
        //邮件接收人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        mailSender.send(message);

    }


    /**
     * html邮件
     *
     * @param to      收件人,多个时参数形式 ："xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param subject 主题
     */
    public boolean sendHtmlMail(String to, String subject, String verification) {

        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            String content = "<center><h1><a href=\"http://www.amberlake.top/login\" style=\"text-decoration-line: none; color: #00C5CD\">古诗文网</a></h1>\n" +
                    "\t<p><b style=\"font-size: 25px;\">Hi, Friend, 欢迎来到古诗文网</b></p>\n" +
                    "\t<p style=\"font-size: 18px\">以下是<b style=\" font-size: 22px\">"+ verificationLength  +"位验证码</b>，请于<b style=\"font-size: 22px \">3分钟</b>内在网页填写</p><div style=\"background: #5BAFDE; width: 150px\"><h3 style=\"text-align: center; font-size: 30px; color: white\">" +
                    verification +"</h3></div>\n" +
                    "\t<p style=\"color:gray; font-size: 15px;\">(若您从未请求发送过验证码，请忽略此邮件。)</p></center>";
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人,设置多个收件人地址
            InternetAddress[] internetAddressTo = InternetAddress.parse(to);
            messageHelper.setTo(internetAddressTo);
            //messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            //日志信息
            log.info("邮件已经发送。");
            return true;
        } catch (Exception e) {
            log.error("发送邮件时发生异常！", e);
            return false;
        }
    }

    /**
     * 带附件的邮件
     *
     * @param to       收件人
     * @param subject  主题
     * @param content  内容
     * @param filePath 附件
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            //日志信息
            log.info("邮件已经发送。");
        } catch (Exception e) {
            log.error("发送邮件时发生异常！", e);
        }
    }
}
