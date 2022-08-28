package com.github.wihoho.server;

import static spark.Spark.*;
public class Main {
    ServerController serverController = new ServerController();

    public Main() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        port(8080);

        get("/verify", (request, response) -> {
            String result = "";
            try {
                result = main.serverController.recognize();
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        });

        get("/add/:label", (request, response) -> {
            try {
                main.serverController.append(request.params(":label"));
            } catch (Exception e){
                e.printStackTrace();
                return -1;
            }
            return 1;
        });

        //instead of single user training, train in bulk
    }
}