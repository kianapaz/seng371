package org.freeplane.main.application;

import java.awt.*;

public class BorderLayoutWithVisibleCenterComponent extends BorderLayout {

    private static final long serialVersionUID = 8414055824538038007L;

    public static void cardinalAdjustments(Component[] cardinals, int[] cardinalValues, int minimumCenterValue, Component centerComp, boolean northSouth){
        int maximumVal = minimumCenterValue * 9;

        if(cardinalValues[0] > 0 || cardinalValues[1] > 0){
            if(northSouth){
                int newNorthHeight = maximumVal * cardinalValues[0] / (cardinalValues[0] + cardinalValues[1]);
                int northX = cardinals[0] != null ? cardinals[0].getX() : centerComp.getX();
                int northY = cardinals[0] != null ? cardinals[0].getY() : centerComp.getY();
                if(cardinals[0] != null)
                    cardinals[0].setBounds(northX, northY, cardinals[0].getWidth(), newNorthHeight);

                centerComp.setBounds(centerComp.getX(), northY + newNorthHeight, centerComp.getWidth(), minimumCenterValue);

                if(cardinals[1] != null) {
                    int newSouthHeight = maximumVal * cardinalValues[1] / (cardinalValues[0] + cardinalValues[1]);
                    cardinals[1].setBounds(cardinals[1].getX(), northY + newNorthHeight + minimumCenterValue, cardinals[1].getWidth(), newSouthHeight);
                }
            }else{
                int newWestWidth = maximumVal * cardinalValues[0] / (cardinalValues[0] + cardinalValues[1]);
                int westX = cardinals[0] != null ? cardinals[0].getX() : centerComp.getX();
                int westY = cardinals[0] != null ? cardinals[0].getY() : centerComp.getY();
                if(cardinals[0] != null)
                    cardinals[0].setBounds(westX, westY, newWestWidth, cardinals[0].getHeight());

                centerComp.setBounds(westX + newWestWidth, centerComp.getY(), minimumCenterValue, centerComp.getHeight());

                if(cardinals[1] != null) {
                    int newEastWidth = maximumVal * cardinalValues[1] / (cardinalValues[0] + cardinalValues[1]);
                    cardinals[1].setBounds(westX + newWestWidth + minimumCenterValue, cardinals[1].getY(), newEastWidth, cardinals[1].getHeight());
                }

            }
        }
    }

    @Override
    public void layoutContainer(Container target) {
        super.layoutContainer(target);
        Component center = getLayoutComponent(CENTER);
        if(center == null || ! center.isVisible())
            return;
        int centerHeight = center.getHeight();
        int centerWidth = center.getWidth();
        int minimumCenterHeight = target.getHeight() / 10;
        int minimumCenterWidth = target.getWidth() / 10;
        if(centerHeight < minimumCenterHeight) {
            // int maximumEdgeHeight = minimumCenterHeight * 9;
            Component north = getLayoutComponent(NORTH);
            int northHeight = north != null ? north.getHeight() : 0;
            Component south = getLayoutComponent(SOUTH);
            int southHeight = south != null ? south.getHeight() : 0;

            Component[] northSouthObj = new Component[] {north, south};
            int[] northSouthValues = new int[] {northHeight, southHeight};
            cardinalAdjustments(northSouthObj, northSouthValues, minimumCenterHeight, center, true);

        }

        if (centerWidth <  minimumCenterWidth) {
            Component west = getLayoutComponent(WEST);
            int westWidth = west != null ? west.getWidth() : 0;
            Component east = getLayoutComponent(EAST);
            int eastWidth = east != null ? east.getWidth() : 0;

            Component[] westEastObj = new Component[] {west, east};
            int[] westEastValues = new int[] {westWidth, eastWidth};
            cardinalAdjustments(westEastObj, westEastValues, minimumCenterWidth, center, false);

        }
    }

}