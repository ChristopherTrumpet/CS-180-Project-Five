package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatService  {
    private final AccountService accountService = new AccountService();
    private final StoreService storeService = new StoreService();
    /**
     *
     * list of customer with items from store purchaeed: seller
     * list of products with the number of sales: seller
     * list of stores by number of products sold : buyer
     * list of stores by the products purchased by that particular customer : buyer
     *
     *

     */

    public JSONArray sortProductsBySalesForStore(String sellerId, String storeId) {
        if (!accountService.isBuyer(sellerId)) {
            return null;
        } else {
            JSONObject store = storeService.getStoreById(storeId);
            JSONArray products = store.getJSONArray("products");
            JSONArray sortedJsonArray = new JSONArray();
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < products.length(); i++) {
                jsonValues.add(products.getJSONObject(i));
            }
            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    int soldOne = ((Integer) o1.get("amount_sold"));
                    int soldTwo = ((Integer) o1.get("amount_sold"));
                    if (soldOne > soldTwo) {
                        return 1;
                    } else if (soldOne == soldTwo) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });


//            int n = products.length();
//            for (int i = 0; i < n - 1; i++) {
//                int min_ind = i;
//                for (int j = i + 1; j < n; j++) {
//                    JSONObject productOne = products[j];
//
//                }
//            }
            return sortedJsonArray;
        }

    }

//    public int compareTo(JSONObject productOne, JSONObject productTwo) {
//        int soldOne = (Integer) productOne.get("amount_sold");
//        int soldTwo = (Integer) productTwo.get("amount_sold");
//        if (soldOne > soldTwo) {
//            return 1;
//        } else if (soldOne == soldTwo) {
//            return 0;
//        } else {
//            return -1;
//        }
//    }

    public JSONArray sortCustomersByItemForStore(String sellerId, String storeId) {
        if (!accountService.isBuyer(sellerId)) {
            return null;
        } else {
            JSONObject store = storeService.getStoreById(storeId);
            JSONArray products = store.getJSONArray("buyers");
        }
        return null;
    }
    public int productsBoughtByCustomer(String buyerId, String storeId) {
        if (!accountService.isBuyer(buyerId)) {
            return -1;
        } else {
            JSONObject store = storeService.getStoreById(storeId);
            JSONArray products = store.getJSONArray("products");
            int numberBought = 0;
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < products.length(); i++) {
                jsonValues.add(products.getJSONObject(i));
            }
            for (int i = 0; i < jsonValues.size(); i++) {
                jsonValues.get(i).get("buyers");
            }
        }
        return 0;
    }
}

