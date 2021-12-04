package model;

public class Transaction {
    private int transactionId;
    private String transactionType;
    private String userEmailId;
    private int eventId;
    private String transactionDate;
    private String transactionDetail;

    public Transaction() {

    }

    public Transaction(String transactionType, String userEmailId, int eventId) {
        this.transactionType = transactionType;
        this.userEmailId = userEmailId;
        this.eventId = eventId;
    }

    public Transaction(int transactionId, String transactionType, String userEmailId, int eventId, String transactionDate) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.userEmailId = userEmailId;
        this.eventId = eventId;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDetail(String transactionDetail) {
        this.transactionDetail = transactionDetail;
    }
}
