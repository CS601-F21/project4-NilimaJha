package model;

public class Transaction {
    private int transaction_id;
    private char transaction_type;
    private String user_email_id;
    private int event_id;
    private String transaction_date;

    public Transaction(char transaction_type, String user_email_id, int event_id) {
        this.transaction_type = transaction_type;
        this.user_email_id = user_email_id;
        this.event_id = event_id;
    }

    public Transaction(int transaction_id, char transaction_type, String user_email_id, int event_id, String transaction_date) {
        this.transaction_id = transaction_id;
        this.transaction_type = transaction_type;
        this.user_email_id = user_email_id;
        this.event_id = event_id;
        this.transaction_date = transaction_date;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public char getTransaction_type() {
        return transaction_type;
    }

    public String getUser_email_id() {
        return user_email_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setTransaction_type(char transaction_type) {
        this.transaction_type = transaction_type;
    }

    public void setUser_email_id(String user_email_id) {
        this.user_email_id = user_email_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
}
