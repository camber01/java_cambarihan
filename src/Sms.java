import java.time.LocalDateTime;

public class Sms {
    protected String msisdn = "";
    protected String recipient = "";
    protected String sender = "";
    protected String short_code = "";
    protected String transaction_id = "";
    protected LocalDateTime timestamp;

    public Sms(String msisdn, String recipient, String sender, String short_code, LocalDateTime timestamp){
        this.msisdn = msisdn;
        this.recipient = recipient;
        this.sender = sender;
        this.short_code = short_code;
        this.timestamp = timestamp;
    }

    public String getMsisdn() { return msisdn; }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() { return sender; }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
