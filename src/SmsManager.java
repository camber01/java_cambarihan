import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class SmsManager implements SmsManagerInterface {

    Sms sms;
    final private static Logger logger = Logger.getLogger(SmsManager.class.getName());

    @Override
    public String insert_sms(ArrayList<Object> smsList) {
        try {
            if (!con.isClosed()) {
                for (Object message : smsList) {
                    try {
                        sms = (Sms) message;
                        Boolean status = sms_checker(sms.getRecipient(), sms.getTimestamp());

                        PreparedStatement sql_sms = con.prepareStatement(
                                "Insert into sms (msisdn, recipient, sender, short_code, timestamp, status) values (?,?,?,?,?,?)");
                        sql_sms.setString(1, sms.getMsisdn());
                        sql_sms.setString(2, sms.getRecipient());
                        if (status && sms.getShort_code().equals("REGISTER"))
                            sql_sms.setString(3, registerCustomer());
                        else
                            sql_sms.setString(3, sms.getSender());
                        sql_sms.setObject(4, sms.getShort_code());
                        sql_sms.setObject(5, sms.getTimestamp());
                        sql_sms.setObject(6, status);

                        int row = sql_sms.executeUpdate();
                        if (row > 0 && status) {
                            logger.log(Level.INFO, "Sms successfully sent!");
                        } else {
                            logger.log(Level.WARNING, "Failed to send sms!");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
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

    public Boolean sms_checker(String recipient, LocalDateTime timestamp){
        Boolean status = null;
        try {
            String check_promo = "Select * from promos" + " where short_code = " + recipient;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(check_promo);

            if (!rs.next()){
                status = false;
                logger.log(Level.INFO, "Invalid Promo Code!");
            }
            else{
                if ( timestamp.isAfter(rs.getTimestamp("start_date").toLocalDateTime())
                        && timestamp.isBefore(rs.getTimestamp("end_date").toLocalDateTime())) {
                    logger.log(Level.INFO, "\n Mobile Number: " + sms.getMsisdn() + "\n Message: PROMO CODE ACCEPTED \n Short Code:" + sms.getShort_code());
                    status = true;
                }
                else{
                    logger.log(Level.INFO, "\n Mobile Number: " + sms.getMsisdn() + "\n Message: PROMO CODE NOT AVAILABLE \n Short Code:" + sms.getShort_code());
                    status = false;
                }
            }
        }catch (SQLException e){
            logger.log(Level.INFO, "Promo not found!", e);
        }
        return status;
    }

    private String registerCustomer() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("To complete the promo registration, please send complete name:");
        String customer_name = myObj.nextLine();

        return customer_name;
    }

    @Override
    public String getSmsByDate() {
        Object date1 = 2022-02-24;
/*
        try {
            String check_promo = "Select * from sms where timestamp >= " + date1;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(check_promo);

            while (rs.next()){
                logger.log(Level.INFO, "Mobile Number: " + rs.getString("msisdn") +
                                "\n Recipient: " + rs.getString("recipient") +
                                "\n Sender: " + rs.getString("sender") +
                                "\n Short Code: " + rs.getString("short_code") +
                                "\n Timestamp: " + rs.getTimestamp("timestamp"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

 */
        return null;
    }

    @Override
    public String getSmsByPromoCode() {
        try {
            String sql_query = "Select * from sms where short_code = \"PISO PIZZA\"";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                logger.log(Level.INFO, "Mobile Number: " + rs.getString("msisdn") +
                        "\n Recipient: " + rs.getString("recipient") +
                        "\n Sender: " + rs.getString("sender") +
                        "\n Short Code: " + rs.getString("short_code") +
                        "\n Timestamp: " + rs.getTimestamp("timestamp"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSmsByMsisdn(String msisdn) {
        try {
            String sql_query = "Select * from sms where msisdn = " + msisdn;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                logger.log(Level.INFO, "Mobile Number: " + rs.getString("msisdn") +
                        "\n Recipient: " + rs.getString("recipient") +
                        "\n Sender: " + rs.getString("sender") +
                        "\n Short Code: " + rs.getString("short_code") +
                        "\n Timestamp: " + rs.getTimestamp("timestamp"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
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
