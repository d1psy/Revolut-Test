package com.revolut;

import com.revolut.api.AccountAPI;
import com.revolut.api.UserAPI;

import static com.revolut.utils.JsonUtils.json;
import static spark.Spark.after;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

public class App {

    private static final int applicationPort = 8080;

    public static void main(String[] args) {
        port(applicationPort);

        after((request, response) -> response.type("application/json"));

        notFound((request, response) -> "{\"error\": \"Requested resource not found.\"}");

        get("/user", UserAPI.list, json());
        post("/user", UserAPI.create, json());
        get("/user/:id", UserAPI.get, json());
        delete("/user/:id", UserAPI.delete, json());
        get("/user/:id/accounts", UserAPI.getAccounts, json());

        get("/account", AccountAPI.list, json());
        post("/account", AccountAPI.create, json());
        get("/account/:id", AccountAPI.get, json());
        delete("/account/:id", AccountAPI.delete, json());
        put( "/account/:from_id/transfer/:to_id", AccountAPI.transfer, json());
    }

}
