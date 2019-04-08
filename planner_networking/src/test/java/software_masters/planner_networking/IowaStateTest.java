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
public class IowaStateTest {
	@Test
	public void test() throws RemoteException {
		// make a new VMOSA plan
		Plan IowaStatePlan = new IowaState();

		// print out strings in the list
		for (int i = 0; i < IowaStatePlan.getList().size(); i++) { System.out.println(IowaStatePlan.getList().get(i)); }

		// get root node
		Node rootNode = IowaStatePlan.getRoot();
		Node m = rootNode.getChildren().get(0);
		Node cv = m.getChildren().get(0);
		Node stra = cv.getChildren().get(0);
		Node goal = stra.getChildren().get(0);
		Node obj = goal.getChildren().get(0);
		Node act = obj.getChildren().get(0);
		Node assess = act.getChildren().get(0);

		assertEquals("Vision", rootNode.getName());
		assertEquals("Mission", m.getName());
		assertEquals("Core Value", cv.getName());
		assertEquals("Strategy", stra.getName());
		assertEquals("Goal", goal.getName());
		assertEquals("Objective", obj.getName());
		assertEquals("Action Plan", act.getName());
		assertEquals("Assessment", assess.getName());

		// try to add vision again and check to see that it wasn't added
		assertEquals(false, rootNode.getChildren().isEmpty());
		// add objective, and following, nodes
		// check if added
		Node missionNode = rootNode.getChildren().get(0);

		IowaStatePlan.addNode(missionNode);
		assertEquals(2, missionNode.getChildren().size());
		Node cv2 = missionNode.getChildren().get(0);
		Node stra2 = cv2.getChildren().get(0);
		Node goal2 = stra2.getChildren().get(0);
		Node obj2 = goal2.getChildren().get(0);
		Node act2 = obj2.getChildren().get(0);
		Node assess2 = act2.getChildren().get(0);

		assertEquals("Core Value", cv2.getName());
		assertEquals("Strategy", stra2.getName());
		assertEquals("Goal", goal2.getName());
		assertEquals("Objective", obj2.getName());
		assertEquals("Action Plan", act2.getName());
		assertEquals("Assessment", assess2.getName());

		// remove mission node and check if removed
		Node rm = missionNode.getChildren().get(0);
		IowaStatePlan.removeNode(rm);
		assertEquals(1, missionNode.getChildren().size());

		// set mission data and check
		missionNode.setData("hello");
		assertEquals("hello", missionNode.getData());

	}

	// test invalid arguments
	@Test
	public void invalidArguments() throws RemoteException {
		// make plan and set pointer to root
		Plan IowaStatePlan2 = new IowaState();
		Node r = IowaStatePlan2.getRoot();
		// try to remove root
		try {
			IowaStatePlan2.removeNode(r);
			fail("My method didn't throw when I expected it to");
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}
		// try to add a vision node
		try {

			IowaStatePlan2.addNode(r);
			fail("My method didn't throw when I expected it to");
		} catch (IllegalArgumentException e) {
			e.getMessage();
		}

	}

}
