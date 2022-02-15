import java.util.Date;

public class Promo {
    protected String promo_code = "";
    protected String details = "";
    protected Integer short_code = 0;
    protected Date start_date;
    protected Date end_date;

    public static void main(String[] args) {

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

    public Integer getShort_code() {
        return short_code;
    }

    public void setShort_code(Integer short_code) {
        this.short_code = short_code;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }
}
