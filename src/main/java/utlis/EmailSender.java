package utlis;

import exceptions.EmailSendException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import utlis.jdbc.PropertiesUtil;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
@Slf4j
public class EmailSender {

    private static final String COMPANY_EMAIL = PropertiesUtil.getProperty("smtp.email");
    private static final String PASSWORD = PropertiesUtil.getProperty("smtp.password");
    private static final String SUBJECT = "Please confirm your email address";
    private static final String BODY = "To confirm your email, go to link below:\n%s";
    private static final String smtpPort = PropertiesUtil.getProperty("smtp.port");
    private static final String smtpHost = PropertiesUtil.getProperty("smtp.host");


    public static void sendPureEmail(Session session, String toEmail, String link) throws EmailSendException {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=%s".formatted(UTF_8.name()));
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(COMPANY_EMAIL, "Our Company"));
            msg.setReplyTo(InternetAddress.parse(COMPANY_EMAIL, false));
            msg.setSubject(SUBJECT, UTF_8.name());
            msg.setText(BODY.formatted(link), UTF_8.name());
            msg.setSentDate(Date.valueOf(LocalDate.now()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            log.info("Message to {} email ready", toEmail);
            Transport.send(msg);
            log.info("Message to {} email send successfully", toEmail);
        } catch (Exception e) {
            log.error("Message to {} email didnt sent", toEmail);
            throw new EmailSendException(e, toEmail);
        }
    }

    public static void sendTLSEmail(Properties properties, String toEmail, String link) throws EmailSendException {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(COMPANY_EMAIL, PASSWORD);
            }
        };
        Session session = Session.getInstance(properties, authenticator);
        sendPureEmail(session, toEmail, link);
    }


    public static Properties defaultTLSProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", Boolean.TRUE.toString());
        properties.put("mail.smtp.starttls.enable", Boolean.TRUE.toString());
        return properties;
    }

}
