/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.main.mindmapmode;

import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.*;
import org.freeplane.core.ui.components.FButtonBar;
import org.freeplane.core.ui.components.FreeplaneToolBar;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.ui.components.resizer.CollapseableBoxBuilder;
import org.freeplane.core.ui.components.resizer.JResizer.Direction;
import org.freeplane.core.ui.components.resizer.UIComponentVisibilityDispatcher;
import org.freeplane.core.ui.menubuilders.action.ComponentBuilder;
import org.freeplane.core.ui.menubuilders.generic.Entry;
import org.freeplane.core.ui.menubuilders.generic.EntryVisitor;
import org.freeplane.core.ui.menubuilders.generic.PhaseProcessor.Phase;
import org.freeplane.core.ui.menubuilders.menu.ComponentProvider;
import org.freeplane.core.ui.menubuilders.menu.JToolbarComponentBuilder;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.attribute.AttributeController;
import org.freeplane.features.attribute.mindmapmode.*;
import org.freeplane.features.clipboard.ClipboardControllers;
import org.freeplane.features.clipboard.mindmapmode.MClipboardControllers;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.mindmapmode.MCloudController;
import org.freeplane.features.edge.AutomaticEdgeColorHook;
import org.freeplane.features.edge.EdgeController;
import org.freeplane.features.edge.mindmapmode.MEdgeController;
import org.freeplane.features.encrypt.mindmapmode.MEncryptionController;
import org.freeplane.features.explorer.mindmapmode.MMapExplorerController;
import org.freeplane.features.export.mindmapmode.ExportController;
import org.freeplane.features.export.mindmapmode.ImportMindmanagerFiles;
import org.freeplane.features.export.mindmapmode.ImportXmlFile;
import org.freeplane.features.filter.FilterController;
import org.freeplane.features.filter.hidden.HiddenNodeContoller;
import org.freeplane.features.icon.IconController;
import org.freeplane.features.icon.hierarchicalicons.HierarchicalIcons;
import org.freeplane.features.icon.mindmapmode.IconSelectionPlugin;
import org.freeplane.features.icon.mindmapmode.MIconController;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.link.mindmapmode.MLinkController;
import org.freeplane.features.map.*;
import org.freeplane.features.map.mindmapmode.*;
import org.freeplane.features.mapio.mindmapmode.MMapIO;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.features.nodelocation.LocationController;
import org.freeplane.features.nodelocation.mindmapmode.MLocationController;
import org.freeplane.features.nodestyle.NodeStyleController;
import org.freeplane.features.nodestyle.mindmapmode.MNodeStyleController;
import org.freeplane.features.nodestyle.mindmapmode.RevisionPlugin;
import org.freeplane.features.note.NoteController;
import org.freeplane.features.note.mindmapmode.MNoteController;
import org.freeplane.features.presentations.mindmapmode.PresentationController;
import org.freeplane.features.spellchecker.mindmapmode.SpellCheckerController;
import org.freeplane.features.styles.AutomaticLayoutController;
import org.freeplane.features.styles.LogicalStyleController;
import org.freeplane.features.styles.MapStyle;
import org.freeplane.features.styles.mindmapmode.MLogicalStyleController;
import org.freeplane.features.styles.mindmapmode.MUIFactory;
import org.freeplane.features.styles.mindmapmode.ShowFormatPanelAction;
import org.freeplane.features.styles.mindmapmode.styleeditorpanel.StyleEditorPanel;
import org.freeplane.features.text.mindmapmode.MTextController;
import org.freeplane.features.text.mindmapmode.SortNodes;
import org.freeplane.features.text.mindmapmode.SplitNode;
import org.freeplane.features.time.CreationModificationDatePresenter;
import org.freeplane.features.ui.FrameController;
import org.freeplane.features.ui.ToggleToolbarAction;
import org.freeplane.features.ui.ViewController;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;
import org.freeplane.features.url.mindmapmode.SaveAll;
import org.freeplane.main.mindmapmode.stylemode.SModeControllerFactory;
import org.freeplane.view.swing.features.BlinkingNodeHook;
import org.freeplane.view.swing.features.FitToPage;
import org.freeplane.view.swing.features.filepreview.AddExternalImageAction;
import org.freeplane.view.swing.features.filepreview.RemoveExternalImageAction;
import org.freeplane.view.swing.features.filepreview.ViewerController;
import org.freeplane.view.swing.features.nodehistory.NodeHistory;
import org.freeplane.view.swing.features.progress.mindmapmode.ProgressFactory;
import org.freeplane.view.swing.features.time.mindmapmode.ReminderHook;
import org.freeplane.view.swing.map.ShowNotesInMapAction;
import org.freeplane.view.swing.map.attribute.AttributePanelManager;
import org.freeplane.view.swing.map.attribute.EditAttributesAction;
import org.freeplane.view.swing.ui.DefaultNodeKeyListener;
import org.freeplane.view.swing.ui.UserInputListenerFactory;
import org.freeplane.view.swing.ui.mindmapmode.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * @author Dimitry Polivaev 24.11.2008
 */
