package model.repo;

public class DocumentWrapper {
    private Document document;
    private String userID;

    public DocumentWrapper(Document document, String userID) {
        this.document = document;
        this.userID = userID;
    }

    public Document getDocument() {
        return document;
    }

    public String getUserID() {
        return userID;
    }
}
