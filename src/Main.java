import database.DatabaseConnect;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static database.DatabaseConnect.con;

public class Main {
    static SmsManager smsManager = new SmsManager();
    static Promo promo;
    static Sms sms;
    static GenerateReport generateReport = new GenerateReport();

    final private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        DatabaseConnect.connect();
        DatabaseConnect.empty_db();
        create_promo();

        while(true){
            logger.log(Level.INFO, "Choose from the options:\n" +
                    "1 - Create Sms\n" +
                    "2 - Populate Data\n" +
                    "3 - Show Retrieved Data\n" +
                    "4 - Generate Report\n" +
                    "5 - Exit\n" );

            int choice = Integer.parseInt(myObj.nextLine());
            if(choice == 1){
                create_sms();
            }
            else if(choice == 2){
                populate_data();
            }
            else if(choice == 3){
                show_data();
            }
            else if(choice == 4){
                generate_report();
            }
            else if (choice == 5){
                break;
            }
            else{
                System.out.println("Invalid Input.");
            }
        }
        DatabaseConnect.disconnect();
    }

    private static void show_data() {
        smsManager.getSmsByDate();
        smsManager.getSmsByPromoCode();
        smsManager.getSmsByMsisdn();
        smsManager.getSmsSentByTheSystem();
        smsManager.getSmsReceiveByTheSystem();
        String[] msisdn = {"+6391234567892", "+6391234567898", "+639123456789"};
        smsManager.getSmsByMsisdn(msisdn);
    }

    public static void generate_report(){
        generateReport.FailedTransaction();
        generateReport.FailedSentTransaction();
        generateReport.FailedReceivedTransaction();
        generateReport.SuccessfulTransaction();
        generateReport.SuccessfulSentTransaction();
        generateReport.SuccessfulReceivedTransaction();
        generateReport.PersonRegistered();
        generateReport.SmsReceived();
        generateReport.SmsSent();
    }

    public static void create_sms(){
        Scanner myObj = new Scanner(System.in);
        String msisdn = "";
        String recipient = "system";
        String sender = "";
        String transactionId = "";
        String shortCode = "";
        String response = "";

        logger.log(Level.INFO, "Insert your mobile number: ");
        msisdn = myObj.nextLine();

        logger.log(Level.INFO, "Insert short code: ");
        shortCode = myObj.nextLine();

        logger.log(Level.INFO, "Do you want to proceed with the registration?(yes/no)");
        response = myObj.nextLine();

        if (response.equalsIgnoreCase("yes")){
            sms = new Sms(msisdn, recipient, sender, shortCode, transactionId, LocalDateTime.now(), true);
            smsManager.sms_checker(sms);

            logger.log(Level.INFO, "To complete the promo registration, please send complete name:");
            recipient = myObj.nextLine();
            sender = "system";

            sms = new Sms(msisdn, recipient, sender, shortCode, transactionId, LocalDateTime.now(), true);
            smsManager.sms_checker(sms);
        }
        else{
            sms = new Sms(msisdn, recipient, sender, shortCode, transactionId, LocalDateTime.now(), false);
            smsManager.sms_checker(sms);
        }
    }

    public static void create_promo(){
        promo = new Promo("PISO PIZZA", "Get pizza for Php 1.00","1234555",
                LocalDateTime.of(2022, Month.FEBRUARY, 1,10, 0, 0 ),
                LocalDateTime.of(2022, Month.JUNE, 30, 23, 59, 0));
        insert_promo(promo);

        promo = new Promo("NOM-NOM", "Have a 35% off when you get 2 bucket.","1234556",
                LocalDateTime.of(2022, Month.MARCH, 1,11, 0, 0 ),
                LocalDateTime.of(2022, Month.MARCH, 15, 11, 0, 0));
        insert_promo(promo);

        promo = new Promo("WINGS", "Eat all you can wings for only P199.","1234557",
                LocalDateTime.of(2022, Month.FEBRUARY, 15,11, 0, 0 ),
                LocalDateTime.of(2022, Month.MARCH, 15, 11, 0, 0));
        insert_promo(promo);
    }
    public static void insert_promo(Promo promo){
        try {
            if (!con.isClosed()) {
                PreparedStatement sql_promo = con.prepareStatement(
                        "Insert into promos(promo_code, details, short_code, start_date, end_date) values (?,?,?,?,?)");
                sql_promo.setString(1, promo.getPromo_code());
                sql_promo.setString(2, promo.getDetails());
                sql_promo.setString(3, promo.getShort_code());
                sql_promo.setObject(4, promo.getStart_date());
                sql_promo.setObject(5, promo.getEnd_date());

                int row = sql_promo.executeUpdate();
                if (row > 0) {
                    logger.log(Level.INFO, "Promo successfully created!");
                } else {
                    logger.log(Level.WARNING, "Failed to create promo!");
                }
            }
            else{
                logger.log(Level.SEVERE, "Database connection error!");
            }
        }catch (SQLException e){
            logger.log(Level.INFO, "Promo already exist.");
        }
    }

    private static void populate_data() {
        for(int i = 0; i < 30; i++){
            sms = new Sms("+639123456789" + i, "system", "Jayson Dy", "1234555","", LocalDateTime.now(),  false);
            smsManager.sms_checker(sms);
        }

        for(int i = 0; i < 10; i++){
            sms = new Sms("+639442456784" + i, "system", "Daryl Ong", "1234556","", LocalDateTime.now(), false);
            smsManager.sms_checker(sms);
        }

        for(int i = 0; i < 5; i++){
            sms = new Sms("+639442456780" + i, "system", "Arthur Nery", "1234556","", LocalDateTime.now(), false);
            smsManager.sms_checker(sms);
        }

        for(int i = 0; i < 10; i++){
            sms = new Sms("+639646456781" + i, "system", "Tj Monterde", "1234557","", LocalDateTime.now(), false);
            smsManager.sms_checker(sms);
        }

        for(int i = 0; i < 5; i++){
            sms = new Sms("+639295736787" + i, "system", "Adie", "1234557","", LocalDateTime.now(), false);
            smsManager.sms_checker(sms);
        }
    }
}
