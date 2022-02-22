public interface SmsManagerInterface {

    String insert_sms(String msisdn, String recipient, String sender, String short_code, String timestamp);

    public String getSmsByDate();
    public String getSmsByPromoCode();
    public String getSmsByMsisdn(String msisdn);
    public String getSmsBySent();
    public String getSmsByReceive();
    public String getSmsByMsisdn(String[] msisdn);
}
