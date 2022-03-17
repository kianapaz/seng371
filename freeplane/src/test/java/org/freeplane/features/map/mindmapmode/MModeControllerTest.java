package org.freeplane.features.map.mindmapmode;

import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.features.filter.NextNodeAction;
import org.freeplane.features.map.MapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.QuitAction;
import org.freeplane.main.application.ApplicationResourceController;
import org.junit.Test;

import static org.freeplane.main.mindmapmode.MModeControllerFactory.modeActionAdditionController;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MModeControllerTest {

    @Test
    public void AddingActionTest() {
        System.out.println("Test for Adding an Action in MModeController");

        Controller controller = new Controller(new ApplicationResourceController());

        AFreeplaneAction actionA = new QuitAction();
        AFreeplaneAction actionB = new NextNodeAction(MapController.Direction.FORWARD);

        AFreeplaneAction[] actionsToAddOne = new AFreeplaneAction[]{
                actionA,
                actionB
        };

        modeActionAdditionController(controller, actionsToAddOne);

        assertTrue(controller.getActions().contains(actionA));
        assertTrue(controller.getActions().contains(actionB));
        assertEquals(controller.getActions().size(), 18);

        assertEquals(controller.getAction("QuitAction").getKey(), "QuitAction");
        assertEquals(controller.getAction("NextNodeAction.FORWARD").getKey(), "NextNodeAction.FORWARD");
    }
}
