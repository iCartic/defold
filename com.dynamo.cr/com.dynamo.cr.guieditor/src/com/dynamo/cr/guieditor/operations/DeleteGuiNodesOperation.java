package com.dynamo.cr.guieditor.operations;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.dynamo.cr.guieditor.IGuiEditor;
import com.dynamo.cr.guieditor.scene.GuiNode;
import com.dynamo.cr.guieditor.scene.GuiScene;

public class DeleteGuiNodesOperation extends AbstractOperation {


    private IGuiEditor editor;
    private List<GuiNode> nodes;

    public DeleteGuiNodesOperation(IGuiEditor editor, List<GuiNode> nodes) {
        super("Delete Node(s)");
        this.editor = editor;
        this.nodes = nodes;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
        GuiScene scene = editor.getScene();
        editor.getSelectionProvider().setSelection(Collections.<GuiNode>emptyList());
        scene.removeNodes(nodes);
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
        GuiScene scene = editor.getScene();
        editor.getSelectionProvider().setSelection(Collections.<GuiNode>emptyList());
        scene.removeNodes(nodes);
        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info)
            throws ExecutionException {
        GuiScene scene = editor.getScene();
        scene.addNodes(nodes);
        editor.getSelectionProvider().setSelection(nodes);
        return Status.OK_STATUS;
    }

}