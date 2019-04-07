package software_masters.planner_networking;

import java.rmi.RemoteException;

/**
 * @author Courtney and Jack
 * @author wesley and lee.
 */
public class IowaState extends Plan
{

	private static final long serialVersionUID = 3096239674948462908L;

	/**
	 * @throws RemoteException
	 */
	public IowaState() throws RemoteException
	{
		super();
	}

	// set strings for default stages IowaState plan
	/*
	 * (non-Javadoc)
	 * 
	 * @see software_masters.planner_networking.Plan#setDefaultStrings()
	 */
	protected void setDefaultStrings()
	{
		this.getList().add("Vision");
		this.getList().add("Mission");
		this.getList().add("Core Value");
		this.getList().add("Strategy");
		this.getList().add("Goal");
		this.getList().add("Objective");
		this.getList().add("Action Plan");
		this.getList().add("Assessment");
	}

	/**
	 * Takes a Node parent and returns a boolean true if added
	 * 
	 * @param parent parent of node to be added
	 * @return boolean true if added
	 */
	public boolean addNode(Node parent) throws RemoteException, IllegalArgumentException
	{
		int tempCount=0;
		if (parent == null)
		{
			throw new IllegalArgumentException("Cannot add to this parent");
		}
		else if (parent.getName().equals("Vision"))
		{
			throw new IllegalArgumentException("Cannot add to this parent");
		}
		else
		{
			for (int i = (this.getList().indexOf(parent.getName())) + 1; i < this.getList().size(); i++)
			{

				tempCount=this.getIdGen();
				tempCount++;
				this.setIdGen(tempCount);
				Node newNode = new Node(parent, this.getList().get(i), "", this.getIdGen());
				parent.addChild(newNode);
				parent = newNode;

			}
			return true;
		}
	}

	/**
	 * Takes a Node nodeRemove and returns a boolean true if removed
	 * 
	 * @param nodeRemove node to be removed
	 * @return boolean true if removed
	 * 
	 */
	public boolean removeNode(Node nodeRemove) throws IllegalArgumentException
	{
		if (nodeRemove == null)
		{
			throw new IllegalArgumentException("Cannot remove this node");
		}
		else if (nodeRemove.getParent()==null)
		{
			throw new IllegalArgumentException("Cannot remove this node");
		} 
		else if (nodeRemove.getName().equals(this.getRoot().getName()) || nodeRemove.getParent().getChildren().size() == 1)
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
