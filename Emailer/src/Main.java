import javax.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        Emailer test;
        if(args.length > 0){
            test = new Emailer(args[1]);
        }
        else{

            test = new Emailer("C:\\Users\\dan_v\\OneDrive\\Desktop\\cfg\\emailcreds.txt");

            test.SendMail("dan_valle@outlook.com,dvalle686@hotmail.com", "Test", "Test Message", "dan@danielvalle.net" );
        }
    }
}
