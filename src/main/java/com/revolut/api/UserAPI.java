package com.revolut.api;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;
import com.revolut.controller.UserController;
import com.revolut.model.Account;
import com.revolut.model.User;
import com.revolut.utils.JsonUtils;

import static com.revolut.utils.JsonUtils.readJson;

public class UserAPI {

    public static Route list = (Request request, Response response) -> new HashMap<>().put("users", UserController.list());

    public static Route getAccounts = (Request request, Response response) -> {
        Map<String, Iterable<Account>> listResponse = new HashMap<>();
        listResponse.put("accounts", UserController.getAccounts(request.params("id")));
        return listResponse;
    };

    public static Route create = (Request request, Response response) -> {
        String name = readJson(request.body()).path("name").asText("");
        String email = readJson(request.body()).path("email").asText("a");
        return UserController.create(name, email);
    };

    public static Route get = (Request request, Response response) -> {
        User user = UserController.get(request.params(":id"));
        if (user == null) {
            return JsonUtils.EMPTY_RESPONSE;
        }
        return user;
    };

    public static Route delete = (Request request, Response response) -> {

        UserController.delete(request.params(":id"));
        return JsonUtils.EMPTY_RESPONSE;
    };

}
