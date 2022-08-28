package services;

import com.google.gson.*;
import database.Elastic;
import model.history.Visit;
import model.history.VisitController;
import model.repo.Document;
import model.repo.DocumentWrapper;
import model.requestBody.GetDocumentRequest;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import java.io.IOException;
import java.util.Collection;

public class RepoController implements RepoServices {
    Elastic elastic;
    VisitController visitController;
    public RepoController(Elastic elastic) {
        this.elastic = elastic;
        visitController = new VisitController(elastic);
    }

    @Override
    public String addDocument(DocumentWrapper documentWrapper) throws IOException {
        Request request = new Request("PUT", "/user/_doc/" + documentWrapper.getDocument().getDocumentID());
        request.setJsonEntity(new Gson().toJson(documentWrapper));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        return jsonResponse;
    }

    @Override
    public JsonObject search(String search) throws IOException {
        String regex = search + ".*";
        String regHead  = "{\"query\":{\"regexp\":{\"document.description\":{\"value\":\"";
        String regTail = "\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000,\"rewrite\":\"constant_score\"}}}}";

        JsonElement json = new JsonParser().parse(regHead + regex + regTail);

        Request request = new Request("GET", "/user/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
        return jsonObject;
    }

    // * str *
    public JsonObject search_1(String search) throws IOException {

        String s1 = "{\"query\":{\"bool\":{\"should\":[{\"wildcard\":{\"document.title\":{\"value\":\"*";
        String s2 = "est";
        String s3 = "*\",\"boost\":2}}},{\"wildcard\":{\"document.description\":{\"value\":\"*";
        String s4 = "est";
        String s5 = "*\",\"boost\":1}}}]}}}";

        String regex = s1 + search + s3 + search + s5;

        JsonElement json = new JsonParser().parse(regex);

        Request request = new Request("GET", "/user/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
        return jsonObject;
    }

    // str * and * str *
    public JsonObject search_2(String search) throws IOException {
        String str1 = "{\"query\":{\"bool\":{\"should\":[{\"wildcard\":{\"document.title\":{\"value\":\"*";
        String str2 = "est";
        String str3 = "*\",\"boost\":2}}},{\"wildcard\":{\"document.description\":{\"value\":\"*";
        String str4 = "est";
        String str5 = "*\",\"boost\":1}}},{\"wildcard\":{\"document.title\":{\"value\":\"";
        String str6 = "est";
        String str7 = "*\",\"boost\":10}}},{\"wildcard\":{\"document.description\":{\"value\":\"";
        String str8 = "est";
        String str9 = "*\",\"boost\":5}}}]}}}";

        String regex = str1 + search + str3 + search + str5 + search + str7 + search + str8;
        JsonElement json = new JsonParser().parse(regex);

        Request request = new Request("GET", "/user/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
        return jsonObject;
    }

    @Override
    public JsonObject getDocuments(String userID) throws IOException {
        String head = "{\"query\":{\"match\":{\"userID\":\"";
        String tail = "\"}}}";
        String jsonString = head + userID + tail;
        JsonElement json = new JsonParser().parse(jsonString);

        Request request = new Request("GET", "/user/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;

        JsonObject jsonObject = jo.getAsJsonObject("hits");
//        JsonArray jsonArray =  jo.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
//        int size = jsonArray.size();
//        ArrayList<JsonElement> array = new ArrayList<JsonElement>();
//        for (int i = 0; i < size; i++) {
//            JsonElement hit = jsonArray.get(i);
//            array.add(hit);
//        }
//        array.forEach(jsonElement -> {
//            System.out.println(jsonElement.getAsJsonObject().get("_source").getAsJsonObject().get("document").getAsJsonObject().get("title"));
//        });
        return jsonObject;
    }

    @Override
    public JsonObject getDocument(GetDocumentRequest getDocumentRequest) throws IOException {
        String head = "{\"query\":{\"match\":{\"document.documentID\":\"";
        String tail = "\"}}}";
        String jsonString = head + getDocumentRequest.documentID + tail;
        JsonElement json = new JsonParser().parse(jsonString);

        Request request = new Request("GET", "/user/_search");
        request.setJsonEntity(new Gson().toJson(json));

        Response response = elastic.restClient.performRequest(request);
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Object obj = new JsonParser().parse(jsonResponse);

        JsonObject jo = (JsonObject) obj;
        JsonObject jsonObject = jo.getAsJsonObject("hits");

        try {
            jsonObject.getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source");
            visitController.addVisit(new Visit(
                    getDocumentRequest.userID,
                    jsonObject.getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonObject("_source").getAsJsonObject("document").get("documentID").getAsString()
            ));
            return jsonObject;
        }catch (Exception e){
            return jsonObject;
        }
    }

    //final API
    @Override
    public Collection<Document> getDocuments() {

        return null;
    }

    //private functions

}
