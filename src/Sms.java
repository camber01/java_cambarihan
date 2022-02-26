import java.time.LocalDateTime;

public class Sms {
    protected String msisdn = "";
    protected String recipient = "";
    protected String sender = "";
    protected String short_code = "";
    protected String transactionId = "";
    protected LocalDateTime timestamp;
    protected boolean register;

    public Sms(String msisdn, String recipient, String sender, String short_code, String transactionId, LocalDateTime timestamp, boolean register){
        this.msisdn = msisdn;
        this.recipient = recipient;
        this.sender = sender;
        this.short_code = short_code;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.register = register;
    }

    public boolean isRegister() { return register; }

    public void setRegister(boolean register) { this.register = register; }

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

    public String getTransactionId() { return transactionId; }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
