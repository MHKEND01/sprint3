package software_masters.planner_networking;

import java.rmi.RemoteException;

/**
 * @author lee and wesley
 * This class mediates communication from view to client model.
 */
public class PlanEditController {

	/**
	 * Acts as a Model-View-Controller controller for the plan-editing window.
	 */
	private ClientModel model;

	public PlanEditController(ClientModel model) {
		this.model = model;
	}

	/**
	 * Updates the text content and name of a node
	 * 
	 * @param node plan section to be updated
	 * @param titleText title of plan section
	 * @param contentText content of plan section
	 */
	public void updateNodeText(Node node, String titleText, String contentText) {
		model.updateNodeText(node, titleText, contentText);
	}

	/**
	 * Passes login credentials to model
	 * 
	 * @param username client's user name
	 * @param password client's password
	 * 
	 * @throws IllegalArgumentException if invalid credentials are used
	 * @throws RemoteException if cannot connect to server
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException {
		model.login(username, password);
	}

	/**
	 * Sets client's active plan and notifies views that the model has a plan to display.
	 * 
	 * @param year year of plan to get
	 * @throws IllegalArgumentException if a plan of this year doesn't exist
	 * @throws RemoteException if cannot connect to server
	 */
	public void setPlanFile(String year) throws IllegalArgumentException, RemoteException {
		model.setPlanFile(year);
	}

	/**
	 * Saves the current state of the business plan based on the provided year
	 * 
	 * @param year year of plan to save
	 * @throws IllegalArgumentException if plan of this year is not editable
	 * @throws RemoteException if cannot connect to server
	 */
	public void savePlan(String year) throws IllegalArgumentException, RemoteException, NumberFormatException { model.savePlan(year); }

	/**
	 * Adds a new section to the business plan
	 * 
	 * @throws IllegalArgumentException if type of current section cannot be duplicated
	 * @throws RemoteException if cannot connect to server
	 */
	public void addSection() throws IllegalArgumentException, RemoteException { model.addSection(); }

	/**
	 * removes a section from the business plan
	 * 
	 * @throws IllegalArgumentException if current section cannot be deleted
	 * @throws RemoteException if cannot connect to server
	 */
	public void deleteSection() throws IllegalArgumentException, RemoteException { model.deleteSection(); }

	/**
	 * @param isSaved the isSaved to set
	 */
	public void setSaved(boolean isSaved) { model.setSaved(isSaved); }

}
