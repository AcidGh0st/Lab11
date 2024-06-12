package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import ucr.lab.HelloApplication;

import java.io.IOException;

public class HelloController {
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Text txtMessage;

    private void loadPage(String page) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(page));
        try {
            this.bp.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void Exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void Home(ActionEvent event) {
        this.txtMessage.setText("Laboratory No. 11");
        this.bp.setCenter(ap);
    }

    @FXML
    void directedGraphOnAction(ActionEvent event) {
        loadPage("directedGraph.fxml");
    }

    @FXML
    void directedGraphOperationsOnAction(ActionEvent event) {
        loadPage("directedGraphOperations.fxml");
    }

    @FXML
    void linkedGraphOnAction(ActionEvent event) {
        loadPage("linkedGraph.fxml");
    }

    @FXML
    void linkedGraphOperationsOnAction(ActionEvent event) {
        loadPage("linkedGraphOperations.fxml");
    }

    @FXML
    void mstOnAction(ActionEvent event) {
        loadPage("mstKruskalAndPrim.fxml");
    }

    @FXML
    void shortestPathOnAction(ActionEvent event) {
        loadPage("dijkstra.fxml");
    }

}