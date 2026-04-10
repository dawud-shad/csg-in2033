package ac.csg.pu.gui.dashboard.commercial;

import ac.csg.pu.gui.SceneHelper;
import ac.csg.pu.gui.util.ShakeAnimation;
import ac.csg.pu.sales.Cart;
import ac.csg.pu.sales.CartItem;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CheckoutController {

    @FXML private VBox summaryBox;
    @FXML private Label totalLabel;
    @FXML private TextField nameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;
    @FXML private TextField addressField;
    @FXML private Button payButton;
    @FXML private Button cancelButton;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        refreshSummary();

        payButton.setOnAction(e -> processPayment());
        cancelButton.setOnAction(e -> returnHome());
    }

    private void refreshSummary() {
        summaryBox.getChildren().clear();
        for (CartItem item : Cart.getItems().values()) {
            String promoText = item.getPromotion() != null ? " (" + item.getPromotion().getName() + ")" : "";
            Text itemText = new Text(item.getProduct().getName() + promoText + " x" + item.getQuantity() +
                    " - £" + String.format("%.2f", item.getTotalPrice()));
            summaryBox.getChildren().add(itemText);
        }
        totalLabel.setText("Total: £" + String.format("%.2f", Cart.getTotalPrice()));
    }

    @FXML
    private void processPayment() {
        String name = nameField.getText().trim();
        String number = cardNumberField.getText().replaceAll("\\s+", "");
        String expiry = expiryField.getText().trim();
        String cvv = cvvField.getText().trim();
        String address = addressField.getText().trim();

        // Basic client-side validation
        if (name.isEmpty() || number.isEmpty() || expiry.isEmpty() || cvv.isEmpty() || address.isEmpty()) {
            messageLabel.setText("All fields are required.");
            shakeFields();
            return;
        }

        if (!number.matches("\\d{12,19}")) {
            messageLabel.setText("Card number must be numeric (12–19 digits).");
            shakeFields();
            return;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            messageLabel.setText("Expiry must be MM/YY format.");
            shakeFields();
            return;
        }

        if (!cvv.matches("\\d{3,4}")) {
            messageLabel.setText("CVV must be 3 or 4 digits.");
            shakeFields();
            return;
        }

        // TODO: Call real payment API here
        messageLabel.setText("Processing payment...");
        makePayment();

        // Disable inputs to prevent double submissions
        setInputsDisabled(true);
    }

    private void returnHome() {
        SceneHelper.switchScene("home.fxml");
    }

    private void setInputsDisabled(boolean disabled) {
        nameField.setDisable(disabled);
        cardNumberField.setDisable(disabled);
        expiryField.setDisable(disabled);
        cvvField.setDisable(disabled);
        addressField.setDisable(disabled);
        payButton.setDisable(disabled);
        cancelButton.setDisable(disabled);
    }

    private void makePayment() {

    }

    private void shakeFields() {
        ShakeAnimation.shake(nameField);
        ShakeAnimation.shake(cardNumberField);
        ShakeAnimation.shake(expiryField);
        ShakeAnimation.shake(cvvField);
        ShakeAnimation.shake(addressField);
    }
}