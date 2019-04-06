/**
 * 
 */
package software_masters.planner_networking;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
	
	private PlanEditController control;
	private TextField titleText, contentText;
	private ClientModel model;
	private Stage primaryStage;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		this.primaryStage = primaryStage;
		initialize();
		
		primaryStage.setTitle("Plane Edit View");

		
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
	    TreeView<Node> tree = new TreeView<Node>(item);
		tree.getSelectionModel().selectedItemProperty().addListener(e -> control.updateNodeText(
				tree.getSelectionModel().getSelectedItem().getValue(), contentText.getText(), 
				titleText.getText()));
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
		this.titleText = new TextField(model.getTitle());
		this.contentText = new TextField(model.getContent());
		contentText.prefHeightProperty().bind(centerPane.heightProperty().multiply(1));
		centerPane.getChildren().add(titleText);
		centerPane.getChildren().add(contentText);

		mainPane.setCenter(centerPane);
	}
	
	/**Sets up client, controller, model, connects to server, and logs in.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	private void initialize() throws RemoteException, NotBoundException
	{
		String hostName = "10.14.1.69";
		Registry registry = LocateRegistry.getRegistry(hostName, 1060);
		Server stub = (Server) registry.lookup("PlannerServer");
		Client client = new Client(stub);
		this.model = new ClientModel(client);
		control = new PlanEditController(model);
		login();
		
	}
	
	/**Logs in
	 * @param client
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	private void login() throws IllegalArgumentException, RemoteException
	{
		control.login("admin", "admin");
		control.setPlanFile("2019");
	}
	
	public void updateWindow()
	{
		primaryStage.setMinWidth(250);
		
		BorderPane mainPane = new BorderPane();
				
		setNavBar(mainPane, model.getPlan());
		setToolBar(mainPane);
		setContent(mainPane);
		
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	

}
