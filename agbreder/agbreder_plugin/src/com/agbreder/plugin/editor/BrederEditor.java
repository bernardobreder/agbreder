package com.agbreder.plugin.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.agbreder.plugin.editor.parser.BrederConfiguration;
import com.agbreder.plugin.element.breder.BClass;

public class BrederEditor extends TextEditor {

	private ColorManager colorManager;

	private BrederContentOutlinePage myOutlinePage;

	public BrederEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new BrederConfiguration(colorManager));
		setDocumentProvider(new FileDocumentProvider());
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			if (myOutlinePage == null && this.getEditorInput() instanceof FileEditorInput) {
				FileEditorInput input = (FileEditorInput) this.getEditorInput();
				BClass element = new BClass(null, input.getFile());
				myOutlinePage = new BrederContentOutlinePage(element);
			}
			return myOutlinePage;
		}
		return super.getAdapter(required);
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		FileDocumentProvider provider = (FileDocumentProvider) this.getDocumentProvider();
		provider.setEncoding(this.getEditorInput(), "utf-8");
		super.doSave(progressMonitor);
	}

}
