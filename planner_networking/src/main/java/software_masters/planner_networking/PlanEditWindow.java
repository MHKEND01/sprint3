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
	private Scene scene;
	private TreeView<Node> tree;
	private TreeItem<Node> currentlySelectedTreeItem;
	private Plan plan;
	private BorderPane mainPane;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		this.primaryStage = primaryStage;
		mainPane = new BorderPane();
		this.scene = new Scene(mainPane);
		
		initialize();
		
		primaryStage.setTitle("Plane Edit View");
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
	public void setNavBar()
	{
		TreeItem<Node> item = convertTree(plan.getRoot());
	    tree = new TreeView<Node>(item);
		tree.getSelectionModel().selectedItemProperty().addListener(e -> 
		{
			TreeItem<Node> temp=tree.getSelectionModel().getSelectedItem();
			control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
			this.currentlySelectedTreeItem = tree.getSelectionModel().getSelectedItem();
			this.updateContent();
		});
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
	public void setToolBar()
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
	public void setContent()
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
		String hostName = "10.14.1.66";
		Registry registry = LocateRegistry.getRegistry(hostName, 1060);
		Server stub = (Server) registry.lookup("PlannerServer");
		Client client = new Client(stub);
		this.model = new ClientModel(client);
		this.model.addView(this);
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
	
	public void updatePlan()
	{
		primaryStage.setMinWidth(250);
		
		PlanFile planFile = model.getPlanFile();
		plan = planFile.getPlan();
		mainPane = new BorderPane();
		TreeItem<Node> item = convertTree(plan.getRoot());
	    this.currentlySelectedTreeItem = item;
		setNavBar();
		setToolBar();
		setContent();
		
		//tree.getSelectionModel().select(tree.getRow(tempItem));
		this.scene.setRoot(mainPane);
		
		primaryStage.setScene(scene);
	}
	
	public void updateContent()
	{
		primaryStage.setMinWidth(250);
	    
		tree.refresh();
		expandTreeView(this.currentlySelectedTreeItem);
		tree.getSelectionModel().select(tree.getRow(this.currentlySelectedTreeItem));
		setContent();

		
		this.scene.setRoot(mainPane);
		
		primaryStage.setScene(scene);
	}
	
	private void expandTreeView( TreeItem<Node> selectedItem ) 
	{
	    if ( selectedItem != null ) 
	    {
	        expandTreeView( selectedItem.getParent() );

	        if ( ! selectedItem.isLeaf() ) 
	        {
	            selectedItem.setExpanded( true );
	        }
	    }
	}

}
