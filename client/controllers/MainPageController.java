package laba8.laba8.client.controllers;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import laba8.laba8.client.App;
import laba8.laba8.client.Client;
import laba8.laba8.client.modules.ObservableResourceFactory;
import laba8.laba8.client.modules.PrinterUI;
import laba8.laba8.common.data.Address;
import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.OrganizationType;
import laba8.laba8.common.interaction.Response;
import laba8.laba8.server.commands.ShowCommand;
import org.controlsfx.control.table.TableFilter;

import java.io.File;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MainPageController {
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String ADD_IF_MAX_COMMAND_NAME = "add_if_max";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String PRINT_FIELD_DESCENDING_ANNUAL_TURNOVER_COMMAND_NAME = "print_field_descending_annual_turnover";
    public static final String AVERAGE_OF_ANNUAL_TURNOVER_COMMAND_NAME = "average_of_annual_turnover";
    public static final String MIN_OF_EMPLOYEES_COUNT_COMMAND_NAME = "min_by_employees_count";
    public static final String EXIT_COMMAND_NAME = "exit";


    @FXML
    private TableView<Organization> organizationTable;
    @FXML
    private TableColumn<Organization, Integer> idColumn;
    @FXML
    private TableColumn<Organization, String> ownerColumn;
    @FXML
    private TableColumn<Organization, ZonedDateTime> creationDateColumn;
    @FXML
    private TableColumn<Organization, String> nameColumn;
    @FXML
    private TableColumn<Organization, Integer> annualTurnoverColumn;
    @FXML
    private TableColumn<Organization, Integer> coordinatesXColumn;
    @FXML
    private TableColumn<Organization, Double> coordinatesYColumn;
    @FXML
    private TableColumn<Organization, Integer> employeeCountColumn;
    @FXML
    private TableColumn<Organization, OrganizationType> organizationTypeColumn;
    @FXML
    private TableColumn<Organization, String> addressColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Button addIfMaxButton;
    @FXML
    private Button insertAtButton;
    @FXML
    private Button averageOfAnnualTurnoverButton;

    @FXML
    private Button minByEmployeesCountButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button printFieldDescendingAnnualTurnoverButton;

    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip executeScriptButtonTooltip;

    @FXML
    private Tooltip printFieldDescendingAnnualTurnoverButtonTooltip;
    @FXML
    private Tooltip addIfMaxButtonTooltip;
    @FXML
    private Tooltip minByEmployeesCountButtonTooltip;
    @FXML
    private Tooltip averageOfAnnualTurnoverButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskPageController askPageController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Integer> shapeMap;
    private Map<Integer, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    private final long RANDOM_SEED = 1821L;
    private final double MAX_SIZE = 250;
    private final Duration ANIMATION_DURATION = Duration.millis(800);

    private volatile boolean refreshProcess = false;
    private ConcurrentLinkedDeque<Organization> collection;


    // Commands


    /**
     * Initialize main window.
     */
    public void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<Integer, Text>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("English", new Locale("en", "EN"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Eesti keel", new Locale("et", "ET"));
        localeMap.put("Български", new Locale("bg", "BG"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */
    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreatedBy().getUsername()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        annualTurnoverColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnualTurnover()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        employeeCountColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getEmployeesCount()));
        organizationTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        addressColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOfficialAddress()==null? "" : cellData.getValue().getOfficialAddress().getZipCode()));
    }

    public boolean isRefreshInProcess() {
        return refreshProcess;
    }

    public void setRefreshProcess(boolean refreshProcess) {
        this.refreshProcess = refreshProcess;
    }

    public void refresh() {
        Thread refresher = new Thread(() -> {
            while (isRefreshInProcess()) {
                Platform.runLater(this::loadCollection);
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    PrinterUI.error("Thread was interrupted, failed to complete operation");
                }
            }
        });
        refresher.start();
    }

    public void setCollection(ConcurrentLinkedDeque<Organization> collection) {
        this.collection = collection;
    }

    public void loadCollection() {
        requestAction(REFRESH_COMMAND_NAME);
        refreshCanvas();
    }


    /**
     * Bind gui language.
     */
    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (App.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        annualTurnoverColumn.textProperty().bind(resourceFactory.getStringBinding("AnnualTurnoverColumn"));
        employeeCountColumn.textProperty().bind(resourceFactory.getStringBinding("EmployeeCountColumn"));
        organizationTypeColumn.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        addressColumn.textProperty().bind(resourceFactory.getStringBinding("AddressColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        addIfMaxButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMaxButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        averageOfAnnualTurnoverButton.textProperty().bind(resourceFactory.getStringBinding("AverageOfAnnualTurnoverButton"));
        printFieldDescendingAnnualTurnoverButton.textProperty().bind(resourceFactory.getStringBinding("PrintFieldDescendingAnnualTurnoverButton"));
        minByEmployeesCountButton.textProperty().bind(resourceFactory.getStringBinding("MinByEmployeesCountButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeScriptButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButtonTooltip"));
        addIfMaxButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddIfMaxButtonTooltip"));
        averageOfAnnualTurnoverButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AverageOfAnnualTurnoverButtonTooltip"));
        minByEmployeesCountButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("MinByEmployeesCountButtonTooltip"));
        printFieldDescendingAnnualTurnoverButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("PrintFieldDescendingAnnualTurnoverButtonTooltip"));
    }

    /**
     * Info button on action.
     */
    @FXML
    private void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);
    }

    /**
     * Add button on action.
     */
    @FXML
    private void addButtonOnAction() {
        askPageController.clearOrganization();
        askStage.showAndWait();
        Organization organization = askPageController.getAndClear();
        if (organization != null) requestAction(ADD_COMMAND_NAME, "", organization);
    }

    /**
     * Update button on action.
     */
    @FXML
    private void updateButtonOnAction() {
        if (!organizationTable.getSelectionModel().isEmpty()) {
            Integer id = organizationTable.getSelectionModel().getSelectedItem().getId();
            askPageController.setOrganization(organizationTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            Organization organization = askPageController.getAndClear();
            if (organization != null) requestAction(UPDATE_COMMAND_NAME, id + "", organization);
        } else{
            PrinterUI.error("OrganizationSelectionException");
        }

    }

    /**
     * Remove button on action.
     */
    @FXML
    private void removeButtonOnAction() {
        if (!organizationTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_COMMAND_NAME,
                    organizationTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else PrinterUI.error("OrganizationSelectionException");
    }


    /**
     * Clear button on action.
     */
    @FXML
    private void clearButtonOnAction() {
        requestAction(CLEAR_COMMAND_NAME);
    }

    /**
     * Execute script button on action.
     */
    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return;
        if (client.processScriptToServer(selectedFile)) Platform.exit();
    }

    /**
     * Add if min button on action.
     */
    @FXML
    private void addIfMaxButtonOnAction() {
        askPageController.clearOrganization();
        askStage.showAndWait();
        Organization organization = askPageController.getAndClear();
        if (organization != null) requestAction(ADD_IF_MAX_COMMAND_NAME, "", organization);
    }


    @FXML
    private void minByEmployeesCountButtonOnAction() {
        requestAction(MIN_OF_EMPLOYEES_COUNT_COMMAND_NAME);
    }

    /**
     * Sum of health button on action.
     */
    @FXML
    private void averageOfAnnualTurnoverButtonOnAction() {
        requestAction(AVERAGE_OF_ANNUAL_TURNOVER_COMMAND_NAME);
    }

    @FXML
    private void printFieldDescendingAnnualTurnoverButtonOnAction() {
        requestAction(PRINT_FIELD_DESCENDING_ANNUAL_TURNOVER_COMMAND_NAME);
    }

    /**
     * Request action.
     */
    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        ConcurrentLinkedDeque<Organization> organizations = client.processRequestToServer(commandName, commandStringArgument,
                commandObjectArgument);
        setCollection(organizations);
        if (organizations != null) {
            ObservableList<Organization> organizationsList = FXCollections.observableArrayList(organizations);
            organizationTable.setItems(organizationsList);
            TableFilter.forTableView(organizationTable).apply();
            organizationTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }
    }

    /**
     * Binds request action.
     */
    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }

    /**
     * Refreshes canvas.
     */
    private void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s -> canvasPane.getChildren().remove(s));
        textMap.clear();
        for (Organization organization : organizationTable.getItems()) {
            if (!userColorMap.containsKey(organization.getCreatedBy().getUsername()))
                userColorMap.put(organization.getCreatedBy().getUsername(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));


            double size = Math.min(organization.getEmployeesCount(), MAX_SIZE);

            Shape circleObject = new Circle(size, userColorMap.get(organization.getCreatedBy().getUsername()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(organization.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(organization.getCoordinates().getY()));
            circleObject.setStroke(Paint.valueOf("#000000"));

            Text textObject = new Text(organization.getId().toString());
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size / 3));
            textObject.setFill(userColorMap.get(organization.getCreatedBy().getUsername()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, organization.getId());
            textMap.put(organization.getId(), textObject);

            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }
    }

    /**
     * Shape on mouse clicked.
     */
    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        Integer id = shapeMap.get(shape);
        for (Organization organization : organizationTable.getItems()) {
            if (Objects.equals(organization.getId(), id)) {
                organizationTable.getSelectionModel().select(organization);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskPageController askPageController) {
        this.askPageController = askPageController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
            languageComboBox.getSelectionModel().selectFirst();
        languageComboBox.setOnAction((event) ->
                resourceFactory.setResources(ResourceBundle.getBundle
                        (App.BUNDLE, localeMap.get(languageComboBox.getValue()))));
        bindGuiLanguage();
    }
}
