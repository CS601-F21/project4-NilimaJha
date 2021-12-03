package model;

public class EventSearchKeyValue {
    String searchCategory;
    String searchType;
    String searchTerm;

    public EventSearchKeyValue() {
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}