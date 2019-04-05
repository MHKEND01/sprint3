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
public class PlanEditWindow extends Application {

	/**
	 * Responsible for generating the plan-editing window. Acts as the view in Model-View-Controller.
	 * @param args
	 */
	private TreeView<Node> tree;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		
		primaryStage.setTitle("Plane Edit View");
		primaryStage.setMinWidth(250);
		
		BorderPane mainPane = new BorderPane();
		
		VMOSA VMOSAPlan = new VMOSA();
		
		setNavBar(mainPane, VMOSAPlan);
		setToolBar(mainPane);
		setContent(mainPane);
		
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();

		
	}
	/**
	 * Converts Business Plan tree into TreeItem tree
	 * @param root of business plan tree
	 * @return root of TreeItem tree for TreeView
	 */
	public TreeItem<Node> convertTree(Node root)
	{
		TreeItem<Node> newRoot = new TreeItem<Node>(root);
		for(int i = 0; i < root.getChildren().size();i++) 
		{
			newRoot.getChildren().add(convertTree(root.getChildren().get(i)));
		}
		return newRoot;
	}
	
	/**
	 * Displays the navigation bar
	 * @param mainPane
	 * @param plan
	 */
	public void setNavBar(BorderPane mainPane, Plan plan)
	{
		TreeItem<Node> item = convertTree(plan.getRoot());
		tree = new TreeView<Node>(item);
		tree.getSelectionModel().selectedItemProperty().addListener(e -> System.out.println(tree.getSelectionModel().getSelectedItem()));
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		
		Label lbl = new Label();
		VBox navPane = new VBox(0);
		tree.prefHeightProperty().bind(navPane.heightProperty().multiply(1));

		navPane.getStyleClass().add("navPane");
		navPane.getChildren().addAll(tree,lbl);
		mainPane.setLeft(navPane);	
	}
	
	/**
	 * Displays the toolbar
	 * @param mainPane
	 */
	public void setToolBar(BorderPane mainPane)
	{
		HBox toolPane = new HBox(30);
		Button saveButton = new Button();
		saveButton.setText("Save");
		saveButton.setOnAction(e -> System.out.println("Saved!"));
		toolPane.getChildren().add(saveButton);
		
		Button addChildButton = new Button();
		addChildButton.setText("Add Child");
		addChildButton.setOnAction(e -> System.out.println("Child made!"));
		toolPane.getChildren().add(addChildButton);
		
		Button deleteButton = new Button();
		deleteButton.setText("Delete");
		deleteButton.setOnAction(e -> System.out.println("DELETED"));
		toolPane.getChildren().add(deleteButton);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		TextField yearText = new TextField();
		Label yearTextLabel = new Label("Year:");
		toolPane.getChildren().add(spacer);
		toolPane.getChildren().add(yearTextLabel);
		toolPane.getChildren().add(yearText);
		
		Button logoutButton = new Button();
		logoutButton.setText("Logout");
		logoutButton.setOnAction(e -> System.out.println("Logging Out"));
		toolPane.getChildren().add(logoutButton);
		
		mainPane.setTop(toolPane);
	}
	
	
	/**
	 * Displays central text-editing pane
	 * @param mainPane
	 */
	public void setContent(BorderPane mainPane)
	{
		VBox centerPane = new VBox(5);
		TextField titleText = new TextField();
		TextField contentText = new TextField();
		contentText.prefHeightProperty().bind(centerPane.heightProperty().multiply(1));
		centerPane.getChildren().add(titleText);
		centerPane.getChildren().add(contentText);

		mainPane.setCenter(centerPane);
	}
	
	

}
