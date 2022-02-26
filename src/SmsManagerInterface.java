public interface SmsManagerInterface {

    public void insert_sms(Sms sms, boolean status);
    public void getSmsByDate();
    public void getSmsByPromoCode();
    public void getSmsByMsisdn();
    public void getSmsSentByTheSystem();
    public void getSmsReceiveByTheSystem();
    public void getSmsByMsisdn(String[] msisdn);
}
