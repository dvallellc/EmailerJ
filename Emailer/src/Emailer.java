import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Emailer{

    private String _smtpServer;
    private String _username;
    private String _password;
    private Authenticator _authenticator;
    private Properties _properties;

    public Emailer(String smtpServer, String username, String password)
    {
        _smtpServer = smtpServer;
        _username = username;
        _password = password;
        _properties = new Properties();

        InstantiateAuthenticator();
    }
    private void InstantiateAuthenticator(){
        _properties.put("mail.smtp.host", _smtpServer);
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
            _username = scanner.nextLine();
            _password = scanner.nextLine();
            InstantiateAuthenticator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void postMail(String recipients[], String subject,
                         String message , String from) throws MessagingException {

        //Set the host smtp addres

        // create some properties and get the default Session

        Session session = Session.getDefaultInstance(_properties, _authenticator);
        session.setDebug(false);

        // create a message

        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Optional : You can also set your custom headers in the Email if you Want
        msg.addHeader("MyHeaderName", "myHeaderValue");

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

}
