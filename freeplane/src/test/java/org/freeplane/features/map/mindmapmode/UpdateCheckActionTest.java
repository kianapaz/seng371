package org.freeplane.features.map.mindmapmode;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.freeplane.main.mindmapmode.UpdateCheckAction.addItemsToGridBag;
import static org.junit.Assert.*;

public class UpdateCheckActionTest {
    final JPanel gridPane = new JPanel(new GridBagLayout());
    final GridBagConstraints gridConstraints = new GridBagConstraints();
    final JLabel emptyLabel = new JLabel("");
    Component[] paneComponents;

    @Before
    public void gridSetUp() {
        paneComponents = new Component[] {emptyLabel, emptyLabel, emptyLabel, emptyLabel, emptyLabel};
        addItemsToGridBag(gridConstraints, gridPane, paneComponents);
    }

    @Test
    public void TestGridPaneItems() throws Exception {

        final long longValue = 5;
        assertEquals(Arrays.stream(paneComponents).count(), longValue);

        Arrays.stream(paneComponents).collect(Collectors.toList()).stream().forEach(value -> {
            assertEquals(value.getAlignmentX(), 0.0, 0);
            assertEquals(value.getAlignmentY(), 0.5, 0);
        });
    }

    @Test
    public void TestGridConstraintItems() throws Exception {
        assertEquals(gridConstraints.gridx, 4);
        assertEquals(gridConstraints.gridy, -1);
    }
}
