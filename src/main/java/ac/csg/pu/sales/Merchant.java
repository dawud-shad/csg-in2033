package ac.csg.pu.sales;

public class Merchant {
    int id;
    String name;

    public Merchant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
