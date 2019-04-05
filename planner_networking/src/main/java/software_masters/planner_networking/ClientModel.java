/**
 * 
 */
package software_masters.planner_networking;

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
		
	}
}
