package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author lee and wesley
 *	Backend of application. This allows the controller to interact with the client.
 * Uses observer pattern to update views of a state change.
 */
public class ClientModel {
	/**
	 * A Model-View-Controller model which encapsulates the client from sprint 2.
	 */

	private Client client;
	private ArrayList<PlanEditWindow> views = new ArrayList<PlanEditWindow>();
	private boolean isSaved;

	public ClientModel(Client client) { this.client = client; }

	/**
	 * Adds view
	 * 
	 * @param window a view object/observer
	 */
	public void addView(PlanEditWindow window) { views.add(window); }

	/**
	 * Removes view
	 * 
	 * @param window a view object/observer
	 */
	public void removeView(PlanEditWindow window) { views.remove(window); }

	/**
	 * Notifies views of a change
	 */
	public void notifyViews() { views.forEach(n -> n.updatePlan()); }

	/**
	 * Updates the text content and name of a node, and changes currently accessed
	 * node
	 * 
	 * @param node plan section to be updated
	 * @param titleText title of plan section
	 * @param contentText content of plan section
	 */
	public void updateNodeText(Node node, String titleText, String contentText) {
		client.editData(contentText);
		client.editName(titleText);
		client.setCurrNode(node);
		isSaved = false;
	}

	/**
	 * @return the client
	 */
	public Client getClient() { return client; }

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) { this.client = client; }

	/**
	 * @return name of current node
	 */
	public String getTitle() { return client.getCurrNode().getName(); }

	/**
	 * @return content of current node
	 */
	public String getContent() { return client.getCurrNode().getData(); }

	/**
	 * @return year of current plan file
	 */
	public String getYear() { return client.getCurrPlanFile().getYear(); }

	/**
	 * @return current plan
	 */
	public Plan getPlan() { return client.getCurrPlanFile().getPlan(); }

	/**
	 * Logs in
	 * 
	 * @param username client's user name
	 * @param password client's password
	 * 
	 * @throws IllegalArgumentException if invalid credentials are given
	 * @throws RemoteException if cannot connect to server
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException {
		client.login(username, password);
	}

	/**
	 * Sets client's plan and notifies views that the model has a plan.
	 * 
	 * @param year year of plan to retrieve from server
	 * @throws IllegalArgumentException if a plan of that year does not exist
	 * @throws RemoteException if cannot connect to server
	 */
	public void setPlanFile(String year) throws IllegalArgumentException, RemoteException {
		client.getPlan(year);
		isSaved = true;
		notifyViews();
	}

	/**
	 * @return current planFile
	 */
	public PlanFile getPlanFile() { return client.getCurrPlanFile(); }

	/**
	 * Saves the current state of the business plan based on year.
	 * 
	 * @param year year to save a plan to
	 * @throws IllegalArgumentException if the plan of provided year is not editable
	 * @throws RemoteException if cannot connect to server
	 */
	public void savePlan(String year) throws IllegalArgumentException, RemoteException, NumberFormatException {
		
		
		client.getCurrPlanFile().setYear(year);
		
		year = year.trim();
		Integer.parseInt(year);
		
		client.pushPlan(client.getCurrPlanFile());
		isSaved = true;
	}

	/**
	 * Adds a new section to the business plan
	 * 
	 * @throws IllegalArgumentException if section cannot be duplicated
	 * @throws RemoteException if cannot connect to server
	 */
	public void addSection() throws IllegalArgumentException, RemoteException {
		client.addBranch();
		this.client.setCurrNode(this.client.getCurrPlanFile().getPlan().getRoot());
		notifyViews();
		isSaved = false;
	}

	/**
	 * removes a section from the business plan
	 * 
	 * @throws IllegalArgumentException if section cannot be deleted
	 * @throws RemoteException if cannot connect to server
	 */
	public void deleteSection() throws IllegalArgumentException, RemoteException {
		client.removeBranch();
		this.client.setCurrNode(this.client.getCurrPlanFile().getPlan().getRoot());
		notifyViews();
		isSaved = false;
	}

	/**
	 * @return boolean indicating if the view has made an edit since last save
	 */
	public boolean isSaved() { return isSaved; }

	/**
	 * @param isSaved boolean indicating if the view has made an edit since last save
	 */
	public void setSaved(boolean isSaved) { this.isSaved = isSaved; }
}
