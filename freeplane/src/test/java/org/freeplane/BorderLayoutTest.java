package org.freeplane;

import org.junit.Test;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;

import static org.freeplane.main.application.BorderLayoutWithVisibleCenterComponent.cardinalAdjustments;
import static org.junit.Assert.assertEquals;

public class BorderLayoutTest extends BorderLayout {

    @Test
    public void BorderLayoutTest() throws Exception {

        Container center = null;
        int minimumCenterHeight = 5 / 10;
        Component north = getLayoutComponent(NORTH);
        int northHeight = north != null ? north.getHeight() : 0;
        Component south = getLayoutComponent(SOUTH);
        int southHeight = south != null ? south.getHeight() : 0;

        Component[] northSouthObj = new Component[] {north, south};
        int[] northSouthValues = new int[] {northHeight, southHeight};

        cardinalAdjustments(northSouthObj, northSouthValues, minimumCenterHeight, center, true);

        assertEquals(northSouthObj.length, 2);
        assertEquals(northSouthValues.length, 2);

        Arrays.stream(northSouthValues).forEach(northSouthValue -> {
            assertEquals(northSouthValue, 0);
        });
    }
}
