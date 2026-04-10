package ac.csg.pu.gui.auth;

import ac.csg.pu.gui.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private VBox commercialFields;

    @FXML private TextField companyNameField;
    @FXML private TextField companyAddressField;
    @FXML private TextField companyRegIdField;

    @FXML private RadioButton nonCommercialRadio;
    @FXML private RadioButton commercialRadio;

    @FXML private Label errorLabel;

    @FXML private ToggleGroup accountTypeGroup;

    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        accountTypeGroup = new ToggleGroup();
        nonCommercialRadio.setToggleGroup(accountTypeGroup);
        commercialRadio.setToggleGroup(accountTypeGroup);

        // Show/hide commercial fields
        accountTypeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == commercialRadio) {
                commercialFields.setVisible(true);
                commercialFields.setManaged(true);
            } else {
                commercialFields.setVisible(false);
                commercialFields.setManaged(false);
            }
        });

        registerButton.setDefaultButton(true);
    }

    @FXML
    private void onRegister() {
        String email = emailField.getText();
        boolean isCommercial = ((RadioButton)accountTypeGroup.getSelectedToggle()).getText().equals("Commercial");

        String companyName = companyNameField.getText();
        String companyAddress = companyAddressField.getText();
        String companyRegId = companyRegIdField.getText();

        // TODO: call PromotionDatabase/UserDatabase to insert user
        errorLabel.setText("Registration logic not implemented yet.");
    }

    @FXML
    private void switchToLogin() {
        SceneHelper.switchScene("auth/login.fxml");
    }
}