public class MModeControllerFactory {
	private static MModeControllerFactory instance;

	public static MModeController createModeController() {
		return MModeControllerFactory.getInstance().createModeControllerImpl();
	}

	private static MModeControllerFactory getInstance() {
		if (instance == null) {
			instance = new MModeControllerFactory();
		}
		return instance;
	}

 	public static MModeController modeController;
	private MUIFactory uiFactory;

    public static void modeActionAddition(MModeController controller, AFreeplaneAction[] addThese){
		Arrays.stream(addThese).forEach(itemToAdd -> {
			controller.addAction(itemToAdd);
		});
    }

	public static void modeActionAdditionController(Controller controller, AFreeplaneAction[] addThese){
		Arrays.stream(addThese).forEach(itemToAdd -> {
			controller.addAction(itemToAdd);
		});
	}

	private void createAddIns() {
		final StyleEditorPanel panel = new StyleEditorPanel(modeController, uiFactory, true);
		final JScrollPane styleScrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		UITools.setScrollbarIncrement(styleScrollPane);
		final JTabbedPane tabs = UITools.getFreeplaneTabbedPanel();

		tabs.add(TextUtils.getText("format_panel"), styleScrollPane);
		tabs.add(TextUtils.getText("attributes_attribute"), createAttributesPanel());

		HierarchicalIcons.install(modeController);
		new AutomaticLayoutController();
		new BlinkingNodeHook();

		SummaryNode.install();

		final MMapController mapController = (MMapController) modeController.getMapController();
		mapController.addMapLifeCycleListener(new SummaryNodeMapUpdater(modeController, mapController));
		final AlwaysUnfoldedNode alwaysUnfoldedNode = new AlwaysUnfoldedNode();
        modeActionAddition(modeController, new AFreeplaneAction[]{new SetAlwaysUnfoldedNodeFlagsAction(alwaysUnfoldedNode),
				new RemoveAllAlwaysUnfoldedNodeFlagsAction(alwaysUnfoldedNode)});

		FreeNode.install();
		new CreationModificationDatePresenter();
		modeController.addExtension(ReminderHook.class, new ReminderHook(modeController));
		new AutomaticEdgeColorHook();
		new ViewerController();
		PresentationController.install(modeController);

		AFreeplaneAction[] actionsToAddOne = new AFreeplaneAction[]{
            new AddAttributeAction(),
            new RemoveFirstAttributeAction(),
            new RemoveLastAttributeAction(),
            new RemoveAllAttributesAction(),
            new AddExternalImageAction(),
            new RemoveExternalImageAction(),
            new ShowFormatPanelAction(),
            new FitToPage(),
            new UpdateCheckAction(modeController.getController())
        };

        modeActionAddition(modeController, actionsToAddOne);

		MEncryptionController.install(new MEncryptionController(modeController));
        AFreeplaneAction[] actionsToAddTwo = new AFreeplaneAction[]{
            new IconSelectionPlugin(),
            new NewParentNode(),
            new SaveAll(),
            new SortNodes(),
            new SplitNode()    
        };

        modeActionAddition(modeController, actionsToAddTwo);

		new ChangeNodeLevelController(modeController);
		NodeHistory.install(modeController);

        modeActionAddition(modeController, new AFreeplaneAction[]{new ImportXmlFile(), new ImportMindmanagerFiles()});
	}

