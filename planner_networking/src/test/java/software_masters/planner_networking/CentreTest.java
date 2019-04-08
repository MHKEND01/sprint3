/**
 * 
 */
package software_masters.planner_networking;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Test;

/**
 * @author Courtney and Jack
 *
 */
public class CentreTest {

	@Test
	public void test() throws RemoteException {
		// make a new Centre plan
		Plan CentrePlan = new Centre();

		// print out strings in the list
		for (int i = 0; i < CentrePlan.getList().size(); i++) { System.out.println(CentrePlan.getList().get(i)); }

		// get root node
		Node rootNode = CentrePlan.getRoot();
		Node goal = rootNode.getChildren().get(0);
		Node learn = goal.getChildren().get(0);
		Node assess = learn.getChildren().get(0);
		Node res = assess.getChildren().get(0);

		assertEquals("Mission", rootNode.getName());
		assertEquals("Goal", goal.getName());
		assertEquals("Learning Objective", learn.getName());
		assertEquals("Assessment Process", assess.getName());
		assertEquals("Results", res.getName());

		// try to add mission again and check to see that it wasn't added
		assertEquals(false, rootNode.getChildren().isEmpty());
		// add goal, and following, nodes
		CentrePlan.addNode(rootNode);
		assertEquals(2, rootNode.getChildren().size());
		Node goalNode = rootNode.getChildren().get(0);
		Node learn2 = goalNode.getChildren().get(0);
		Node assess2 = learn2.getChildren().get(0);
		Node res2 = assess2.getChildren().get(0);

		assertEquals("Goal", goalNode.getName());
		assertEquals("Learning Objective", learn2.getName());
		assertEquals("Assessment Process", assess2.getName());
		assertEquals("Results", res2.getName());

		// remove a goal node
		Node rm = rootNode.getChildren().get(0);
		// check to see if removed
		CentrePlan.removeNode(rm);
		assertEquals(1, rootNode.getChildren().size());
		// set goal data and check it
		goalNode.setData("hello");
		assertEquals("hello", goalNode.getData());

	}

	// test invalid arguments
	@Test
	public void invalidArguments() throws RemoteException {
		// make plan and set pointer to root
		Plan CentrePlan2 = new Centre();
		Node r = CentrePlan2.getRoot();
		// try to remove root
		try {
			CentrePlan2.removeNode(r);
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}
		// try to add a mission node
		try {
			CentrePlan2.addNode(r);
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}
	}

}
