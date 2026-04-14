package ac.csg.pu.gui.dashboard.commercial;

import ac.csg.pu.sales.Cart;
import ac.csg.pu.sales.CartItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CartCardController {

    @FXML private Label nameLabel;
    @FXML private Label promotionLabel;
    @FXML private Label quantityLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Button plusButton;
    @FXML private Button minusButton;

    private CartItem item;
    private CartController cartController;

    public void setItem(CartItem item) {
        this.item = item;
        updateCard();
        setupActions();
    }

    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    private void setupActions() {
        plusButton.setOnAction(e -> {
            Cart.incrementProduct(item.getProduct(), item.getPromotion());
            updateCard();
            if (cartController != null) cartController.refreshCartList();
        });

        minusButton.setOnAction(e -> {
            Cart.decrementProduct(item.getProduct(), item.getPromotion());
            updateCard();
            if (cartController != null) cartController.refreshCartList();
        });
    }

    private void updateCard() {
        nameLabel.setText(item.getProduct().getName());
        promotionLabel.setText(item.getPromotion() != null ? "(" + item.getPromotion().getName() + ")" : "");
        quantityLabel.setText(String.valueOf(item.getQuantity()));

        // Append VAT label to the cart total
        String vatText = item.getProduct().isVatExempt() ? " (VAT exempt)" : " (incl. VAT)";
        totalPriceLabel.setText(String.format("£%.2f%s", item.getTotalPrice(), vatText));

        // Hide minus button if quantity is 0
        minusButton.setVisible(item.getQuantity() > 0);
    }
}