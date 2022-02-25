import java.util.ArrayList;

public interface SmsManagerInterface {

    public void insert_sms(ArrayList<Object> promoList);
    public String getSmsByDate();
    public String getSmsByPromoCode();
    public String getSmsByMsisdn();
    public String getSmsBySent();
    public String getSmsByReceive();
    public void getSmsByMsisdn(String[] msisdn);
}
