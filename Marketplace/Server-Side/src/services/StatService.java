package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class StatService {
    private final AccountService accountService = new AccountService();

    /**
     *
     * list of customer with items from store purchaeed: seller
     * list of products with the number of sales: seller
     * list of stores by number of products sold : buyer
     * list of stores by the products purchased by that particular customer : buyer
     *
     *

     */

}
