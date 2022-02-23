import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class SmsManager implements SmsManagerInterface {

    Sms sms;

    final private static Logger logger = Logger.getLogger(SmsManager.class.getName());
    LocalDateTime time = LocalDateTime.now();

    @Override
    public String insert_sms() {
        sms = new Sms("+6391234567843", "Piso Pizza", "Bernabe Cambarihan", "1234551", time);

        try {
            if (!con.isClosed()) {
                Boolean status = sms_checker(sms.short_code, sms.timestamp);

                PreparedStatement sql_sms = con.prepareStatement(
                        "Insert into sms (msisdn, recipient, sender, short_code, timestamp, status) values (?,?,?,?,?,?)");
                sql_sms.setString(1, sms.msisdn);
                sql_sms.setString(2, sms.recipient);
                sql_sms.setString(3, sms.sender);
                sql_sms.setObject(4, sms.short_code);
                sql_sms.setObject(5, sms.timestamp);
                sql_sms.setObject(6, status);

                int row = sql_sms.executeUpdate();
                if (row > 0) {
                    logger.log(Level.INFO, "Sms successfully sent!");
                } else {
                    logger.log(Level.WARNING, "Failed to send sms!");
                }
            }
            else {
                logger.log(Level.SEVERE, "Failed to connect to Database!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Boolean sms_checker(String short_code, LocalDateTime timestamp){
        Boolean status = null;
        try {
            String check_promo = "Select * from promos" + " where short_code = " + short_code;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(check_promo);

            if (!rs.next()){
                status = false;
                logger.log(Level.INFO, "Invalid Promo Code!");
            }
            else{
                if ( timestamp.isAfter(rs.getTimestamp("start_date").toLocalDateTime())
                        && timestamp.isBefore(rs.getTimestamp("end_date").toLocalDateTime())) {
                    logger.log(Level.INFO, "Promo Code Accepted!");
                    status = true;
                }
                else{
                    logger.log(Level.INFO, "Promo not Available!");
                    status = false;
                }
            }
        }catch (SQLException e){
            logger.log(Level.INFO, "Promo not found!", e);
        }
        return status;
    }

    @Override
    public String getSmsByDate() {
        return null;
    }

    @Override
    public String getSmsByPromoCode() {
        return null;
    }

    @Override
    public String getSmsByMsisdn(String msisdn) {
        return null;
    }

    @Override
    public String getSmsBySent() {
        return null;
    }

    @Override
    public String getSmsByReceive() {
        return null;
    }

    @Override
    public String getSmsByMsisdn(String[] msisdn) {
        return null;
    }
}
