package com.revolut.api;

import com.fasterxml.jackson.databind.JsonNode;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.HashMap;
import com.revolut.controller.AccountController;
import com.revolut.error.ErrorResponse;
import com.revolut.model.Account;
import com.revolut.utils.JsonUtils;

import static com.revolut.utils.JsonUtils.readJson;

public class AccountAPI {

    public static Route list = (Request request, Response response) -> new HashMap<>().put("accounts",  AccountController.list());

    public static Route create = (Request request, Response response) -> {
        JsonNode userIdNode = readJson(request.body()).get("userId");
        JsonNode balanceNode = readJson(request.body()).get("balance");

        if (userIdNode.isNull() || balanceNode.isNull()) {
            throw new ErrorResponse("userId or balance is empty", 400);
        }

        if (!balanceNode.isNumber()) {
            throw new ErrorResponse("Balance has to be a number", 400);
        }

        return AccountController.create(userIdNode.asText(), balanceNode.asDouble());
    };

    public static Route get = (Request request, Response response) -> {
        Account account = AccountController.get(request.params(":id"));
        if (account == null) {
            return JsonUtils.EMPTY_RESPONSE;
        }
        return account;
    };

    public static Route transfer = (Request request, Response response) -> {

        JsonNode amountNode = readJson(request.body()).get("amount");
        if (amountNode.isNull() || !amountNode.isNumber()) {
            throw new ErrorResponse("Amount of transfer is missing or not a number", 400);
        }

        AccountController.transfer(request.params(":from_id"), request.params(":to_id"), amountNode.asDouble());
        return JsonUtils.EMPTY_RESPONSE;
    };

    public static Route delete = (Request request, Response response) -> {
        AccountController.delete(request.params(":id"));
        return JsonUtils.EMPTY_RESPONSE;
    };
}