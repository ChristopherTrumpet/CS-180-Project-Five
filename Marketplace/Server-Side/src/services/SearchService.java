package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchService {
    public ArrayList<String> searchProductList(JSONArray productList, String searchQuery) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < productList.length(); i++) {
            JSONObject productItem = (JSONObject) productList.get(i);
            if (productItem.getString("name").toLowerCase().contains(searchQuery)) {
                ids.add(productItem.getString("id"));
            } else if (productItem.getString("description").toLowerCase().contains(searchQuery)) {
                ids.add(productItem.getString("id"));
            }
        }
        return ids;
    }

    public ArrayList<String> search(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        AccountService as = new AccountService();
        JSONObject products = new JSONObject(as.getJSONFile("data/products.json"));
        ArrayList<String> ids = new ArrayList<>(searchProductList(products.getJSONArray("products"), searchQuery));
        JSONArray storeList = (new JSONObject(as.getJSONFile("data/stores.json"))).getJSONArray("stores");
        for (int i = 0; i < storeList.length(); i++) {
            JSONArray storeProductList = storeList.getJSONObject(i).getJSONArray("products");
            for (int j = 0; j < storeProductList.length(); j++) {
                JSONObject product = storeProductList.getJSONObject(i);
                String productId = (String) product.get("id");
                if (ids.contains(productId)) {
                    ids.add(storeList.getJSONObject(i).getString("store_id"));
                    break;
                }
            }
        }
        return ids;
    }

}
