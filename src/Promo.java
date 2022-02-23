import java.time.LocalDateTime;

public class Promo {
    protected String promo_code = "";
    protected String details = "";
    protected String short_code = "";
    protected LocalDateTime start_date;
    protected LocalDateTime end_date;

    public Promo(String promo_code, String details, String short_code, LocalDateTime start_date, LocalDateTime end_date){
        this.promo_code = promo_code;
        this.details = details;
        this.short_code = short_code;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }
}
