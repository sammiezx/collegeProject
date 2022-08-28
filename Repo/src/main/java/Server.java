import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.Elastic;
import model.repo.DocumentWrapper;
import model.requestBody.GetDocumentRequest;
import serverClasses.StandardResponse;
import serverClasses.StatusResponse;
import services.RepoController;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        Elastic elastic = new Elastic();
        RepoController repoController = new RepoController(elastic);
        port(8080);

        post("/addDocument", ((request, response) -> {
            response.type("application/json");

            String message = repoController.addDocument(new Gson().fromJson(request.body(), DocumentWrapper.class));

            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, message));
        }));

        get("/search/:keyword", ((request, response) -> {
            response.type("application/json");

//            JsonObject hits = repoController.search(request.params(":keyword"));
            JsonObject hits = repoController.search_1(request.params(":keyword"));

            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, hits));
        }));

        get("/userDocument/:keyword", ((request, response) -> {
            response.type("application/json");

            JsonObject hits = repoController.getDocuments(request.params(":keyword"));

            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, hits));
        }));

        post("/getDocument", ((request, response) -> {
            response.type("application/json");
            JsonObject hits = repoController.getDocument(new Gson().fromJson(request.body(), GetDocumentRequest.class));
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS, hits));

        }));

    }
}
