import database.DatabaseConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class Main {
    static DatabaseConnect connect = new DatabaseConnect();

    final private static Logger logger = Logger.getLogger(Main.class.getName());
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

    public static void main(String[] args) {
        connect.connect();
        create_promo();
        connect.disconnect();
    }

    public static void create_promo(){
        LocalDateTime time = LocalDateTime.now();
        Promo promo = new Promo("Piso Pizza", "This is a sample details for the piso pizza","1234555",
                                time.of(2022, Month.FEBRUARY, 22,11, 00, 00 ),
                                time.of(2022, Month.MARCH, 22, 11, 00, 00));
        try {
            if (!con.isClosed()) {
                PreparedStatement sql_promo = con.prepareStatement("Insert into promos(promo_code, details, short_code, start_date, end_date) values (?,?,?,?,?)");
                sql_promo.setString(1, promo.promo_code);
                sql_promo.setString(2, promo.details);
                sql_promo.setString(3, promo.short_code);
                sql_promo.setObject(4, promo.start_date);
                sql_promo.setObject(5, promo.end_date);

                int row = sql_promo.executeUpdate();
                if (row > 0) {
                    logger.log(Level.INFO, "Promo successfully created!");
                } else {
                    logger.log(Level.WARNING, "Failed to create promo!");
                }
            }
            else{
                logger.log(Level.SEVERE, "Failed to connect to Database!");
            }

        }catch (SQLException e){
            logger.log(Level.SEVERE,"Error creating Promo");
        }

    }
}
