package ac.csg.pu.ord;

import java.time.LocalDate;
import java.util.*;

import static ac.csg.pu.ord.OrderDatabase.*;

public class Order {

    private int id;
    private String email;
    private OrderStatus status;
    private LocalDate date;
    private String address;

    // Map of order item ID -> order item
    private Map<Integer, OrderItem> items = new HashMap<>();;

    // ---- Constructor ----
    Order(int id, String email, OrderStatus status, LocalDate date, String address) {
        this.email = email;
        this.id = id;
        this.status = status;
        this.date = date;
        this.address = address;
    }

    // ---- Getters ----
    public int getId() { return id; }
    public String getEmail() { return email; }
    public Collection<OrderItem> getItems() { return items.values(); }
    public OrderStatus getStatus() { return status; }
    public LocalDate getDate() { return date; }
    public String getAddress() { return address; }

    // ---- Setters ----
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setAddress(String address) { this.address = address; }

    // ---- Item Management ----
    public void addItem(OrderItem item) { this.items.put(-1, item); }
    public void addItems(List<OrderItem> items) { }
    public void clearItems() { this.items.clear(); }
}