	private JComponent createAttributesPanel() {
		final JPanel tablePanel = new AttributePanelManager(modeController).getTablePanel();
		final JScrollPane attributeScrollPane = new JScrollPane(tablePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		UITools.setScrollbarIncrement(attributeScrollPane);
		return attributeScrollPane;
	}

	private MModeController createModeControllerImpl() {
		createStandardControllers();
		createAddIns();
		return modeController;
	}

	private void createStandardControllers() {
		final Controller controller = Controller.getCurrentController();
		modeController = new MModeController(controller);

		final UserInputListenerFactory userInputListenerFactory = new UserInputListenerFactory(modeController);
        final IMouseListener nodeMouseMotionListener = new MNodeMotionListener();
		final JPopupMenu popupmenu = new JPopupMenu();
		final MFileManager fileManager = new MFileManager();
		final MapController mapController = modeController.getMapController();
		final MTextController textController = new MTextController(modeController);
		final MLogicalStyleController logicalStyleController = new MLogicalStyleController(modeController);

		userInputListenerFactory.setNodeMouseMotionListener(nodeMouseMotionListener);
		userInputListenerFactory.setNodeMouseWheelListener(new MNodeMouseWheelListener(userInputListenerFactory.getMapMouseWheelListener()));
		userInputListenerFactory.setNodePopupMenu(popupmenu);

		modeController.setUserInputListenerFactory(userInputListenerFactory);

		controller.addModeController(modeController);
		controller.selectModeForBuild(modeController);

		ClipboardControllers.install(new MClipboardControllers());
		new MMapController(modeController);

		UrlManager.install(fileManager);
		MMapIO.install(modeController);

		controller.getMapViewManager().addMapViewChangeListener(fileManager);
		new MIconController(modeController).install(modeController);
		new ProgressFactory().installActions(modeController);

		EdgeController.install(new MEdgeController(modeController));

		CloudController.install(new MCloudController(modeController));
		NoteController.install(new MNoteController(modeController));
		userInputListenerFactory.setMapMouseListener(new MMapMouseListener());

		textController.install(modeController);
		MMapExplorerController.install(modeController, textController);

		LinkController.install(new MLinkController(modeController));

		NodeStyleController.install(new MNodeStyleController(modeController));
		userInputListenerFactory.setNodeDragListener(new MNodeDragListener());
		userInputListenerFactory.setNodeDropTargetListener(new MNodeDropListener());

		LocationController.install(new MLocationController());

		LogicalStyleController.install(logicalStyleController);
		logicalStyleController.initM();
		AttributeController.install(new MAttributeController(modeController));
		userInputListenerFactory.setNodeKeyListener(new DefaultNodeKeyListener(new IEditHandler() {
			@Override
			public void edit(final KeyEvent e, final FirstAction action, final boolean editLong) {
				((MTextController) MTextController.getController(modeController)).getEventQueue().activate(e);
				textController.edit(action, editLong);
			}
		}));
		userInputListenerFactory.setNodeMotionListener(new MNodeMotionListener());

		modeActionAddition(modeController, new AFreeplaneAction[]{new EditAttributesAction()});

		SpellCheckerController.install(modeController);
		ExportController.install(new ExportController("/xml/ExportWithXSLT.xml"));
		MapStyle.install(true);

		final FreeplaneToolBar toolbar = new FreeplaneToolBar("main_toolbar", SwingConstants.HORIZONTAL);
		final FrameController frameController = (FrameController) controller.getViewController();
		final JTabbedPane formattingPanel = UITools.getFreeplaneTabbedPanel();
		final JRootPane rootPane = ((RootPaneContainer)frameController.getMenuComponent()).getRootPane();
		final FButtonBar fButtonToolBar = new FButtonBar(rootPane);


		UIComponentVisibilityDispatcher.install(toolbar, "toolbarVisible");
		userInputListenerFactory.addToolBar("/main_toolbar", ViewController.TOP, toolbar);
		userInputListenerFactory.addToolBar("/filter_toolbar", FilterController.TOOLBAR_SIDE, FilterController.getController(controller).getFilterToolbar());
		userInputListenerFactory.addToolBar("/status", ViewController.BOTTOM, frameController
		    .getStatusBar());

		Box resisableTabs = new CollapseableBoxBuilder("styleScrollPaneVisible").createBox(formattingPanel, Direction.RIGHT);
		userInputListenerFactory.addToolBar("/format", ViewController.RIGHT, resisableTabs);

		UIComponentVisibilityDispatcher.install(fButtonToolBar, "fbarVisible");
		fButtonToolBar.setVisible(ResourceController.getResourceController().getBooleanProperty("fbarVisible"));
		userInputListenerFactory.addToolBar("/fbuttons", ViewController.TOP, fButtonToolBar);

		userInputListenerFactory.setKeyEventProcessor(new IKeyStrokeProcessor() {
			@Override
			public boolean processKeyBinding(KeyStroke ks, KeyEvent e) {
				return ResourceController.getResourceController().getAcceleratorManager().processKeyBinding(ks, e) || fButtonToolBar.processKeyBinding(ks, e);
			}
		});

		modeActionAdditionController(controller, new AFreeplaneAction[]{new ToggleToolbarAction("ToggleFBarAction", "/fbuttons")});
		SModeControllerFactory.install();


		modeActionAddition(modeController, new AFreeplaneAction[]{new ShowNotesInMapAction(), new SetAcceleratorOnNextClickAction()});

		ResourceController.getResourceController().getAcceleratorManager().addAcceleratorChangeListener(modeController, fButtonToolBar);
		userInputListenerFactory.addToolBar("/icon_toolbar", ViewController.LEFT, ((MIconController) IconController
		    .getController()).getIconToolBarScrollPane());

		modeActionAddition(modeController, new AFreeplaneAction[]{new ToggleToolbarAction("ToggleLeftToolbarAction", "/icon_toolbar")});

		new RevisionPlugin();
		FoldingController.install(new FoldingController());

		uiFactory = new MUIFactory();
		mapController.addUINodeChangeListener(uiFactory);
		mapController.addNodeSelectionListener(uiFactory);
		mapController.addUIMapChangeListener(uiFactory);
		controller.getMapViewManager().addMapSelectionListener(uiFactory);
		modeController.addExtension(MUIFactory.class, uiFactory);

		modeController.addUiBuilder(Phase.ACTIONS, "main_toolbar_font_name", new ComponentBuilder(
		    new ComponentProvider() {
			    @Override
			    public Component createComponent(Entry entry) {
				    final Container fontBox = uiFactory.createFontBox();
				    return fontBox;
			    }
		    }), EntryVisitor.EMTPY);


		modeController.addUiBuilder(Phase.ACTIONS, "main_toolbar_font_size", new ComponentBuilder(
		    new ComponentProvider() {
			    @Override
			    public Component createComponent(Entry entry) {
				    return uiFactory.createSizeBox();
			    }
		    }), EntryVisitor.EMTPY);


		modeController.addUiBuilder(Phase.ACTIONS, "main_toolbar_style", new ComponentBuilder(
		    new ComponentProvider() {
			    @Override
			    public Component createComponent(Entry entry) {
				    return uiFactory.createStyleBox();
			    }
		    }), EntryVisitor.EMTPY);

		modeController.addUiBuilder(Phase.UI, "main_toolbar_zoom", new JToolbarComponentBuilder(
		    new ComponentProvider() {
			    @Override
			    public Component createComponent(Entry entry) {
				    return controller.getMapViewManager().createZoomBox();
			    }
		    }));

		HiddenNodeContoller.registerModeSpecificActions(modeController);
		HiddenNodeContoller.install(modeController);
	}
}
