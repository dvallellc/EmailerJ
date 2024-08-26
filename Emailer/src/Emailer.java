import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Emailer{

    private String _smtpServer;
    private String _username;
    private String _password;

    private int _smtpPort;
    private InternetAddress _sender;
    private Authenticator _authenticator;
    private Properties _properties;

    public Emailer(String smtpServer, int smtpPort, String username, String password)
    {
        _smtpServer = smtpServer;
        _smtpPort = smtpPort;
        _username = username;
        _password = password;

        InstantiateAuthenticator();
    }
    private void InstantiateAuthenticator(){
        try{
            _sender = new InternetAddress(_username);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        _properties = new Properties();
        _properties.put("mail.smtp.auth", true);
        _properties.put("mail.smtp.starttls.enable", "true");
        _properties.put("mail.smtp.host", _smtpServer);
        _properties.put("mail.smtp.port", _smtpPort);
        _authenticator = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(_username, _password);
            }
        };
    }
    public Emailer(String configPath){
        ReadConfigFile(configPath);
    }
    private void ReadConfigFile(String configPath){
        try (Scanner scanner = new Scanner(new File(configPath))) {
            _smtpServer = scanner.nextLine();
            _smtpPort = Integer.parseInt(scanner.nextLine());
            _username = scanner.nextLine();
            _password = scanner.nextLine();
            InstantiateAuthenticator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void SendMail(String recipients, String subject,
                         String messageText , String from) throws MessagingException {

        Session session = Session.getDefaultInstance(_properties, _authenticator);
        session.setDebug(false);

        Message message = new MimeMessage(session);
        message.setFrom(_sender);
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(recipients));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(messageText, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

}
