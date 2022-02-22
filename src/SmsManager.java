import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.DatabaseConnect.con;

public class SmsManager implements SmsManagerInterface {
    final private static Logger logger = Logger.getLogger(SmsManager.class.getName());

    @Override
    public String insert_sms(String msisdn, String recipient, String sender, String short_code, String timestamp) {

        return null;
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
