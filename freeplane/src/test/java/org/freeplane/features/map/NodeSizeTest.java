package org.freeplane.features.map;

import org.freeplane.api.LengthUnit;
import org.freeplane.api.Node;
import org.freeplane.api.Quantity;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.view.swing.map.MapScroller;
import org.freeplane.view.swing.map.MapView;
import org.freeplane.view.swing.map.NodeView;
import org.freeplane.view.swing.map.VerticalNodeViewLayoutStrategy;
import org.freeplane.view.swing.map.cloud.CloudView;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import java.awt.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

public class NodeSizeTest {
    protected static MapView mockMapView;
    protected static NodeModel mockNodeModel;
    static MapModel map = null;

    protected static MapFake mockMap;

    @Before
    public void setup(){
        mockMap = new MapFake();
    }

    public static class MockMapNodeSize extends SummaryLevelsShould{

        @Test
        public void assertRootSummaryViewLevel() throws Exception {
            mapFake.addNode("Node A");
            final SummaryLevels summaryLevels = new SummaryLevels(mapFake.getRoot());
            assertThat(summaryLevels.summaryLevels.length, equalTo(1));
        }

        @Ignore
        @Test
        public void assertChildNodeMinimumLevel() throws Exception {
            final MapModel mockMapModel = Mockito.mock(MapModel.class);
            final ModeController modeController = Mockito.mock(ModeController.class, Mockito.RETURNS_DEEP_STUBS);
            final MapView mapView = new MapView(mockMapModel, modeController);
            final NodeModel node = Mockito.mock(NodeModel.class);
            final NodeView nodeView = Mockito.mock(NodeView.class);
            new VerticalNodeViewLayoutStrategy(nodeView);

            assertThat(nodeView.getMinimalDistanceBetweenChildren(), equalTo(-1));
        }

        @Test
        public void assertAdditionHeightView() throws Exception {
            mapFake.addGroupBeginNode();
            mapFake.addNode("Node A");
            mapFake.addSummaryNode();
            mapFake.addGroupBeginNode();
            mapFake.addNode("Node B");
            mapFake.addSummaryNode();
            NodeModel summaryNode = mapFake.addSummaryNode();
            final SummaryLevels summaryLevels = new SummaryLevels(mapFake.getRoot());
            assertThat(summaryLevels.findSummaryNode(2), equalTo(summaryNode));
            assertThat(summaryLevels.highestSummaryLevel, equalTo(2));
            assertThat(summaryLevels.findSummaryNodeIndex(0), equalTo(2));

        }

    }

}
