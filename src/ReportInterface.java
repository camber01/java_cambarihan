public interface ReportInterface {
    public void FailedTransaction();
    public void FailedSentTransaction();
    public void FailedReceivedTransaction();
    public void SuccessfulTransaction();
    public void SuccessfulSentTransaction();
    public void SuccessfulReceivedTransaction();
    public void PersonRegistered();
    public void SmsReceived();
    public void SmsSent();
}
