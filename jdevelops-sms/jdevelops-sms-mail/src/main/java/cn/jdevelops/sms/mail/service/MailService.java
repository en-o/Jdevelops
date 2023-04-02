package cn.jdevelops.sms.mail.service;

import javax.mail.MessagingException;
import java.util.List;

/**
 * 邮件通用操作
 * @author lmz
 * @date 2021/3/29  9:26
 */
public interface MailService {

    /**
     * 发送文本邮件
     *
     * @param to      　　　　　收件人地址
     * @param subject 　　邮件主题
     * @param content 　　邮件内容
     * @param cc      　　　　　抄送地址
     * @author lmz
     */
    void sendSimpleMail(String to, String subject, String content, String... cc);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     * @author lmz
     */
    void sendHtmlMail(String to, String subject, String content, String... cc) throws MessagingException;

    /**
     * 发送带附件的邮件 单个附件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件地址
     * @param cc       抄送地址
     * @throws MessagingException 邮件发送异常
     * @author lmz
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc) throws MessagingException;

    /**
     * description:发送带附件的邮件 多个附件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 附件地址
     * @param cc       抄送地址
     * @throws MessagingException 邮件发送异常
     * @author lmz
     * @date 2021/3/29  10:59
     */
    void sendAttachmentsMail(String to, String subject, String content, List<String> filePath, String... cc) throws MessagingException;

    /**
     * 发送正文中有静态资源的邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param rscPath 静态资源地址
     * @param rscId   静态资源id
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     * @author lmz
     */
    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc) throws MessagingException;

    /**
     * description: 发送正文中有网络资源的文件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param urls    网络资源集合
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     * @author lmz
     * @date 2021/3/29  11:35
     */
    void sendUrlResourceMail(String to, String subject, String content, List<String> urls,
                             String... cc) throws MessagingException;
}
