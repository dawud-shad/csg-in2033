package ac.csg.pu.comms;

import ac.csg.pu.comms.model.Response;
import ac.csg.pu.ord.Order;
import ac.csg.pu.ord.OrderDatabase;
import org.eclipse.jetty.util.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class OrderService {
    public static String render(int id) {
        Order order = OrderDatabase.getOrder(id);

        if (order == null) {
            return "<h1>Order not found</h1>";
        }

        try {
            return renderPage(order);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "<h1>Error rendering page</h1>";
        }
    }

    private static String renderPage(Order order) throws IOException, URISyntaxException {
        URL url = OrderService.class.getResource("order/order.html");

        String html = Files.readString(Path.of(url.toURI()));

        StringBuilder itemsHtml = new StringBuilder();
        double total = 0;

        for (ac.csg.pu.ord.OrderItem item : order.getItems()) {
            double lineTotal = item.purchasePrice() * item.quantity();
            total += lineTotal;

            itemsHtml.append("""
            <div class="item">
                <span>%s</span>
                <span>x%d</span>
                <span>£%.2f</span>
            </div>
        """.formatted(
                    item.productName(),
                    item.quantity(),
                    lineTotal
            ));
        }

        html = html
                .replace("{{id}}", String.valueOf(order.getId()))
                .replace("{{status}}", order.getStatus().name())
                .replace("{{items}}", itemsHtml.toString())
                .replace("{{total}}", String.format("%.2f", total))
                .replace("{{address}}", order.getAddress());

        return html;
    }
}
