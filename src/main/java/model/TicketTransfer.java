package model;

import validation.TransferTicketIdList;
import validation.TransfereeEmail;
import java.util.ArrayList;

/**
 * stores information provided by user in transfer ticket form page.
 * It contains following attributes :-
 * ticketIdList : Stores list of all the ticketIds selected by user to be transferred.
 * transferee   : EmailId to whom the tickets is to be transferred.
 * transferor   : EmailId of the person who is transferring the tickets.
 * @author nilimajha
 */
public class TicketTransfer {

    @TransferTicketIdList
    private ArrayList<Integer> ticketIdList = new ArrayList<>();

    @TransfereeEmail
    private String transferee;

    private String transferor;

    /**
     * getter for the attribute ticketIdList.
     * @return ticketIdList
     */
    public ArrayList<Integer> getTicketIdList() {
        return ticketIdList;
    }

    /**
     * getter for the attribute transferee.
     * @return transferee
     */
    public String getTransferee() {
        return transferee;
    }

    /**
     * getter for the attribute transferor.
     * @return transferor
     */
    public String getTransferor() {
        return transferor;
    }

    /**
     * setter for the attribute ticketIdList.
     * @param ticketIdList
     */
    public void setTicketIdList(ArrayList<Integer> ticketIdList) {
        this.ticketIdList = ticketIdList;
    }

    /**
     * setter for the attribute transferee.
     * @param transferee
     */
    public void setTransferee(String transferee) {
        this.transferee = transferee;
    }

    /**
     * setter for the attribute transferor.
     * @param transferor
     */
    public void setTransferor(String transferor) {
        this.transferor = transferor;
    }
}

