package org.freeplane.plugin.jsyntaxpane;

import de.sciss.syntaxpane.SyntaxDocument;
import de.sciss.syntaxpane.Token;
import de.sciss.syntaxpane.actions.ActionUtils;
import de.sciss.syntaxpane.components.SyntaxComponent;
import de.sciss.syntaxpane.util.Configuration;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.map.MapController;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeIdHighLighter implements SyntaxComponent, CaretListener {
	private final Pattern nodeIdPattern = Pattern.compile("(ID_\\d+)|(\"ID_\\d+\")");
	private JEditorPane pane;
	private Status status;
	private ArrayList<NodeModel> nodesOriginallyFolded = new ArrayList<NodeModel>(50);
	private NodeModel originallySelectedNode = null;

	/** remove as soon as SyntaxComponent has it in the JDK5 version. */
	private static enum Status {
        INSTALLING,
        DEINSTALLING
    }

	@Override
	public void caretUpdate(CaretEvent e) {
		handle(e.getDot());
	}

	public void handle(int pos) {
		SyntaxDocument doc = ActionUtils.getSyntaxDocument(pane);
		if (doc != null) {
			try {
				doc.readLock();
				Token token = doc.getTokenAt(pos);
				if (token == null || !handle(doc, token)) {
					deHighlight();
				}
			}
			finally {
				doc.readUnlock();
			}
		}
	}

	private boolean handle(SyntaxDocument doc, Token token) {
		final Matcher matcher = nodeIdPattern.matcher(token.getText(doc));
		if (matcher.matches()) {
			String id = matcher.group(1);
			final NodeModel node = Controller.getCurrentController().getMap().getNodeForID(id);
			if (node != null) {
				final MapController mapController = Controller.getCurrentModeController().getMapController();
				final NodeModel selectedNode = mapController.getSelectedNode();
				if(node.equals(selectedNode)){
					return true;
				}
				NodeModel originallySelectedNode = this.originallySelectedNode;
				if (originallySelectedNode == null)
					originallySelectedNode = mapController.getSelectedNode();
				else{
					deHighlight();
				}
				this.originallySelectedNode = originallySelectedNode;
				mapController.displayNode(node, nodesOriginallyFolded);
				mapController.select(node);
				pane.setToolTipText(node.getText());
				return true;
			}
			else {
				pane.setToolTipText("<html><body bgcolor='#CC0000'>" //
					+ TextUtils.format(getResourceKey("node_is_not_defined"), id) + "</body></html>");
			}
		}
		else{
			 deHighlight();
		}
		return false;
	}

	public void deHighlight() {
		if (originallySelectedNode == null)
			return;
		final Controller controller = Controller.getCurrentController();
		if (controller == null)
			return;
		final MapController mapController = controller.getModeController().getMapController();
		mapController.displayNode(originallySelectedNode);
		mapController.select(originallySelectedNode);
		foldOriginallyFolded(mapController);
		originallySelectedNode = null;
		pane.setToolTipText(null);
	}

	private void foldOriginallyFolded(final MapController mapController) {
		final int countNodesOriginallyUnfolded = nodesOriginallyFolded.size();
		if (countNodesOriginallyUnfolded > 0) {
			for (int i = countNodesOriginallyUnfolded - 1; i >= 0; i--)
				mapController.fold(nodesOriginallyFolded.get(i));
			nodesOriginallyFolded.clear();
		}
	}

	@Override
	public void config(Configuration config) {
	}

	@Override
	public void install(JEditorPane editor) {
		this.pane = editor;
		pane.addCaretListener(this);
		handle(editor.getCaretPosition());
		status = Status.INSTALLING;
		addWindowListener();
	}
	private void addWindowListener() {
		pane.addFocusListener(new FocusAdapter() {
			@Override
            public void focusGained(FocusEvent e) {
				e.getComponent().removeFocusListener(this);
				SwingUtilities.getWindowAncestor(pane).addWindowListener(new WindowAdapter(){
					@Override
		            public void windowClosed(WindowEvent e) {
						e.getWindow().removeWindowListener(this);
						deHighlight();
		            }
				});
            }
		});
	}

	@Override
	public void deinstall(JEditorPane editor) {
		status = Status.DEINSTALLING;
		deHighlight();
		pane.removeCaretListener(this);
		pane = null;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("document")) {
			pane.removeCaretListener(this);
			if (status.equals(Status.INSTALLING)) {
				pane.addCaretListener(this);
				deHighlight();
			}
		}
	}

	public String getResourceKey(final String key) {
		return "org.freeplane.plugin.script.NodeIdHighlighter." + key;
	}
}
