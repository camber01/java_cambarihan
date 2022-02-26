package database;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {
    final private static Logger logger = Logger.getLogger(DatabaseConnect.class.getName());
    public static Connection con = null;

    public static void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sms?useTimezone=true&serverTimezone=UTC", "root", "password");
            logger.info("Connected");
        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    public static void disconnect() {
        try{
            if(con != null){
                con.close();
                logger.info("Disconnected");
            }
        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    public static void empty_db(){
        String remove_sms = "DELETE FROM sms";
        String remove_promo = "DELETE FROM promos";
        Statement statement;
        try {
            statement = con.createStatement();
            statement.executeUpdate(remove_sms);
            statement.executeUpdate(remove_promo);

            logger.log(Level.INFO, "Data has been removed!");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
