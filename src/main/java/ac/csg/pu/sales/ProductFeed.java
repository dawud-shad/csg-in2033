package ac.csg.pu.sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.*;

public class ProductFeed {

    private final Map<Integer, Product> productsById = new HashMap<>(); // quick lookup by product ID
    private final Map<Integer, Merchant> merchantsById = new HashMap<>(); // quick lookup by merchant ID

    private final ObservableList<Product> allProducts = FXCollections.observableArrayList(); // all products
    private final FilteredList<Product> filteredProducts = new FilteredList<>(allProducts); // filtered view for UI

    // Add a merchant
    public void addMerchant(Merchant merchant) {
        merchantsById.put(merchant.getId(), merchant);
    }

    // Add a product
    public void addProduct(Product product) {
        if (!productsById.containsKey(product.getId())) {
            productsById.put(product.getId(), product);
            allProducts.add(product);
        }
    }

    // Get products by merchant
    public List<Product> getProductsByMerchant(int merchantId) {
        List<Product> list = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getMerchantId() == merchantId) {
                list.add(p);
            }
        }
        return list;
    }

    // Filter products for the UI
    public void filterProducts(java.util.function.Predicate<Product> predicate) {
        filteredProducts.setPredicate(predicate);
    }

    // Get the filtered list for binding to ListView
    public ObservableList<Product> getFilteredProducts() {
        return filteredProducts;
    }

    // Optional: get Merchant by ID
    public Merchant getMerchant(int merchantId) {
        return merchantsById.get(merchantId);
    }
}