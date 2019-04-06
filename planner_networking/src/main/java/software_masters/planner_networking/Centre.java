package software_masters.planner_networking;

import java.rmi.RemoteException;

/**
 * @author Courtney and Jack
 * @author wesley and lee.
 */
public class Centre extends Plan
{
	private static final long serialVersionUID = 8094008350302564337L;

	/**
	 * @throws RemoteException
	 */
	public Centre() throws RemoteException
	{
		super();
	}

	// set strings for default stages Centre plan
	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Plan#setDefaultStrings()
	 */
	protected void setDefaultStrings()
	{
		this.getList().add("Mission");
		this.getList().add("Goal");
		this.getList().add("Learning Objective");
		this.getList().add("Assessment Process");
		this.getList().add("Results");
	}

	/**
	 * Take a Node parent and adds the required children and returns a boolean true
	 * if added
	 * 
	 * @param parent parent of node that needs to be added
	 * @return boolean true if added
	 */
	public boolean addNode(Node parent) throws RemoteException, IllegalArgumentException
	{
		if (parent == null)
		{
			throw new IllegalArgumentException("Cannot add to this parent");
		} 
		else
		{
			for (int i = (this.getList().indexOf(parent.getName())) + 1; i < this.getList().size(); i++)
			{

				Node newNode = new Node(parent, this.getList().get(i), null, null);

				parent.addChild(newNode);
				parent = newNode;

			}
			return true;
		}
	}

	/**
	 * Takes a Node nodeRemove and returns a boolean true is removed
	 * 
	 * @param nodeRemove node to be removed
	 * @return boolean true if removed
	 */
	public boolean removeNode(Node nodeRemove) throws IllegalArgumentException
	{
		if (nodeRemove == null) 
		{
			throw new IllegalArgumentException("Cannot remove this node");
		}
		else if ((nodeRemove.getName().equals(this.getRoot().getName())) || nodeRemove.getParent().getChildren().size() == 1)
		{

			throw new IllegalArgumentException("Cannot remove this node");

		} 
		else
		{
			nodeRemove.getParent().removeChild(nodeRemove);
			nodeRemove.setParent(null);
			return true;

		}
	}

}