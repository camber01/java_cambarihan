package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnect {
    final private static Logger logger = Logger.getLogger(DatabaseConnect.class.getName());
    private static Connection con = null;

    public static void connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sms?useTimezone=true&serverTimezone=UTC", "root", "password");
            logger.info("Connected");
        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    private static void disconnect() {
        try{
            if(con != null){
                con.close();
                logger.info("Disconnected");
            }
        }catch (Exception e){
            logger.log(Level.SEVERE, "Not Connected", e);
        }
    }

    public static void main(String[] args) {
        DatabaseConnect.connect();
        DatabaseConnect.disconnect();
    }
}
