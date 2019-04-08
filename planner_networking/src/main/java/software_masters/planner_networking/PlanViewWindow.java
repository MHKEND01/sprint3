package software_masters.planner_networking;

import java.rmi.RemoteException;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * @author lee and wesley
 */
public class PlanViewWindow extends PlanEditWindow {
	
	@Override
	public void closeWindow() {
		primaryStage.close();
	}
	
	@Override
	public void setToolBar() {
		HBox toolPane = new HBox(30);
		//creates a spacer push year and logout button to right side of toolbar
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		yearText = new TextField(planFile.getYear());
		Label yearTextLabel = new Label("Year:");

		Label labelText = new Label("View Only");
		StackPane label = new StackPane(labelText);
		label.setStyle("-fx-text-inner-color: red;");
		
		toolPane.getChildren().add(label);
		toolPane.getChildren().add(spacer);
		toolPane.getChildren().add(yearTextLabel);
		toolPane.getChildren().add(yearText);

		//creates a button to allow users to logout
		Button logoutButton = new Button();
		logoutButton.setText("Logout");
		logoutButton.setOnAction(e -> closeWindow());
		toolPane.getChildren().add(logoutButton);

		mainPane.setTop(toolPane);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
