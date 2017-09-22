package com.agbreder.plugin.editor.parser;

import org.eclipse.jface.text.DefaultTextHover;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;

public class BrederConfiguration extends SourceViewerConfiguration {

	private BrederDoubleClickStrategy doubleClickStrategy;
	private BrederScanner scanner;
	private ColorManager colorManager;

	public BrederConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new BrederDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected BrederScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new BrederScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(colorManager.getColor(ColorConstants.DEFAULT))));
		}
		return scanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new BrederScanner(colorManager));
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		return new DefaultTextHover(sourceViewer);
	}

}