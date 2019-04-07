/**
 * 
 */
package software_masters.planner_networking;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
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
	private TextField titleText, contentText, yearText;
	private ClientModel model;
	private Stage primaryStage;
	private Scene scene;
	private TreeView<Node> tree;
	private TreeItem<Node> currentlySelectedTreeItem;
	private Plan plan;
	private PlanFile planFile;
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
		primaryStage.setOnCloseRequest((WindowEvent e) -> { 
			e.consume();
			closeWindow();
			});
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
	    this.currentlySelectedTreeItem = item;
	    
		tree.refresh();
		tree.getSelectionModel().select(tree.getRow(this.currentlySelectedTreeItem));
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
		
		Button addChildButton = new Button();
		addChildButton.setText("Add Section");
		addChildButton.setOnAction(e -> {
			try {
				TreeItem<Node> temp=tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.addSection();
			} catch (IllegalArgumentException | RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		toolPane.getChildren().add(addChildButton);
		
		Button deleteButton = new Button();
		deleteButton.setText("Delete");
		deleteButton.setOnAction(e -> {
			try {
				control.deleteSection();
			} catch (IllegalArgumentException | RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		toolPane.getChildren().add(deleteButton);
		
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		yearText = new TextField(planFile.getYear());
		Label yearTextLabel = new Label("Year:");
		
		Button saveButton = new Button();
		saveButton.setText("Save");
		saveButton.setOnAction(e -> {
			try {
				TreeItem<Node> temp=tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.savePlan(yearText.getText());
			} catch (IllegalArgumentException | RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		toolPane.getChildren().add(saveButton);
		
		toolPane.getChildren().add(spacer);
		toolPane.getChildren().add(yearTextLabel);
		toolPane.getChildren().add(yearText);
		
		Button logoutButton = new Button();
		logoutButton.setText("Logout");
		logoutButton.setOnAction(e -> closeWindow());
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
		Registry registry = LocateRegistry.getRegistry(hostName, 1066);
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
		control.setPlanFile("2018");
	}
	
	public void updatePlan()
	{
		primaryStage.setMinWidth(250);
		
		planFile = model.getPlanFile();
		plan = planFile.getPlan();
		mainPane = new BorderPane();

		setNavBar();
		setToolBar();
		setContent();
		
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
	
	/**Upon closing the window, determines if there are any changes which may need to be saved first.
	 * If so, a warning popup is displayed.
	 * 
	 */
	public void closeWindow()
	{
		String currContent = contentText.getText();
		String currTitle = titleText.getText();
		String currYear = yearText.getText();
		
		if(!currContent.equals(model.getContent()) || !currTitle.equals(model.getTitle()) || !currYear.equals(model.getYear()))
		{
			control.setSaved(false);
		}
		
		if(!model.isSaved())
		{
			warnToSave();
		}else {
			primaryStage.close();
		}
		

	}
	
	/**
	 * Creates popup window asking user if they want to exit without saving
	 */
	public void warnToSave()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(" Save Plan?");
		alert.setHeaderText("Save document before closing?");
		alert.setContentText("Your changes will be lost if you do not save.");
	
		ButtonType save=new ButtonType("Save");
		ButtonType no_save=new ButtonType("Dont Save");
		alert.getButtonTypes().setAll(save, no_save, ButtonType.CANCEL);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == save)
		{
			try {
				TreeItem<Node> temp=tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.savePlan(yearText.getText());
				primaryStage.close();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				warn("Cannot be saved, this year is invalid");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				warn("Failed to push to server");
			}
		}
		else if(result.get() == no_save)
		{
			primaryStage.close();
		}

	}
	public void warn(String message)
	{
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText(message);
		alert.setContentText(null);
		alert.showAndWait();
	}

}
