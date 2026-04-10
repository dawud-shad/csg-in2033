package ac.csg.pu.sales;

import ac.csg.pu.prm.Promotion;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    // Static map: Product ID + Promotion ID -> CartItem
    private static final Map<String, CartItem> items = new HashMap<>();

    private static String key(Product product, Promotion promo) {
        int promoId = promo != null ? promo.getId() : 0;
        return product.getId() + ":" + promoId;
    }

    public static void incrementProduct(Product product, Promotion promo) {
        String k = key(product, promo);
        if (items.containsKey(k)) {
            items.get(k).incrementQuantity();
        } else {
            items.put(k, new CartItem(product, promo));
        }
    }

    public static void decrementProduct(Product product, Promotion promo) {
        String k = key(product, promo);
        if (items.containsKey(k)) {
            CartItem item = items.get(k);
            item.decrementQuantity();
            if (item.getQuantity() <= 0) items.remove(k);
        }
    }

    public static Map<String, CartItem> getItems() {
        return items;
    }

    public static double getTotalPrice() {
        return items.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public static void clear() {
        items.clear();
    }
}