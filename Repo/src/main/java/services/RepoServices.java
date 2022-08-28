package services;

import com.google.gson.JsonObject;
import model.repo.Document;
import model.repo.DocumentWrapper;
import model.requestBody.GetDocumentRequest;

import java.io.IOException;
import java.util.Collection;

public interface RepoServices {
    public String addDocument(DocumentWrapper documentWrapper) throws IOException;
    public JsonObject search(String search) throws IOException;
    public JsonObject getDocuments(String userID) throws IOException;
    public JsonObject getDocument(GetDocumentRequest getDocumentRequest) throws IOException;

    public Collection<Document> getDocuments();
}

