/**
 * 
 */
package software_masters.planner_networking;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author lee.kendall
 *
 */
public class ClientModel {
	/**
	 * A Model-View-Controller model which encapsulates the client from sprint 2.
	 * 
	 */

	private Client client;
	private ArrayList<PlanEditWindow> views = new ArrayList<PlanEditWindow>();
	private boolean isSaved;

	public ClientModel(Client client) { this.client = client; }

	/**
	 * Adds view
	 * 
	 * @param window
	 */
	public void addView(PlanEditWindow window) { views.add(window); }

	/**
	 * Removes view
	 * 
	 * @param window
	 */
	public void removeView(PlanEditWindow window) { views.remove(window); }

	/**
	 * Notifies views of a change
	 * 
	 */
	public void notifyViews() { views.forEach(n -> n.updatePlan()); }

	/**
	 * Updates the text content and name of a node, and changes currently accessed
	 * node
	 * 
	 * @param node
	 * @param titleText
	 * @param contentText
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
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException {
		client.login(username, password);
	}

	/**
	 * Sets client's plan and notifies views that the model has a plan.
	 * 
	 * @param year
	 * @throws IllegalArgumentException
	 * @throws RemoteException
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
	 * Saves the current state of the planfile after updating the year if changed,
	 * if allowed
	 * 
	 * @param year
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void savePlan(String year) throws IllegalArgumentException, RemoteException {
		client.getCurrPlanFile().setYear(year);
		client.pushPlan(client.getCurrPlanFile());
		isSaved = true;
	}

	/**
	 * Adds a new section to the business plan
	 * 
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void addSection() throws IllegalArgumentException, RemoteException {
		client.addBranch();
		this.client.setCurrNode(this.client.getCurrPlanFile().getPlan().getRoot());
		notifyViews();
		isSaved = false;
	}

	/**
	 * removes a section from the business plan if allowed
	 * 
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void deleteSection() throws IllegalArgumentException, RemoteException {
		client.removeBranch();
		this.client.setCurrNode(this.client.getCurrPlanFile().getPlan().getRoot());
		notifyViews();
		isSaved = false;
	}

	/**
	 * @return the isSaved
	 */
	public boolean isSaved() { return isSaved; }

	/**
	 * @param isSaved the isSaved to set
	 */
	public void setSaved(boolean isSaved) { this.isSaved = isSaved; }
}
