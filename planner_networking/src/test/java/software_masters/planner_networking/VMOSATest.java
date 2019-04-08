/**
 * 
 */
package software_masters.planner_networking;

import static org.junit.Assert.*;
//import java.util.ArrayList;

import java.rmi.RemoteException;

import org.junit.Test;

/**
 * @author Courtney and Jack
 *
 */
public class VMOSATest {
	@Test
	public void test() throws RemoteException {
		// make a new VMOSA plan
		VMOSA VMOSAPlan = new VMOSA();

		// print out strings in the list
		for (int i = 0; i < VMOSAPlan.getList().size(); i++) { System.out.println(VMOSAPlan.getList().get(i)); }

		// get root node
		Node rootNode = VMOSAPlan.getRoot();
		Node missionNode = rootNode.getChildren().get(0);
		Node objNode = missionNode.getChildren().get(0);
		Node stratNode = objNode.getChildren().get(0);
		Node ActNode = stratNode.getChildren().get(0);
		Node assessNode = ActNode.getChildren().get(0);

		// see that all nodes were added after root
		assertEquals("Vision", rootNode.getName());
		assertEquals("Mission", missionNode.getName());
		assertEquals("Objective", objNode.getName());
		assertEquals("Strategy", stratNode.getName());
		assertEquals("Action Plan", ActNode.getName());
		assertEquals("Assessment", assessNode.getName());

		// try to add vision again and check to see that it wasn't added
		assertEquals(false, rootNode.getChildren().isEmpty());

		VMOSAPlan.addNode(missionNode);
		assertEquals(2, missionNode.getChildren().size());
		Node obj2 = missionNode.getChildren().get(1);
		Node strat2 = obj2.getChildren().get(0);
		Node act2 = strat2.getChildren().get(0);
		Node assess2 = act2.getChildren().get(0);

		assertEquals("Objective", obj2.getName());
		assertEquals("Strategy", strat2.getName());
		assertEquals("Action Plan", act2.getName());
		assertEquals("Assessment", assess2.getName());

		// set pointer to an objective node and remove it
		// check to see if removed
		Node rm = missionNode.getChildren().get(0);
		VMOSAPlan.removeNode(rm);
		assertEquals(1, missionNode.getChildren().size());

		// set data of mission node and check it
		missionNode.setData("hello");
		assertEquals("hello", missionNode.getData());

	}

	// test invalid arguments
	@Test
	public void invalidArguments() throws RemoteException {
		// make a plan and set pointer to root
		Plan VMOSAPlan2 = new VMOSA();
		Node r = VMOSAPlan2.getRoot();
		// try to remove root
		try {
			VMOSAPlan2.removeNode(r);
			fail("My method didn't throw when I expected it to");
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}
		// try to add a vision node
		try {

			VMOSAPlan2.addNode(r);
			fail("My method didn't throw when I expected it to");
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}
	}

}
