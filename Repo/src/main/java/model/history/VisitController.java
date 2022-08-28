package model.history;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.Elastic;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.io.IOException;

public class VisitController {
    Elastic elastic;
    public VisitController(Elastic elastic){
        this.elastic = elastic;
    }
    public String addVisit(Visit visit) throws IOException {
        Request request = new Request("PUT", "/repohistory/_doc/" + visit.visitID);
        request.setJsonEntity(new Gson().toJson(visit));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());

        return jsonResponse;
    }

    public JsonObject getVisitByDocument(String documentID) throws IOException {
        String head = "{\"query\":{\"match\":{\"documentID\":\"";
        String tail = "\"}}}";
        String jsonString = head + documentID + tail;
        JsonElement json = new JsonParser().parse(jsonString);

        Request request = new Request("GET", "/repoHistory/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
        return jsonObject;
    }

    public JsonObject getVisitByUser(String userID) throws IOException {
        String head = "{\"query\":{\"match\":{\"userID\":\"";
        String tail = "\"}}}";
        String jsonString = head + userID + tail;
        JsonElement json = new JsonParser().parse(jsonString);

        Request request = new Request("GET", "/repoHistory/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
        return jsonObject;
    }
}
