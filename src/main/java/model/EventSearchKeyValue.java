package model;

import utils.Constants;

import javax.validation.constraints.NotNull;

/**
 * stores information provided by user for searching events by category page.
 * It contains following attributes :-
 * searchCategory : Stores search category for event search.
 * searchType     :
 * searchTerm     : Stores the term for which relevant event is searched from database..
 * @author nilimajha
 */
public class EventSearchKeyValue {

    @NotNull (message = Constants.FILED_NOT_NULL_ERROR_MESSAGE)
    private String searchTerm;
    private String searchCategory;
    private String searchType;

    /**
     * Constructor
     */
    public EventSearchKeyValue() {
    }

    /**
     * getter for class attribute searchCategory
     * @return searchCategory
     */
    public String getSearchCategory() {
        return searchCategory;
    }

    /**
     * getter for class attribute searchType
     * @return searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * getter for class attribute searchTerm
     * @return searchTerm
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * setter for class attribute searchCategory
     * @return searchCategory
     */
    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

    /**
     * setter for class attribute searchType
     * @return searchType
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * setter for class attribute searchTerm
     * @return searchTerm
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
