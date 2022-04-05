package org.freeplane.features.ui;

import java.awt.event.ActionEvent;

import  org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.features.ui.FrameController;


public class ToggleDarkBackgroundAction extends AFreeplaneAction{

    private static final long serialVersionUID = 1L;

    public ToggleDarkBackgroundAction(final ViewController viewController){
        super("ToggleDarkBackgroundAction");
    }

    public void actionPerformed(final ActionEvent e){
        if(FrameController.isBackgroundBlack()){
            FrameController.toggleBackground("com.bulenkov.darcula.DarculaLaf");
        }else{
            FrameController.toggleBackground("default");
        }
    }

}

