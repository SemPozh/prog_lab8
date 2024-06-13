package laba8.laba8.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import laba8.laba8.client.App;
import laba8.laba8.client.Client;
import laba8.laba8.client.modules.ObservableResourceFactory;
import laba8.laba8.client.modules.PrinterUI;

public class LoginPageController {
    private App app;
    private Client client;

    private final Color CONNECTED_COLOR = Color.GREEN;
    private final Color NOT_CONNECTED_COLOR = Color.RED;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private CheckBox registerCheckBox;
    @FXML
    private Label isConnectedLabel;
    @FXML
    private Button signInButton;

    /**
     * SignI in button on action.
     */
    @FXML
    private void signInButtonOnAction() {
        if (client.authenticateUserProcess(usernameField.getText(),
                passwordField.getText(),
                registerCheckBox.isSelected())){
            app.showMainPage();
        } else if (!client.isConnected()) {
            PrinterUI.error("NotConnectedException");
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("NotConnected"));
            isConnectedLabel.setTextFill(NOT_CONNECTED_COLOR);
        } else {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("Connected"));
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        }
    }

    public ObservableResourceFactory resourceFactory;

    private void bindGuiLanguage() {
        usernameLabel.textProperty().bind(resourceFactory.getStringBinding("UsernameLabel"));
        passwordLabel.textProperty().bind(resourceFactory.getStringBinding("PasswordLabel"));
        registerCheckBox.textProperty().bind(resourceFactory.getStringBinding("RegisterCheckbox"));
        signInButton.textProperty().bind(resourceFactory.getStringBinding("SignInButton"));
        if (client.isConnected()) {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("Connected"));
            isConnectedLabel.setTextFill(CONNECTED_COLOR);
        } else {
            isConnectedLabel.textProperty().bind(resourceFactory.getStringBinding("NotConnected"));
            isConnectedLabel.setTextFill(NOT_CONNECTED_COLOR);
        }
    }


    public void setApp(App app) {
        this.app = app;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }
}
