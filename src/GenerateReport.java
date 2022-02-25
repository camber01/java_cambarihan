import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class GenerateReport implements ReportInterface {

    final private static Logger logger = Logger.getLogger(GenerateReport.class.getName());
    Sms sms;
    Boolean isEmpty;
    ArrayList<Object> sms_report =  new ArrayList<>();

    public void process_sms(String sql_query){
        Statement statement = null;

        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                sms = new Sms(rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getTimestamp(7).toLocalDateTime());
                sms_report.add(sms);
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

    public void show_result(){
        for (Object result : sms_report) {
            sms = (Sms) result;
            logger.log(Level.INFO, "--------------------------\n" + "Mobile Number: " + sms.getMsisdn() +
                    "\n Recipient: " + sms.getRecipient() +
                    "\n Sender: " + sms.getSender() +
                    "\n Short Code: " + sms.getShort_code() +
                    "\n Timestamp: " + sms.getTimestamp());
        }
        sms_report.clear();
    }

    @Override
    public String FailedTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Failed Transaction" +
                "\n\t  =================================");
        String sql_query = "Select * from sms where status = false";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No failed transaction found.");
        }
        else{
            show_result();
        }
        return null;
    }

    @Override
    public String FailedSentTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Failed Sent Transaction" +
                "\n\t  =================================");

        String sql_query = "Select * from sms where status = false and short_code != \"REGISTER\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No failed sent transaction found.");
        }
        else{
            show_result();
        }

        return null;
    }

    @Override
    public String FailedReceivedTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Failed Received Transaction" +
                "\n\t  =================================");

        String sql_query = "Select * from sms where status = false and short_code = \"REGISTER\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No failed received transaction found.");
        }
        else{
            show_result();
        }
        return null;
    }

    @Override
    public String SuccessfulTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Successful Transaction" +
                "\n\t  =================================");
        String sql_query = "Select * from sms where status = true";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No successful transaction found.");
        }
        else{
            show_result();
        }
        return null;
    }

    @Override
    public String SuccessfulSentTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Successful Sent Transaction" +
                "\n\t  =================================");
        String sql_query = "Select * from sms where status = true and short_code != \"REGISTER\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No successful sent transaction found.");
        }
        else{
            show_result();
        }
        return null;
    }

    @Override
    public String SuccessfulReceivedTransaction() {
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Successful Received Transaction" +
                "\n\t  =================================");
        String sql_query = "Select * from sms where status = true and short_code = \"REGISTER\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No successful sent transaction found.");
        }
        else{
            show_result();
        }
        return null;
    }

    @Override
    public String PersonRegistered() {
        sms_report.clear();

        Statement statement = null;
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tList of Person Registered" +
                "\n\t  =================================");
        String sql_query = "Select distinct sender from sms where status = true and short_code = \"REGISTER\"";
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                sms_report.add(rs.getString(1));
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

        if (isEmpty){
            logger.log(Level.INFO, "No successful sent transaction found.");
        }
        else{
            for (Object result : sms_report){
                logger.log(Level.INFO, (String) result);
            }
        }
        return null;
    }

    @Override
    public String SmsReceived() {
        sms_report.clear();
        Statement statement =null;
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tTotal Sms Received" +
                "\n\t  =================================");
        String sql_query = "Select count(*) from sms where short_code != \"REGISTER\"";
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                sms_report.add(rs.getString(1));
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

        if (isEmpty){
            logger.log(Level.INFO, "No sms received found.");
        }
        else{
            for (Object result : sms_report){
                logger.log(Level.INFO, (String) result);
            }
        }
        return null;
    }

    @Override
    public String SmsSent() {sms_report.clear();
        Statement statement =null;
        isEmpty = true;

        logger.log(Level.INFO, "=================================\n" +
                "\t\tTotal Sms Sent" +
                "\n\t  =================================");
        String sql_query = "Select count(*) from sms where short_code = \"REGISTER\"";
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql_query);

            while (rs.next()){
                sms_report.add(rs.getString(1));
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

        if (isEmpty){
            logger.log(Level.INFO, "No sms sent found.");
        }
        else{
            for (Object result : sms_report){
                logger.log(Level.INFO, (String) result);
            }
        }
        return null;
    }
}
