/**
 * 
 */
package org.freeplane.plugin.script.proxy;

import org.freeplane.api.LengthUnit;
import org.freeplane.api.NodeShape;
import org.freeplane.api.Quantity;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeStyleController;
import org.freeplane.features.nodestyle.NodeStyleShape;
import org.freeplane.features.nodestyle.mindmapmode.MNodeStyleController;
import org.freeplane.features.styles.LogicalStyleController.StyleOption;
import org.freeplane.plugin.script.ScriptContext;

class NodeGeometryProxy extends AbstractProxy<NodeModel> implements Proxy.NodeGeometry {
	private static final NodeStyleShape[] SHAPES = NodeStyleShape.values();
    private static final NodeShape[] NODE_SHAPES = NodeShape.values();

    NodeGeometryProxy(final NodeModel delegate, final ScriptContext scriptContext) {
		super(delegate, scriptContext);
	}

    private MNodeStyleController getStyleController() {
        return (MNodeStyleController) NodeStyleController.getController();
    }

    @Override
    public NodeShape getShape() {
        return NODE_SHAPES[getStyleController().getShape(getDelegate(), StyleOption.FOR_UNSELECTED_NODE).ordinal()];
    }

    @Override
    public Quantity<LengthUnit> getHorizontalMargin() {
        return getStyleController().getShapeConfiguration(getDelegate(), StyleOption.FOR_UNSELECTED_NODE).getHorizontalMargin();
    }

    @Override
    public Quantity<LengthUnit> getVerticalMargin() {
        return getStyleController().getShapeConfiguration(getDelegate(), StyleOption.FOR_UNSELECTED_NODE).getVerticalMargin();
    }

    @Override
    public boolean getIsUniform() {
        return getStyleController().getShapeConfiguration(getDelegate(), StyleOption.FOR_UNSELECTED_NODE).isUniform();
    }

    @Override
    public void setShape(NodeShape shape) {
        getStyleController().setShape(getDelegate(), SHAPES[shape.ordinal()]);
        
    }

    @Override
    public void setHorizontalMargin(Quantity<LengthUnit> length) {
        getStyleController().setShapeHorizontalMargin(getDelegate(), length);
        
    }

    @Override
    public void setVerticalMargin(Quantity<LengthUnit> length) {
        getStyleController().setShapeVerticalMargin(getDelegate(), length);
        
    }

    @Override
    public void setIsUniform(boolean isUniform) {
        getStyleController().setUniformShape(getDelegate(), isUniform);
    }
}
