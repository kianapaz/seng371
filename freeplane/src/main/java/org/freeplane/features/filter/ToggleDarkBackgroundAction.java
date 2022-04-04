package org.freeplane.features.filter;

import java.awt.*;
import java.awt.event.ActionEvent;

import  org.freeplane.core.ui.AFreeplaneAction;
import  org.freeplane.features.mode.Controller;
import org.freeplane.features.ui.FrameController;


public class ToggleDarkBackgroundAction extends AFreeplaneAction{

    private static final long serialVersionUID = 1L;

    public ToggleDarkBackgroundAction(){
        super("ToggleDarkBackgroundAction");
    }

    public void actionPerformed(final ActionEvent e){
        final FrameController viewController = (FrameController) Controller.getCurrentController().getViewController();
        if(viewController.isBackgroundBlack()){
            viewController.toggleBackground(Color.BLACK);
        }else{
            viewController.toggleBackground(Color.WHITE);
        }
    }

}

