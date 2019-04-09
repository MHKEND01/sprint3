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
 * @author lee and wesley
 * This class creates the view to edit a given business plan
 */
public class PlanEditWindow extends Application {

	/**
	 * Responsible for generating the plan-editing window. Acts as the view in
	 * Model-View-Controller.
	 */

	private PlanEditController control;
	protected TextField titleText, contentText, yearText;
	private ClientModel model;
	protected Stage primaryStage;
	private Scene scene;
	private TreeView<Node> tree;
	private TreeItem<Node> currentlySelectedTreeItem;
	private Plan plan;
	protected PlanFile planFile;
	protected BorderPane mainPane;

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Creates stage, borderpane, and main scene.
	 * Connects to server, logins in, and obtains a business plan to edit for
	 * demonstration purposes.
	 * Specifies event to occur when the window is closed. This is used to enforce saving constraints on close.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		mainPane = new BorderPane();
		this.scene = new Scene(mainPane);
		this.scene.getStylesheets().add(getClass().getResource("Edit_View_Style.css").toExternalForm());
		
		initialize();

		primaryStage.setTitle("Plan Edit View");
		primaryStage.setOnCloseRequest((WindowEvent e) -> {
			e.consume();
			closeWindow();
		});
		primaryStage.show();

	}

	/**
	 * Converts Business Plan tree into TreeItem tree so we can create a Treeview
	 * using recursion.
	 * 
	 * @param root of business plan tree
	 * @return root of TreeItem tree for TreeView
	 */
	public TreeItem<Node> convertTree(Node root) {
		TreeItem<Node> newRoot = new TreeItem<Node>(root);
		for (int i = 0; i < root.getChildren().size(); i++) {
			newRoot.getChildren().add(convertTree(root.getChildren().get(i)));
		}
		return newRoot;
	}

	/**
	 * Builds navigation bar and pins it to the left pane.
	 */
	public void setNavBar() {
		//creates tree view object
		TreeItem<Node> item = convertTree(plan.getRoot());
		tree = new TreeView<Node>(item);
		this.currentlySelectedTreeItem = item;

		/* updates the tree view and highlights the tree item that corresponds to the currently
		displayed business plan section
		*/
		tree.refresh();
		tree.getSelectionModel().select(tree.getRow(this.currentlySelectedTreeItem));
		
		//handles changing the view based on when a tree item is selected and updates the model's state
		tree.getSelectionModel().selectedItemProperty().addListener(e -> {
			TreeItem<Node> temp = tree.getSelectionModel().getSelectedItem();
			control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
			this.currentlySelectedTreeItem = tree.getSelectionModel().getSelectedItem();
			this.updateContent();
		});
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		//bind tree view to a vbox and then left pane of the border pane
		Label lbl = new Label();
		VBox navPane = new VBox(0);
		tree.prefHeightProperty().bind(navPane.heightProperty().multiply(1));
		tree.setTooltip(new Tooltip("Click on a section to edit it"));

		navPane.getStyleClass().add("navPane");
		navPane.getChildren().addAll(tree, lbl);
		mainPane.setLeft(navPane);
	}

	/**
	 * Displays the toolbar and pins it to the top pane of the border pane.
	 */
	public void setToolBar() {
		HBox toolPane = new HBox(30);
		toolPane.setId("toolbar_hbox");
		
		//creates a button to add branch to business plan
		Button addChildButton = new Button();
		addChildButton.setText("Add Section");
		addChildButton.setTooltip(new Tooltip("adds another section of this type"));
		
		//updates the model's state by adding the corresponding nodes and updating data from the text fields
		addChildButton.setOnAction(e -> {
			try {
				TreeItem<Node> temp = tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.addSection();
			} catch (IllegalArgumentException | RemoteException e2) {
				warn("Cannot add another section of this type");
			}
		});
		toolPane.getChildren().add(addChildButton);

		//creates a button to delete branch from business plan
		Button deleteButton = new Button();
		deleteButton.setText("Delete");
		deleteButton.setTooltip(new Tooltip("deletes section and all dependencies"));
		//updates the model's state by deleting the corresponding nodes and updating data from the text fields
		//throws a warning popup to confirm deletion 
		deleteButton.setOnAction(e -> {
			TreeItem<Node> temp = tree.getSelectionModel().getSelectedItem();
			control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
			warnOnDelete();
		});
		toolPane.getChildren().add(deleteButton);

		//creates a spacer push year and logout button to right side of toolbar
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		yearText = new TextField(planFile.getYear());
		yearText.setTooltip(new Tooltip("Edit the year to save this buisness plan to"));

		Label yearTextLabel = new Label("Year:");

		//creates a button to push business plan to server
		Button saveButton = new Button();
		saveButton.setText("Save");
		saveButton.setTooltip(new Tooltip("pushes file to server"));
		//updates the model's state by pushing the business plan to the server
		saveButton.setOnAction(e -> {
			try {
				TreeItem<Node> temp = tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.savePlan(yearText.getText());
			} 
			catch (NumberFormatException e1)
			{
				warn("Invalid Year");
			}
			catch (IllegalArgumentException e1) {
				warn("This plan is marked as non-editable: Cannot save changes");
			}
			catch (RemoteException e1)
			{
				warn("Unable to connect to server");
			}

		});
		toolPane.getChildren().add(saveButton);

		
		toolPane.getChildren().add(spacer);
		toolPane.getChildren().add(yearTextLabel);
		toolPane.getChildren().add(yearText);

		//creates a button to allow users to logout
		Button logoutButton = new Button();
		logoutButton.setText("Logout");
		logoutButton.setTooltip(new Tooltip("exit program"));
		logoutButton.setOnAction(e -> closeWindow());
		toolPane.getChildren().add(logoutButton);

		mainPane.setTop(toolPane);
	}

	/**
	 * Displays central text-editing pane
	 */
	public void setContent() {
		VBox centerPane = new VBox(5);
		
		this.titleText = new TextField(model.getTitle());
		this.contentText = new TextField(model.getContent());
		
		contentText.setTooltip(new Tooltip("Edit the content for this section here"));
		titleText.setTooltip(new Tooltip("Edit the title of this section here"));

		
		contentText.prefHeightProperty().bind(centerPane.heightProperty().multiply(1));
		centerPane.getChildren().add(titleText);
		centerPane.getChildren().add(contentText);

		mainPane.setCenter(centerPane);
	}

	/**
	 * Updates view for editing plan after adding and deleting sections as well as obtaining a new plan
	 */
	public void updatePlan() {
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

	/**
	 * Updates view for editing plan when navigating between business plan sections
	 */
	public void updateContent() {
		primaryStage.setMinWidth(250);

		tree.refresh();
		expandTreeView(this.currentlySelectedTreeItem);
		tree.getSelectionModel().select(tree.getRow(this.currentlySelectedTreeItem));
		setContent();

		this.scene.setRoot(mainPane);

		primaryStage.setScene(scene);
	}

	/**
	 * This helper method ensures treeview stays expanded and selected plan section is highlighted. 
	 * @param selectedItem
	 */
	private void expandTreeView(TreeItem<Node> selectedItem) {
		if (selectedItem != null) {
			expandTreeView(selectedItem.getParent());

			if (!selectedItem.isLeaf()) { selectedItem.setExpanded(true); }
		}
	}

	/**
	 * Upon closing the window, determines if there are any changes which may need
	 * to be saved first. If so, a warning popup is displayed.
	 */
	public void closeWindow() {
		String currContent = contentText.getText();
		String currTitle = titleText.getText();
		String currYear = yearText.getText();

		if (!currContent.equals(model.getContent()) || !currTitle.equals(model.getTitle())
				|| !currYear.equals(model.getYear())) {
			control.setSaved(false);
		}

		if (!model.isSaved()) {
			warnToSave();
		} else {
			primaryStage.close();
		}

	}

	/**
	 * Creates popup window asking user if they want to exit without saving.
	 */
	public void warnToSave() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(" Save Plan?");
		alert.setHeaderText("Save document before closing?");
		alert.setContentText("Your changes will be lost if you do not save.");

		ButtonType save = new ButtonType("Save");
		ButtonType no_save = new ButtonType("Dont Save");
		alert.getButtonTypes().setAll(save, no_save, ButtonType.CANCEL);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == save) {
			//updates the model before saving and closing
			try {
				TreeItem<Node> temp = tree.getSelectionModel().getSelectedItem();
				control.updateNodeText(temp.getValue(), titleText.getText(), contentText.getText());
				control.savePlan(yearText.getText());
				primaryStage.close();
			} catch (IllegalArgumentException e) {
				warn("Cannot be saved, this year is invalid");
			} catch (RemoteException e) {
				warn("Failed to push to server");
			}
		} else if (result.get() == no_save) { primaryStage.close(); }

	}

	/**
	 * Creates popup window asking user if they want to delete the current section
	 */
	public void warnOnDelete() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Branch?");
		alert.setHeaderText("Are you sure you wish to delete this section?");
		alert.setContentText("You will not be able to recover this section and all dependent sections.");

		ButtonType delete = new ButtonType("Delete");
		alert.getButtonTypes().setAll(delete, ButtonType.CANCEL);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == delete) {
			try {
				control.deleteSection();
			} catch (IllegalArgumentException e) {
				warn("Cannot Delete this Node");
			} catch (RemoteException e) {
				warn("Failed to connect to server");
			}
		}

	}

	/**
	 * Helper method that creates a generic warning popup based on provided text.
	 * Used in the case the user executes an invalid operation.
	 * @param message is the error message displayed on the popup
	 */
	public void warn(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText(message);
		alert.setContentText(null);
		alert.showAndWait();
	}

	/**
	 * Sets up client, controller, model, connects to server, and logs in.
	 * 
	 * @throws RemoteException if cannot connect to server
	 * @throws NotBoundException
	 */
	private void initialize() throws RemoteException, NotBoundException {
		String hostName = "127.0.0.1";
		Registry registry = LocateRegistry.getRegistry(hostName, 1061);
		Server stub = (Server) registry.lookup("PlannerServer");
		Client client = new Client(stub);
		this.model = new ClientModel(client);
		this.model.addView(this);
		control = new PlanEditController(model);
		login();

	}

	/**
	 * Logs in and gets a default plan.
	 * 
	 * @param client
	 * @throws IllegalArgumentException
	 * @throws RemoteException if cannot connect to server
	 */
	private void login() throws IllegalArgumentException, RemoteException {
		control.login("admin", "admin");
		control.setPlanFile("2018");
	}

}
