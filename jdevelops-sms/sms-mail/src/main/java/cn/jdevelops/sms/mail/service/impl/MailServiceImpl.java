package cn.jdevelops.sms.mail.service.impl;

import cn.jdevelops.sms.mail.service.MailService;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * description:java邮件发送实现类
 *
 * @author lmz
 * @date 2021/3/29  9:08
 */
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    public MailServiceImpl(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        if (!ObjectUtils.isEmpty(cc)) {
            message.setCc(cc);
        }
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content, String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, List<String> filePath, String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
        for (String s : filePath) {
            FileSystemResource file = new FileSystemResource(new File(s));
            String fileName = s.substring(s.lastIndexOf(File.separator) + 1);
            helper.addAttachment(fileName, file);
        }
        mailSender.send(message);
    }

    @Override
    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId,
                                 String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);
        mailSender.send(message);
    }

    @Override
    public void sendUrlResourceMail(String to, String subject, String content, List<String> urls,
                                    String... cc) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = buildHelper(to, subject, content, message, cc);
        MimeMultipart mimeMultipart = helper.getMimeMultipart();
        for (String url : urls) {
            try {
                // 向multipart中添加远程附件
                URLDataSource urlDataSource = new URLDataSource(new URL(url));
                BodyPart bodyPart = new MimeBodyPart();
                DataHandler dh;
                dh = new DataHandler(new URLDataSource(new URL(url)));
                bodyPart.setDataHandler(dh);
                bodyPart.setFileName(url.substring(urls.lastIndexOf("/")+1));
                mimeMultipart.addBodyPart(bodyPart);
                mailSender.send(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 统一封装MimeMessageHelper
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param message 消息对象
     * @param cc      抄送地址
     * @return MimeMessageHelper
     * @throws MessagingException 异常
     */
    private MimeMessageHelper buildHelper(String to, String subject, String content, MimeMessage message,
                                          String... cc) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        if (!ObjectUtils.isEmpty(cc)) {
            helper.setCc(cc);
        }
        return helper;
    }
}
