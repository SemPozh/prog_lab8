package laba8.laba8.client.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import laba8.laba8.client.Client;
import laba8.laba8.client.modules.ObservableResourceFactory;
import laba8.laba8.client.modules.PrinterUI;
import laba8.laba8.client.validators.*;
import laba8.laba8.common.data.Address;
import laba8.laba8.common.data.Coordinates;
import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.OrganizationType;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;
import laba8.laba8.common.exeptions.MustBeNotEmptyException;
import laba8.laba8.common.exeptions.NotInDeclaredLimitsException;
import laba8.laba8.server.modules.ResponseOutputer;

public class AskPageController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label annualTurnoverLabel;
    @FXML
    private Label employeesCountLabel;
    @FXML
    private Label organizationTypeLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField annualTurnoverField;
    @FXML
    private TextField employeesCountField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<OrganizationType> organizationTypeBox;

    @FXML
    private Button enterButton;

    private Stage askStage;
    private Organization resultOrganization;
    private ObservableResourceFactory resourceFactory;

    private Client client;

    public Organization getAndClear() {
        Organization organizationToReturn = resultOrganization;
        resultOrganization = null;
        return organizationToReturn;
    }

    /**
     * Initialize ask window.
     */
    public void initialize() {
        organizationTypeBox.setItems(FXCollections.observableArrayList(OrganizationType.values()));
    }

    /**
     * Set Marine.
     *
     * @param organization Organization to set.
     */
    public void setOrganization(Organization organization) {
        nameField.setText(organization.getName());
        coordinatesXField.setText(organization.getCoordinates().getX() + "");
        coordinatesYField.setText(organization.getCoordinates().getY() + "");
        annualTurnoverField.setText(organization.getAnnualTurnover() == null ? "" : organization.getAnnualTurnover() + "");
        employeesCountField.setText(organization.getEmployeesCount() + "");
        organizationTypeBox.setValue(organization.getType());
        addressField.setText(organization.getOfficialAddress()==null ? "" : organization.getOfficialAddress().getZipCode());
    }

    /**
     * Enter button on action.
     */
    @FXML
    private void enterButtonOnAction() {
        try {
            resultOrganization = new Organization(
                    convertName(),
                    new Coordinates(
                            convertCoordinatesX(),
                            convertCoordinatesY()
                    ),
                    convertEmployeeCount(),
                    convertOrganizationType(),
                    getClient().getUser()
            );
            resultOrganization.setAnnualTurnover(convertAnnualTurnover());
            resultOrganization.setOfficialAddress(convertAddress());
            askStage.close();
        } catch (InvalidObjectFieldException e) {
            PrinterUI.error(e.getMessage());
        }
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Clear Organization.
     */
    public void clearOrganization() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        annualTurnoverField.clear();
        employeesCountField.clear();
        addressField.clear();
        organizationTypeBox.setValue(OrganizationType.PUBLIC);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    /**
     * Convert name.
     *
     * @return Name.
     */
    private String convertName() throws InvalidObjectFieldException {
        NameValidator nameValidator = new NameValidator();
        String name = nameField.getText();
        return nameValidator.validate(name);
    }

    /**
     * Convert Coordinates X.
     *
     * @return X.
     */
    private Integer convertCoordinatesX() throws InvalidObjectFieldException {
        XCoordinateValidator xCoordinateValidator = new XCoordinateValidator();
        String strX = coordinatesXField.getText();
        return xCoordinateValidator.validate(strX);
    }

    /**
     * Convert Coordinates Y.
     *
     * @return Y.
     */
    private double convertCoordinatesY() throws InvalidObjectFieldException {
        YCoordinateValidator yCoordinateValidator = new YCoordinateValidator();
        String strY;
        strY = coordinatesYField.getText();
        return yCoordinateValidator.validate(strY);
    }

    private Integer convertAnnualTurnover() throws InvalidObjectFieldException {
        AnnualTurnoverValidator annualTurnoverValidator = new AnnualTurnoverValidator();
        String strAnnualTurnover;
        strAnnualTurnover = annualTurnoverField.getText();
        return annualTurnoverValidator.validate(strAnnualTurnover);
    }

    private Integer convertEmployeeCount() throws InvalidObjectFieldException {
        EmployeeCountValidator employeeCountValidator = new EmployeeCountValidator();
        String strEmployeeCount;
        strEmployeeCount = employeesCountField.getText();
        return employeeCountValidator.validate(strEmployeeCount);
    }

    private OrganizationType convertOrganizationType(){
        return organizationTypeBox.getValue();
    }

    private Address convertAddress() throws InvalidObjectFieldException {
        ZipCodeValidator zipCodeValidator = new ZipCodeValidator();
        String zipCode;
        zipCode = addressField.getText();
        return zipCodeValidator.validate(zipCode);
    }

    /**
     * Init langs.
     *
     * @param resourceFactory Resource factory to set.
     */
    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }

    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        annualTurnoverLabel.textProperty().bind(resourceFactory.getStringBinding("AnnualTurnoverColumn"));
        employeesCountLabel.textProperty().bind(resourceFactory.getStringBinding("EmployeeCountColumn"));
        organizationTypeLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        addressLabel.textProperty().bind(resourceFactory.getStringBinding("AddressColumn"));
        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }
}
