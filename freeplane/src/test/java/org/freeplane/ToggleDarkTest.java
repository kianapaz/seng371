package org.freeplane;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.freeplane.features.ui.FrameController;
import static org.junit.Assert.assertEquals;

public class ToggleDarkTest {

    @Test
    public void ToggleDarkTest() throws Exception{
        final FrameController frame = mock(FrameController.class);
        assertEquals(frame.getCurrentLookandFeel(), "default");
        frame.toggleBackground("com.bulenkov.darcula.DarculaLaf");
        assertEquals(frame.getCurrentLookandFeel(), "com.bulenkov.darcula.DarculaLaf");
        frame.toggleBackground("default");
        assertEquals(frame.getCurrentLookandFeel(), "default");
    }
}
