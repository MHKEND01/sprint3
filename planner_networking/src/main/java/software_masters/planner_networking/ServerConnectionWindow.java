/**
 * 
 */
package software_masters.planner_networking;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.geometry.*;


/**
 * @author lee.kendall
 *
 */
public class ServerConnectionWindow extends Application {

	/**
	 * @param args
	 */
	TreeView<Node> tree;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		primaryStage.setTitle("Title");
		primaryStage.setMinWidth(250);
		
		VMOSA VMOSAPlan = new VMOSA();
		TreeItem<Node> item = convertTree(VMOSAPlan.getRoot());

		
		tree = new TreeView<Node>(item);
		tree.getSelectionModel().selectedItemProperty().addListener(e -> System.out.println(tree.getSelectionModel().getSelectedItem()));
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		BorderPane mainPane = new BorderPane();
		
		Label lbl = new Label();
		VBox navPane = new VBox(1);
		navPane.getChildren().addAll(tree,lbl);
		
		HBox toolPane = new HBox(1);
		Button saveButton = new Button();
		saveButton.setText("Save");
		saveButton.setOnAction(e -> System.out.println("Saved!"));
		toolPane.getChildren().add(saveButton);
		
		mainPane.setLeft(navPane);
		mainPane.setTop(toolPane);
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public TreeItem<Node> convertTree(Node root)
	{
		TreeItem<Node> newRoot = new TreeItem<Node>(root);
		for(int i = 0; i < root.getChildren().size();i++) 
		{
			newRoot.getChildren().add(convertTree(root.getChildren().get(i)));
		}
		return newRoot;
	}

}
