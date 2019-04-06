/**
 * 
 */
package software_masters.planner_networking;

import java.rmi.RemoteException;

/**
 
 * @author lee.kendall
 *
 */
public class PlanEditController {

	/**
	 * Acts as a Model-View-Controller controller for the plan-editing window.
	 */
	
	private ClientModel model;
	
	public PlanEditController(ClientModel model) 
	{
		// TODO Auto-generated constructor stub
		
		this.model = model;
	}
	
	/**Updates the text content and name of a node
	 * @param node
	 * @param titleText
	 * @param contentText
	 */
	public void updateNodeText(Node node, String titleText, String contentText)
	{
		model.updateNodeText(node, titleText, contentText);
	}
	
	/**Logs in
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void login(String username, String password) throws IllegalArgumentException, RemoteException
	{
		model.login(username, password);
	}
	
	/**Sets client's plan and notifies views that the model has a plan.
	 * @param year
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void setPlanFile(String year) throws IllegalArgumentException, RemoteException
	{
		model.setPlanFile(year);
	}
	
	/**Saves the current state of the planfile after updating the year if changed, if allowed
	 * @param year
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void savePlan(String year) throws IllegalArgumentException, RemoteException
	{
		model.savePlan(year);
	}
	
	/**
	 * Adds a new section to the business plan
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void addSection() throws IllegalArgumentException, RemoteException
	{
		model.addSection();
	}
	
	/**
	 * removes a section from the business plan if allowed
	 * @throws IllegalArgumentException
	 * @throws RemoteException
	 */
	public void deleteSection() throws IllegalArgumentException, RemoteException
	{
		model.deleteSection();
	}

}
