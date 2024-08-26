public class Main {
    public static void main(String[] args){
        Emailer test;
        if(args.length > 0){
            test = new Emailer(args[1]);
        }
        else{

            test = new Emailer("C:\\Users\\dan_v\\OneDrive\\Desktop\\cfg\\config.txt");
        }
    }
}
