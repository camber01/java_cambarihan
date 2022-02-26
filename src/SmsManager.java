import java.sql.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import static database.DatabaseConnect.con;

public class SmsManager implements SmsManagerInterface {

    Sms sms;
    boolean isEmpty;
    ArrayList<Object> sms_result = new ArrayList<>();
    final private static Logger logger = Logger.getLogger(SmsManager.class.getName());

    @Override
    public void insert_sms(Sms sms, boolean status){
        try {
            if (!con.isClosed()) {
                try {
                    PreparedStatement sql_sms = con.prepareStatement(
                            "Insert into sms (msisdn, recipient, sender, short_code, transaction_id, timestamp, status, register) values (?,?,?,?,?,?,?,?)");
                    sql_sms.setString(1, sms.getMsisdn());
                    sql_sms.setString(2, sms.getRecipient());
                    sql_sms.setString(3, sms.getSender());
                    sql_sms.setObject(4, sms.getShort_code());
                    sql_sms.setObject(5, sms.getTransactionId());
                    sql_sms.setObject(6, sms.getTimestamp());
                    sql_sms.setObject(7, status);
                    sql_sms.setObject(8, sms.isRegister());

                    int row = sql_sms.executeUpdate();
                    if (row > 0 && status) {
                        logger.log(Level.INFO, "Sms successfully sent!");
                    } else {
                        logger.log(Level.WARNING, "Failed to send sms!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                logger.log(Level.SEVERE, "Failed to connect to Database!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void sms_checker(Sms sms){
        try {
            String check_promo = "Select * from promos" + " where short_code = " + sms.getShort_code();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(check_promo);

            if (!rs.next()){
                logger.log(Level.INFO, "Invalid Promo Code!");
            }
            else{
                if ( sms.getTimestamp().isAfter(rs.getTimestamp("start_date").toLocalDateTime())
                        && sms.getTimestamp().isBefore(rs.getTimestamp("end_date").toLocalDateTime())) {
                    if (sms.register && sms.sender.equals("system"))
                        logger.log(Level.INFO, sms.recipient + " has been successfully registered.");
                    else
                        logger.log(Level.INFO, "\n Mobile Number: " + sms.getMsisdn() + "\n Message: PROMO CODE ACCEPTED \n Short Code:" + sms.getShort_code());

                    sms.setTransactionId(generateTransactionId());
                    insert_sms(sms, true);
                }
                else{
                    logger.log(Level.INFO, "\n Mobile Number: " + sms.getMsisdn() + "\n Message: PROMO CODE NOT AVAILABLE \n Short Code:" + sms.getShort_code());
                    sms.setTransactionId(generateTransactionId());
                    insert_sms(sms, false);
                }
            }
        }catch (SQLException e){
            logger.log(Level.INFO, "Promo not found!", e);
        }
    }

    public String generateTransactionId(){
        Random random = new Random();
        int id = random.nextInt(1300 * 22);
        String tranId = "TRAN" + id;

        return tranId;
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
                        rs.getString(4), rs.getString(5), rs.getString(6),
                        rs.getTimestamp(7).toLocalDateTime(), rs.getBoolean(8));
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
    public void getSmsByDate() {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms by Start and End date.\n" +
                "\t=============================");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime start_date = LocalDateTime.of(2022, Month.FEBRUARY, 21, 0, 0);
        LocalDateTime end_date = LocalDateTime.of(2022, Month.FEBRUARY, 25, 0, 0);

        isEmpty = true;

        String sql_query = "SELECT * FROM sms WHERE timestamp BETWEEN \"" + start_date.format(format) + "\" and \"" + end_date.format(format) + "\"";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found starting from " + start_date + " to " + end_date);
        }
        else{
            show_result(sms_result);
        }
    }

    @Override
    public void getSmsByPromoCode() {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms by Promo Code.\n" +
                "\t=============================");
        isEmpty = true;
        String promoCode = "PISO PIZZA";

        String sql_query = "select * from sms.sms inner join sms.promos where promo_code = \"" + promoCode +"\" and sms.short_code = promos.short_code;";
        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found with Promo Code \"" + promoCode + "\"");
        }
        else{
            show_result(sms_result);
        }
    }

    @Override
    public void getSmsByMsisdn() {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms by Mobile Number.\n" +
                "\t=============================");
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
    }

    @Override
    public void getSmsSentByTheSystem() {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms sent by the System.\n" +
                "\t=============================");
        isEmpty = true;
        String sql_query = "Select * from sms where sender = \"system\"";

        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found");
        }
        else{
            show_result(sms_result);
        }
    }

    @Override
    public void getSmsReceiveByTheSystem() {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms received by the system.\n" +
                "\t=============================");
        isEmpty = true;
        String sql_query = "Select * from sms where recipient = \"system\"";

        process_sms(sql_query);

        if (isEmpty){
            logger.log(Level.INFO, "No sms found");
        }
        else{
            show_result(sms_result);
        }
    }

    @Override
    public void getSmsByMsisdn(String[] msisdn) {
        logger.log(Level.INFO, "=============================\n" +
                "\tSms by multiple mobile numbers.\n" +
                "\t=============================");
        isEmpty = true;

        for (String mNumber : msisdn) {
            String sql_query = "Select * from sms where msisdn = " + mNumber;
            process_sms(sql_query);

            if (isEmpty) {
                logger.log(Level.INFO, "No sms found with Mobile Number \"" + mNumber + "\"");
            } else {
                show_result(sms_result);
            }
        }
    }
}
