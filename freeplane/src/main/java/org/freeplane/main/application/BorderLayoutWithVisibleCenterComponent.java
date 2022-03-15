package org.freeplane.main.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

class BorderLayoutWithVisibleCenterComponent extends BorderLayout {

    private static final long serialVersionUID = 8414055824538038007L;

    private void cardinalAdjustments(Component[] cardinals, int[] cardinalValues, int minimumCenterValue, Component centerComp, boolean northSouth){
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

            // if(northHeight > 0 || southHeight > 0) {
            //     int newNorthHeight = maximumEdgeHeight * northHeight / (northHeight + southHeight);
            //     int northX = north != null ? north.getX() : center.getX();
            //     int northY = north != null ? north.getY() : center.getY();
            //     if(north != null)
            //         north.setBounds(northX, northY, north.getWidth(), newNorthHeight);
            //     center.setBounds(center.getX(), northY + newNorthHeight, center.getWidth(), minimumCenterHeight);
            //     if(south != null) {
            //         int newSouthHeight = maximumEdgeHeight * southHeight / (northHeight + southHeight);
            //         south.setBounds(south.getX(), northY + newNorthHeight + minimumCenterHeight, south.getWidth(), newSouthHeight);
            //     }
            // }
        }
        if (centerWidth <  minimumCenterWidth) {
            // int maximumEdgeWidth = minimumCenterWidth * 9;
            Component west = getLayoutComponent(WEST);
            int westWidth = west != null ? west.getWidth() : 0;
            Component east = getLayoutComponent(EAST);
            int eastWidth = east != null ? east.getWidth() : 0;

            Component[] westEastObj = new Component[] {west, east};
            int[] westEastValues = new int[] {westWidth, eastWidth};
            cardinalAdjustments(westEastObj, westEastValues, minimumCenterWidth, center, false);

            // if(westWidth > 0 || eastWidth > 0) {
            //     int newWestWidth = maximumEdgeWidth * westWidth / (westWidth + eastWidth);
            //     int westX = west != null ? west.getX() : center.getX();
            //     int westY = west != null ? west.getY() : center.getY();
            //     if(west != null)
            //         west.setBounds(westX, westY, newWestWidth, west.getHeight());
            //     center.setBounds(westX + newWestWidth, center.getY(), minimumCenterWidth, center.getHeight());
            //     if(east != null) {
            //         int newEastWidth = maximumEdgeWidth * eastWidth / (westWidth + eastWidth);
            //         east.setBounds(westX + newWestWidth + minimumCenterWidth, east.getY(),  newEastWidth, east.getHeight());
            //     }
            // }
        }
    }

}