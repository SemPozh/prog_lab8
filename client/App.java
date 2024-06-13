package laba8.laba8.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import laba8.laba8.client.controllers.AskPageController;
import laba8.laba8.client.controllers.LoginPageController;
import laba8.laba8.client.controllers.MainPageController;
import laba8.laba8.client.modules.*;
import laba8.laba8.common.exeptions.NotInDeclaredLimitsException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class App extends Application {

    public static final String BUNDLE = "bundles.gui";
    public static final String SYMBOL1 = "$ ";
    public static final String SYMBOL2 = "> ";
    private static ObservableResourceFactory resourceFactory;
    private InputHandler inputHandler;
    private Stage primaryStage;

    private static String host;
    private static int port;

    private static Client client;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Printer.print("Usage: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Printer.print("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Printer.print("Port cannot be negative!");
        }
        return false;
    }

    @Override
    public void init() {
        inputHandler = new InputHandler(new Scanner(System.in));
        client = new Client(host, port);
        client.run();
    }

    @Override
    public void stop() {
        client.stop();
        inputHandler.close();
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        resourceFactory = new ObservableResourceFactory();
        resourceFactory.setResources(ResourceBundle.getBundle(BUNDLE));
        PrinterUI.setResourceFactory(resourceFactory);
        Printer.setResourceFactory(resourceFactory);

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            this.primaryStage = stage;

            FXMLLoader loginPageManager = new FXMLLoader();
            loginPageManager.setLocation(getClass().getResource("/view/LoginPage.fxml"));
            Parent loginPageRootNode = loginPageManager.load();
            Scene loginPageScene = new Scene(loginPageRootNode);
            LoginPageController loginPageController = loginPageManager.getController();
            loginPageController.setApp(this);
            loginPageController.setClient(client);
            loginPageController.initLangs(resourceFactory);
            primaryStage.setTitle("LABA 8");
            primaryStage.setScene(loginPageScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception exception) {
            PrinterUI.error("Unexpected error!");
        }
    }


    public void showMainPage() {
        try {

            FXMLLoader mainPageLoader = new FXMLLoader();
            mainPageLoader.setLocation(getClass().getResource("/view/MainPage.fxml"));
            Parent mainPageRootNode = mainPageLoader.load();
            Scene mainPageScene = new Scene(mainPageRootNode);
            MainPageController mainPageController = mainPageLoader.getController();
            mainPageController.initLangs(resourceFactory);
            FXMLLoader askPageLoader = new FXMLLoader();
            askPageLoader.setLocation(getClass().getResource("/view/AskPage.fxml"));
            Parent askPageRootNode = askPageLoader.load();
            Scene askPageScene = new Scene(askPageRootNode);
            Stage askStage = new Stage();
            askStage.setTitle("LABA 8");
            askStage.setScene(askPageScene);
            askStage.setResizable(false);
            askStage.initModality(Modality.WINDOW_MODAL);
            askStage.initOwner(primaryStage);
            AskPageController askPageController = askPageLoader.getController();
            askPageController.setClient(client);
            askPageController.setAskStage(askStage);
            askPageController.initLangs(resourceFactory);

            mainPageController.setClient(client);
            mainPageController.setUsername(client.getUsername());
            mainPageController.setAskStage(askStage);
            mainPageController.setPrimaryStage(primaryStage);
            mainPageController.setAskWindowController(askPageController);
            mainPageController.setRefreshProcess(true);
            mainPageController.refresh();

            primaryStage.setScene(mainPageScene);
            primaryStage.setMinWidth(mainPageScene.getWidth());
            primaryStage.setMinHeight(mainPageScene.getHeight());
            primaryStage.setResizable(true);
        } catch (Exception exception) {
            PrinterUI.error("Unexpected error!");
        }
    }
}
