import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class SmsManager implements SmsManagerInterface {

    Sms sms;
    final private static Logger logger = Logger.getLogger(SmsManager.class.getName());
    ArrayList<Object> sms_result = new ArrayList<>();
    Boolean isEmpty;

    @Override
    public void insert_sms(ArrayList<Object> smsList) {
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

    public void show_result(ArrayList<Object> sms_result){
        for (Object result : sms_result) {
            sms = (Sms) result;
            logger.log(Level.INFO, "Mobile Number: " + sms.getMsisdn() +
                    "\n Recipient: " + sms.getRecipient() +
                    "\n Sender: " + sms.getSender() +
                    "\n Short Code: " + sms.getShort_code() +
                    "\n Timestamp: " + sms.getTimestamp());
        }
        sms_result.clear();
    }

    public void process_sms(String sql_query){
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                sms = new Sms(rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getTimestamp(7).toLocalDateTime());
                sms_result.add(sms);
                isEmpty = false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if (statement != null){
                    statement.close();
                }
            }catch (Exception e){
                logger.log(Level.SEVERE, "Error in Closing", e);
            }
        }
    }

    @Override
    public String getSmsByDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start_date = LocalDateTime.of(2022, Month.FEBRUARY, 21, 0, 0);
        LocalDateTime end_date = LocalDateTime.of(2022, Month.FEBRUARY, 23, 0, 0);

        isEmpty = true;

        String sql_query = "SELECT * FROM sms WHERE timestamp BETWEEN \"" + start_date.format(format) + "\" and \"" + end_date.format(format) + "\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found starting from " + start_date + " to " + end_date);
        }
        else{
            show_result(sms_result);
        }
        return null;
    }

    @Override
    public String getSmsByPromoCode() {
        isEmpty = true;
        String short_code = "PISO PIZZA";

        String sql_query = "Select * from sms where short_code = \"" + short_code +"\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found with Short Code \"" + short_code + "\"");
        }
        else{
            show_result(sms_result);
        }
        return null;
    }

    @Override
    public String getSmsByMsisdn() {
        isEmpty = true;
        String msisdn = "+639123456780";
        String sql_query = "Select * from sms where msisdn = " + msisdn;

        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found with Mobile Number \"" + msisdn + "\"");
        }
        else{
            show_result(sms_result);
        }
        return null;
    }

    @Override
    public String getSmsBySent() {
        isEmpty = true;
        String sql_query = "Select * from sms where status = false";

        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found");
        }
        else{
            show_result(sms_result);
        }
        return null;
    }

    @Override
    public String getSmsByReceive() {
        isEmpty = true;
        String sql_query = "Select * from sms where status = true";

        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found");
        }
        else{
            show_result(sms_result);
        }
        return null;
    }

    @Override
    public void getSmsByMsisdn(String[] msisdn) {
        isEmpty = true;

        for (String s : msisdn) {
            String sql_query = "Select * from sms where msisdn = " + s;
            process_sms(sql_query);

            if (isEmpty) {
                logger.log(Level.INFO, "No sms found with Mobile Number \"" + s + "\"");
            } else {
                show_result(sms_result);
            }
        }
    }
}
