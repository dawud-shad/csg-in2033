package ac.csg.pu.gui.dashboard.admin;

import ac.csg.pu.prm.Promotion;
import ac.csg.pu.prm.PromotionDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PromotionEditController {

    @FXML private TextField nameField;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TextField productIdField;
    @FXML private TextField discountField;
    @FXML private ListView<String> discountListView;
    @FXML private Label errorLabel;

    private Stage popupStage;
    private Promotion promotion;
    private PromotionsController parentController;
    private final Map<Integer, Double> discounts = new HashMap<>();
    private final ObservableList<String> discountDisplay = FXCollections.observableArrayList();
    private boolean isNewPromotion = false;

    @FXML
    public void initialize() {
        discountListView.setItems(discountDisplay);
    }

    public void setup(Promotion promotion, PromotionsController parent, Stage stage) {
        this.promotion = promotion;
        this.parentController = parent;
        this.popupStage = stage;

        if (promotion == null) {
            this.isNewPromotion = true;
            this.promotion = new Promotion(
                    -1,       // temporary ID
                    "",          // default name
                    true,        // active by default
                    LocalDate.now(),
                    LocalDate.now().plusDays(7),
                    new HashMap<>()
            );
        }

        // Pre-populate fields
        nameField.setText(this.promotion.getName());
        startDateField.setText(this.promotion.getStartDate().toString());
        endDateField.setText(this.promotion.getEndDate().toString());

        // Pre-populate discounts
        this.promotion.getDiscounts().forEach((productId, discount) -> {
            discounts.put(productId, discount);
            discountDisplay.add("Product " + productId + " — " + discount + "%");
        });
    }

    @FXML
    private void onUpdateDiscount() {
        String productIdText = productIdField.getText().trim();
        String discountText = discountField.getText().trim();

        if (productIdText.isEmpty() || discountText.isEmpty()) {
            errorLabel.setText("Please enter both product ID and discount.");
            return;
        }

        int productId;
        double discount;
        try {
            productId = Integer.parseInt(productIdText);
            discount = Double.parseDouble(discountText);
            if (discount < 0 || discount > 100) {
                errorLabel.setText("Discount must be between 0 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid product ID or discount value.");
            return;
        }

        // Check if this product already has a discount
        if (discounts.containsKey(productId)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Overwrite Discount?");
            alert.setHeaderText("Product " + productId + " already has a discount: " + discounts.get(productId) + "%");
            alert.setContentText("Do you want to overwrite it with " + discount + "%?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) {
                // User cancelled overwrite
                return;
            }
        }

        // Add/overwrite discount
        discounts.put(productId, discount);

        // Update ListView in-place
        boolean updated = false;
        for (int i = 0; i < discountDisplay.size(); i++) {
            String entry = discountDisplay.get(i);
            if (entry.startsWith("Product " + productId)) {
                discountDisplay.set(i, "Product " + productId + " — " + discount + "%");
                updated = true;
                break;
            }
        }
        if (!updated) {
            discountDisplay.add("Product " + productId + " — " + discount + "%");
        }

        // Clear input fields and error messages
        productIdField.clear();
        discountField.clear();
        errorLabel.setText("");
    }

    @FXML
    private void onSave() {
        String name = nameField.getText().trim();
        String startText = startDateField.getText().trim();
        String endText = endDateField.getText().trim();

        if (name.isEmpty() || startText.isEmpty() || endText.isEmpty()) {
            errorLabel.setText("All fields are required.");
            return;
        }

        LocalDate start, end;
        try {
            start = LocalDate.parse(startText);
            end = LocalDate.parse(endText);
        } catch (DateTimeParseException e) {
            errorLabel.setText("Invalid date format. Use yyyy-MM-dd.");
            return;
        }

        if (!end.isAfter(start)) {
            errorLabel.setText("End date must be after start date.");
            return;
        }

        if (discounts.isEmpty()) {
            errorLabel.setText("Please add at least one product discount.");
            return;
        }

        if (isNewPromotion) {
            // CREATE
            int id = PromotionDatabase.insertPromotion(name, true, start.toString(), end.toString());
            if (id == -1) {
                errorLabel.setText("Failed to create promotion.");
                return;
            }
            promotion.setId(id);
        } else {
            // EDIT
            // Update promotion in DB
            PromotionDatabase.updatePromotion(promotion.getId(), name, start.toString(), end.toString());
        }

        // Update discounts in DB
        discounts.forEach((productId, discount) ->
                PromotionDatabase.insertDiscount(promotion.getId(), productId, discount)
        );

        parentController.onRefresh();
        popupStage.close();
    }

    @FXML
    private void onCancel() {
        popupStage.close();
    }
}