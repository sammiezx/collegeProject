package model.history;

import java.time.LocalDateTime;

public class Visit {
    public String visitID;
    public String userID;
    public String documentID;
    public LocalDateTime created;
    public Visit(String userID, String documentID){
        created = LocalDateTime.now();
        this.documentID = documentID;
        this.userID = userID;
        visitID = userID + "_" + documentID;
    }
}
