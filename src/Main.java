import database.DatabaseConnect;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class Main {
    static SmsManager smsManager = new SmsManager();
    static Promo promo;
    static Sms sms;

    final private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        DatabaseConnect.connect();
        //DatabaseConnect.empty_db();
        //create_promo();
        //create_sms();
        //populate_data();
        show_data();
        DatabaseConnect.disconnect();
    }

    private static void show_data() {
        //smsManager.getSmsByDate();
        //smsManager.getSmsByPromoCode();
        //smsManager.getSmsByMsisdn();
        String[] msisdn = {"+6391234567843", "+6391234564567", "+6391234564569"};
        //smsManager.getSmsByMsisdn(msisdn);
        //smsManager.getSmsBySent();
        smsManager.getSmsByReceive();
    }

    public static void create_sms(){
        ArrayList<Object> smsList = new ArrayList<>();

        sms = new Sms("+6391234567843", "1234556", "", "REGISTER", LocalDateTime.now());
        smsList.add(sms);

        sms = new Sms("+6391234564567", "1234559", "", "REGISTER", LocalDateTime.now());
        smsList.add(sms);

        sms = new Sms("+6391234564569", "1234559", "", "REGISTER", LocalDateTime.now());
        smsList.add(sms);

        sms = new Sms("+6391234569876", "1234555", "", "PROMO", LocalDateTime.now());
        smsList.add(sms);

        smsManager.insert_sms(smsList);
        smsList.clear();
    }

    public static void create_promo(){
        ArrayList<Object> promoList = new ArrayList<>();

        promo = new Promo("PISO PIZZA", "Get pizza for Php 1.00","1234555",
                LocalDateTime.of(2022, Month.FEBRUARY, 1,10, 0, 0 ),
                LocalDateTime.of(2022, Month.JUNE, 30, 23, 59, 0));
        promoList.add(promo);

        promo = new Promo("NOM-NOM", "Have a 35% off when you get 2 bucket.","1234556",
                LocalDateTime.of(2022, Month.MARCH, 1,11, 0, 0 ),
                LocalDateTime.of(2022, Month.MARCH, 15, 11, 0, 0));
        promoList.add(promo);

        promo = new Promo("WINGS", "Eat all you can wings for only P199.","1234559",
                LocalDateTime.of(2022, Month.FEBRUARY, 15,11, 0, 0 ),
                LocalDateTime.of(2022, Month.MARCH, 15, 11, 0, 0));
        promoList.add(promo);

        insert_promo(promoList);
    }
    public static void insert_promo(ArrayList<Object> promoList){
        try {
            if (!con.isClosed()) {
                for (Object o : promoList) {
                    try {
                        promo = (Promo) o;

                        PreparedStatement sql_promo = con.prepareStatement("Insert into promos(promo_code, details, short_code, start_date, end_date) values (?,?,?,?,?)");
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
                    }catch (SQLException e){
                        logger.log(Level.SEVERE,"Promo already exist!");
                    }
                }
            }
            else{
                logger.log(Level.SEVERE, "Database connection error!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void populate_data() {
        ArrayList<Object> smsList = new ArrayList<>();

        for(int i = 0; i < 30; i++){
            sms = new Sms("+63912345678" + i, "1234555", "Jayson Dy", "PISO PIZZA", LocalDateTime.now());
            smsList.add(sms);
        }
        smsManager.insert_sms(smsList);
        smsList.clear();

        for(int i = 0; i < 10; i++){
            sms = new Sms("+63544245678" + i, "1234556", "Daryl Ong", "NOM-NOM", LocalDateTime.now());
            smsList.add(sms);
        }
        smsManager.insert_sms(smsList);
        smsList.clear();

        for(int i = 0; i < 5; i++){
            sms = new Sms("+63544245678" + i, "1234556", "", "REGISTER", LocalDateTime.now());
            smsList.add(sms);
        }
        smsManager.insert_sms(smsList);
        smsList.clear();

        for(int i = 0; i < 10; i++){
            sms = new Sms("+63764645678" + i, "1234559", "Tj Monterde", "WINGS", LocalDateTime.now());
            smsList.add(sms);
        }
        smsManager.insert_sms(smsList);
        smsList.clear();

        for(int i = 0; i < 5; i++){
            sms = new Sms("+63729573678" + i, "1234559", "", "REGISTER", LocalDateTime.now());
            smsList.add(sms);
        }
        smsManager.insert_sms(smsList);
        smsList.clear();
    }
}
