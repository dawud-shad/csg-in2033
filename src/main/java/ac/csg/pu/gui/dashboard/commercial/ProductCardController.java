package ac.csg.pu.gui.dashboard.commercial;

import ac.csg.pu.config.Constants;
import ac.csg.pu.prm.Promotion;
import ac.csg.pu.sales.Cart;
import ac.csg.pu.sales.Product;
import ac.csg.pu.sales.ProductFeed;
import ac.csg.pu.test.TestDataInitializer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductCardController {

    @FXML private ImageView productImage;
    @FXML private Label nameLabel;
    @FXML private Label merchantLabel;
    @FXML private Label originalPriceLabel;
    @FXML private Label discountedPriceLabel;
    @FXML private Button addButton;

    private Product product;
    private Promotion promotion;
    private CartController cartController;

    private final ProductFeed productFeed = TestDataInitializer.getProductFeed();

    public void setProduct(Product product) {
        this.product = product;

        nameLabel.setText(product.getName());

        // Merchant lookup
        String merchantName = productFeed.getMerchant(product.getMerchantId()).getName();
        merchantLabel.setText(merchantName);

        // Apply VAT for display if not exempt
        double displayPrice = product.getPrice();
        if (!product.isVatExempt()) {
            displayPrice *= (1.0 + Constants.VAT_RATE);
        }

        String vatText = product.isVatExempt() ? " (VAT exempt)" : " (incl. VAT)";
        originalPriceLabel.setText(String.format("£%.2f%s", displayPrice, vatText));

        // Placeholder image
        productImage.setImage(new Image(
                getClass().getResource("img/placeholder.png").toExternalForm()
        ));

        setupActions();
    }

    public void setProduct(Product product, Promotion promotion) {
        this.product = product;
        this.promotion = promotion;

        double price = product.getPrice();
        // Base price including VAT if not exempt
        if (!product.isVatExempt()) {
            price *= (1.0 + Constants.VAT_RATE);
        }
        double discount = promotion.getDiscountForProduct(product.getId());
        double newPrice = price * (1 - discount / 100.0);

        String vatText = product.isVatExempt() ? " (VAT exempt)" : " (incl. VAT)";
        originalPriceLabel.setText(String.format("£%.2f%s", price, vatText));

        if (price > newPrice) {
            originalPriceLabel.getStyleClass().setAll("old-price");
            discountedPriceLabel.getStyleClass().setAll("new-price");
            discountedPriceLabel.setText(String.format("£%.2f%s", newPrice, vatText));
            discountedPriceLabel.setVisible(true);
        }

        nameLabel.setText(product.getName());

        // Merchant lookup
        String merchantName = productFeed.getMerchant(product.getMerchantId()).getName();
        merchantLabel.setText(merchantName);

        // Placeholder image
        productImage.setImage(new Image(
                getClass().getResource("img/placeholder.png").toExternalForm()
        ));

        setupActions();
    }

    private void setupActions() {
        addButton.setOnAction(e -> {
            Cart.incrementProduct(product, promotion);

            if (cartController != null) {
                cartController.refreshCartList();
            }
        });
    }

    public void setCartController(CartController cartController) { this.cartController = cartController; }
}