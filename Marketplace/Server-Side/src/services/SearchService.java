package services;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * SearchService
 * <p>
 * searches JSON files for a query and returns results.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class SearchService {

    public ArrayList<String> search(String searchQuery) {
        ArrayList<String> results = new ArrayList<>();
        ArrayList<String> finalResults = new ArrayList<>();

        StoreService ss = new StoreService();
        AccountService as = new AccountService();

        JSONObject products = as.getJSONFromFile(ss.getProductFileDirectory());
        JSONObject stores = as.getJSONFromFile(ss.getStoreFileDirectory());

        for (Object product : products.getJSONArray("products")) {
            for (Object store : stores.getJSONArray("stores")) {
                for (Object storeProduct : ((JSONObject) store).getJSONArray("products")) {
                    if (((JSONObject) storeProduct).getString("id").equals(((JSONObject) product).getString("product_id"))) {
                        JSONObject productObj = (JSONObject) product;
                        JSONObject storeProductObj = (JSONObject) storeProduct;
                        String name = productObj.getString("name").toLowerCase();
                        String description = productObj.getString("description").toLowerCase();
                        int qty = storeProductObj.getInt("qty");
                        double price = storeProductObj.getDouble("price");

                        searchQuery = searchQuery.toLowerCase();

                        try {
                            if (Integer.parseInt(searchQuery) == qty || Double.parseDouble(searchQuery) == price) {
                                results.add(productObj.getString("name"));
                            }
                        } catch (NumberFormatException e) {
                            // Ignore
                        }

                        if (name.contains(searchQuery) || description.contains(searchQuery)) {
                            results.add(productObj.getString("name"));
                        }
                    }
                }
            }
        }

        if (!results.isEmpty()) {
            finalResults.add(String.valueOf(results.size()));
            finalResults.addAll(results);
            return finalResults;
        }

        return null;

    }

}
