package ac.csg.pu.prm;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Promotion {

    private int id;
    private String name;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;

    // Map of product ID → discount percentage (0–100)
    private final Map<Integer, Double> discounts;

    public Promotion(int id, String name, boolean active, LocalDate startDate, LocalDate endDate, Map<Integer, Double> discounts) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discounts = discounts;
    }

    // ---- Getters ----
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Map<Integer, Double> getDiscounts() { return discounts; }

    // ---- Setters ----
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    // ---- Discount management ----
    public void addProductDiscount(int productId, double discountPercent) {
        discounts.put(productId, discountPercent);
    }

    public Double getDiscountForProduct(int productId) {
        return discounts.getOrDefault(productId, 0.0);
    }

    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        return active && (startDate == null || !now.isBefore(startDate)) && (endDate == null || !now.isAfter(endDate));
    }
}
