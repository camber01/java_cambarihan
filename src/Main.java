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

    final private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        DatabaseConnect.connect();
        create_promo();
        smsManager.insert_sms();
        DatabaseConnect.disconnect();
    }

    public static void create_promo(){
        ArrayList<Object> promoList = new ArrayList<>();

        promo = new Promo("PROMO", "Get Pizza for only P1","1234555",
                LocalDateTime.of(2022, Month.FEBRUARY, 21,11, 0, 0 ),
                LocalDateTime.of(2022, Month.MARCH, 22, 11, 0, 0));
        promoList.add(promo);

        promo = new Promo("WALWAL", "Have a 35% off when you get 2 bucket.","1234556",
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
}
