package software_masters.planner_networking;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Courtney and Jack
 * @author wesley and lee.
 *
 */
public abstract class Plan implements Serializable// extends UnicastRemoteObject
{
	private static final long serialVersionUID = 1538776243780396317L;
	private String name;
	private ArrayList<String> defaultNodes = new ArrayList<String>();
	private Node root;
	private int idGen=0;

	/**
	 * @throws RemoteException
	 */
	public Plan() throws RemoteException
	{
		defaultNodes = new ArrayList<String>();
		setDefaultStrings();
		addDefaultNodes();
	}

	// creates string array node hierarchy in subclass
	abstract protected void setDefaultStrings();

	/**
	 * This class builds default template based on string array
	 * 
	 * @throws RemoteException
	 */
	protected void addDefaultNodes() throws RemoteException
	{
		idGen++;
		root=new Node(null,this.defaultNodes.get(0),"", idGen);
		Node current=root,temp;
		for(int i=1;i<this.defaultNodes.size();i++) {
			idGen++;
			temp=new Node(current,this.defaultNodes.get(i),"", idGen);
			current.addChild(temp);
			current=temp;
		}
	}

	/**
	 * @param parent
	 * @return
	 */
	abstract public boolean addNode(Node parent) throws RemoteException, IllegalArgumentException;

	/**
	 * @param Node
	 * @return
	 */
	abstract public boolean removeNode(Node Node) throws IllegalArgumentException;

	/**
	 * Takes a Node node and String data Sets data for the node
	 * 
	 * @param node node to set data for
	 * @param data data to set in node
	 * 
	 */
	public void setNodeData(Node node, String data)
	{
		node.setData(data);
	}

	/**
	 * returns the root node
	 * 
	 * @return Node root node
	 * 
	 */
	public Node getRoot()
	{
		return root;
	}

	/**
	 * returns a list of default node strings
	 * 
	 * @return ArrayList list of default node strings
	 */
	public ArrayList<String> getList()
	{
		return defaultNodes;
	}

	/**
	 * returns a String name of plan
	 * 
	 * @return String strings of plan name
	 * 
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Takes a String name and sets name of plan
	 * 
	 * @param name name to set as plan name
	 * 
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * @return the defaultNodes
	 */
	public ArrayList<String> getDefaultNodes() {
		return defaultNodes;
	}

	/**
	 * @param defaultNodes the defaultNodes to set
	 */
	public void setDefaultNodes(ArrayList<String> defaultNodes) {
		this.defaultNodes = defaultNodes;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * @return the idGen
	 */
	public int getIdGen() {
		return idGen;
	}

	/**
	 * @param idGen the idGen to set
	 */
	public void setIdGen(int idGen) {
		this.idGen = idGen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plan other = (Plan) obj;
		if (defaultNodes == null)
		{
			if (other.defaultNodes != null)
				return false;
		} else if (!defaultNodes.equals(other.defaultNodes))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (root == null)
		{
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		return true;
	}
}