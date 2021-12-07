package model;

/**
 * stores transaction related data to be stored in the db or extracted from the db.
 * It contains following attributes :-
 * transactionId     : id assigned to the transaction.
 * transactionType   : Type of transaction.
 * userEmailId       : EmailId of the user who made the transaction or to whom the transaction was made.
 * eventId           : EventId associated with the ticket transaction.
 * transactionDate   : Date on which the transaction was done.
 * transactionDetail : One line description of the transaction.
 *
 * @author nilimajha
 */
public class Transaction {

    private int transactionId;
    private String transactionType;
    private String userEmailId;
    private int eventId;
    private String transactionDate;
    private String transactionDetail;

    /**
     * Constructor
     */
    public Transaction() {
    }

    /**
     * Constructor that initialises three attribute of Transaction class object.
     * @param transactionType
     * @param userEmailId
     * @param eventId
     */
    public Transaction(String transactionType, String userEmailId, int eventId) {
        this.transactionType = transactionType;
        this.userEmailId = userEmailId;
        this.eventId = eventId;
    }

    /**
     * Constructor to initialise all attribute except transactionDetail.
     * @param transactionId
     * @param transactionType
     * @param userEmailId
     * @param eventId
     * @param transactionDate
     */
    public Transaction(int transactionId, String transactionType, String userEmailId, int eventId, String transactionDate) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.userEmailId = userEmailId;
        this.eventId = eventId;
        this.transactionDate = transactionDate;
    }

    /**
     * getter for class attribute transactionId
     * @return transactionId
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * getter for class attribute transactionType
     * @return transactionType
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * getter for class attribute userEmailId
     * @return userEmailId
     */
    public String getUserEmailId() {
        return userEmailId;
    }

    /**
     * getter for class attribute eventId
     * @return eventId
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * getter for class attribute transactionDate
     * @return transactionDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * getter for class attribute transactionDetail
     * @return transactionDetail
     */
    public String getTransactionDetail() {
        return transactionDetail;
    }

    /**
     * setter for class attribute transactionId
     * @param transactionId
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * setter for class attribute transactionType
     * @param transactionType
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * setter for class attribute userEmailId
     * @param userEmailId
     */
    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    /**
     * setter for class attribute eventId
     * @param eventId
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * setter for class attribute transactionDate
     * @param transactionDate
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * setter for class attribute transactionDetail
     * @param transactionDetail
     */
    public void setTransactionDetail(String transactionDetail) {
        this.transactionDetail = transactionDetail;
    }
}
