package ac.csg.pu.gui.auth;

import ac.csg.pu.comms.MailService;
import ac.csg.pu.comms.model.Mail;
import ac.csg.pu.gui.SceneHelper;
import ac.csg.pu.gui.util.ShakeAnimation;
import ac.csg.pu.members.UserDatabase;
import ac.csg.pu.members.UserType;
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
        if (!validateRegistrationInfo()) return;
        saveUser();
    }

    @FXML
    private boolean validateRegistrationInfo() {
        String email = emailField.getText().trim();
        boolean isCommercial = commercialRadio.isSelected();

        if (accountTypeGroup.getSelectedToggle() == null) {
            errorLabel.setText("Please select an account type.");
            return false;
        }

        if (email.isEmpty()) {
            errorLabel.setText("Email is required.");
            shakeFields();
            return false;
        }

        if (isCommercial) {
            String companyName = companyNameField.getText().trim();
            String companyAddress = companyAddressField.getText().trim();
            String companyRegId = companyRegIdField.getText().trim();

            if (companyName.isEmpty() || companyAddress.isEmpty() || companyRegId.isEmpty()) {
                errorLabel.setText("All fields are required.");
                shakeFields();
                return false;
            }
        }

        return true;
    }

    private void saveUser() {
        String email = emailField.getText().trim();
        boolean isCommercial = ((RadioButton)accountTypeGroup.getSelectedToggle()).getText().equals("Commercial");

        if (isCommercial) {
            String companyName = companyNameField.getText().trim();
            String companyAddress = companyAddressField.getText().trim();
            String companyRegId = companyRegIdField.getText().trim();

            // TODO: forward to SA for approval, save details when approved
            errorLabel.setText("Commercial application submitted for approval.");
            return;
        }

        String password = UserDatabase.generatePassword();

        UserDatabase.insertNewUser(
                email,
                password,
                UserType.NC.name()
        );

        sendPasswordEmail(email, password);

        errorLabel.setText("Registration successful! Generated password (" + password + ") has been sent to email " + email);
    }

    @FXML
    private void switchToLogin() {
        SceneHelper.switchScene("auth/login.fxml");
    }

    private void shakeFields() {
        ShakeAnimation.shake(emailField);
        ShakeAnimation.shake(commercialFields);
    }

    private void sendPasswordEmail(String email, String password) {
        new Thread(() -> {
            try {
                Mail mail = new Mail();
                mail.receivers = new String[]{email};
                mail.subject = "Your Account Password";
                mail.body =
                        "Welcome!\n\n" +
                                "Your account has been created successfully.\n\n" +
                                "Email: " + email + "\n" +
                                "Password: " + password + "\n\n" +
                                "Please log in and change your password immediately.";

                MailService.process(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}