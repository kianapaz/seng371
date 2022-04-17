package org.freeplane.features.map;

import org.freeplane.api.LengthUnit;
import org.freeplane.api.Node;
import org.freeplane.api.Quantity;
import org.freeplane.core.ui.components.UITools;
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

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
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

        @Test
        public void assertGraphChanges() throws Exception {
            MapModel map = mock(MapModel.class);
            NodeModel node = new NodeModel(map);
            NodeModel root = new NodeModel(map);

            Container parent = mock(Container.class);
            NodeView nodeView = mock(NodeView.class);
            LinkedList<Point> testingPoint = new LinkedList<Point>();
            Point point = new Point(0, 2);
            testingPoint.add(point);
            nodeView.getCoordinates(testingPoint);
            when(map.getRootNode()).thenReturn(root);


        }

        @Test
        public void assertVerticalDistance() {
            final NodeView nodeView = Mockito.mock(NodeView.class);

            int quantity = 40;
            NodeView view = mock(NodeView.class);
            when(nodeView.getChildDistanceContainer()).thenReturn(view);
            when(nodeView.getMinimalDistanceBetweenChildren()).thenReturn(quantity);

            assertThat(nodeView.getMinimalDistanceBetweenChildren(), equalTo(40));
            assertThat(nodeView.getChildDistanceContainer(), equalTo(view));

        }

        @Test
        public void assertRectangleDimensions() throws Exception {
            MapScroller mapScroll = mock(MapScroller.class);
            Rectangle rectangle = new Rectangle(0, 0, -1, -1);
            when(mapScroll.calculateOptimalVisibleRectangle()).thenReturn(rectangle);
            Rectangle rec = mapScroll.calculateOptimalVisibleRectangle();

            assertThat(rec, equalTo(rectangle));
        }

        @Ignore
        @Test
        public void assertNodeDistance() {
            final MapModel mockMapModel = Mockito.mock(MapModel.class);
            final ModeController modeController = Mockito.mock(ModeController.class, Mockito.RETURNS_DEEP_STUBS);
            final MapView mapView = new MapView(mockMapModel, modeController);
            final NodeModel node = Mockito.mock(NodeModel.class);
            NodeView nodeView = mock(NodeView.class);
            VerticalNodeViewLayoutStrategy nodeViewLayoutStrategy = new VerticalNodeViewLayoutStrategy(nodeView);
            int properDistanceValue = 10;
            when(nodeViewLayoutStrategy.summarizedNodeDistance(10)).thenReturn(properDistanceValue);
            int distance = nodeViewLayoutStrategy.summarizedNodeDistance(10);

            assertThat(distance, equalTo(properDistanceValue));
        }


    }
}
