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
public class ClientModel 
{
	 /** A Model-View-Controller model which encapsulates the client from sprint 2.
	  * 
	  */
	 
	private Client client;
	private ArrayList<PlanEditWindow> views = new ArrayList<PlanEditWindow>();
	
	public ClientModel(Client client)
	{
		this.client = client;
	}
	
	/**Adds view
	 * @param window
	 */
	public void addView(PlanEditWindow window)
	{
		views.add(window);
	}
	
	/**Removes view
	 * @param window
	 */
	public void removeView(PlanEditWindow window)
	{
		views.remove(window);
	}
	
	/**Notifies views of a change
	 * 
	 */
	public void notifyViews()
	{
		views.forEach(n -> n.updateWindow());
	}
	
	/**Updates the text content and name of a node
	 * @param node
	 * @param titleText
	 * @param contentText
	 */
	public void updateNodeText(Node node, String titleText, String contentText)
	{
		client.editData(contentText);
		client.editName(titleText);
		client.setCurrNode(node);
		notifyViews();
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * @return name of current node
	 */
	public String getTitle()
	{
		return client.getCurrNode().getName();
	}
	
	/**
	 * @return content of current node
	 */
	public String getContent()
	{
		return client.getCurrNode().getData();
	}
	
	/**
	 * @return current plan
	 */
	public Plan getPlan()
	{
		return client.getCurrPlanFile().getPlan();
	}
	
	/**Logs in
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException
	{
		client.login(username, password);
	}
	
	/**Sets client's plan and notifies views that the model has a plan.
	 * @param year
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void setPlanFile(String year) throws IllegalArgumentException, RemoteException
	{
		client.getPlan(year);
		notifyViews();
	}
	
	/**
	 * @return current planFile
	 */
	public PlanFile getPlanFile()
	{
		return client.getCurrPlanFile();
	}
}
