import java.util.ArrayList;

public interface SmsManagerInterface {

    public String insert_sms(ArrayList<Object> promoList);
    public String getSmsByDate();
    public String getSmsByPromoCode();
    public String getSmsByMsisdn(String msisdn);
    public String getSmsBySent();
    public String getSmsByReceive();
    public String getSmsByMsisdn(String[] msisdn);
}
