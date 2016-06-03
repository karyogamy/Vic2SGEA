package gui;

import java.io.IOException;

import main.Report;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Game extends Application {
    //SaveGame saveGame = new SaveGame();
    public static Stage goodsStage;
    public static Stage countryStage;
    public static Stage window;
    public static GoodsList goodsList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        final Parent windowRoot = FXMLLoader.load(getClass().getResource("Window.fxml"));
        final FXMLLoader goodsLoader = new FXMLLoader();
        final Parent goodsRoot = goodsLoader.load(getClass().getResource("Goods.fxml").openStream());
        goodsList = goodsLoader.getController();

        stage.setTitle("Victoria II SGEA: main window");
        stage.setScene(new Scene(windowRoot));
        //stage.setMinWidth(700);
        //stage.setMinHeight(500);
        stage.show();
        stage.getIcons().add(new Image("/flags/EST.png")); /* Cause I'm Estonian, thats why */

        //stage.getScene().getStylesheets().add(getClass().getResource("Moy.css").toExternalForm());
        // stage.getScene().getStylesheets().add(Game.class.getResource("/MOY.css").toExternalForm());
		/* String cssPath = "moy.css";
		 stage.getScene().getStylesheets().addAll(cssPath);*/
        // Throws error when user cancels selection

        goodsStage = new Stage();
        goodsStage.setTitle("Goods list window");
        goodsStage.getIcons().add(new Image("/flags/EST.png"));  //Cause I'm Estonian, thats why
        goodsStage.setScene(new Scene(goodsRoot));
    }



}
