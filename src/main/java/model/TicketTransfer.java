package model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;

public class TicketTransfer {
//    @NotEmpty
//    @Size(max = 10, min = 1, message = "Mobile number should be of 10 digits")
//    @Pattern(regexp = "[0-9][0-9]{9}", message = "Mobile number is invalid!!")
    private ArrayList<Integer> ticketIdList = new ArrayList<>();
    private String transferee;
    private String transferor;

    public ArrayList<Integer> getTicketIdList() {
        return ticketIdList;
    }

    public String getTransferee() {
        return transferee;
    }

    public String getTransferor() {
        return transferor;
    }

    public void setTicketIdList(ArrayList<Integer> ticketIdList) {
        this.ticketIdList = ticketIdList;
    }

    public void setTransferee(String transferee) {
        this.transferee = transferee;
    }

    public void setTransferor(String transferor) {
        this.transferor = transferor;
    }
}

