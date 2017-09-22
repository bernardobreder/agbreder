package com.agbreder.plugin.wizard;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.ElementManager;
import com.agbreder.plugin.wizard.provider.content.BRootContentProvider;
import com.agbreder.plugin.wizard.provider.content.BTreeContentProvider;
import com.agbreder.plugin.wizard.provider.label.BLabelProvider;

public class BWizardUtil {

	public static Element chooseContainer(ViewerFilter filter, ISelectionStatusValidator validator) {
		return chooseContainer(new BRootContentProvider(), filter, validator);
	}

	public static Element chooseContainer(BTreeContentProvider provider, ViewerFilter filter, ISelectionStatusValidator validator) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new BLabelProvider(), provider);
		dialog.setValidator(validator);
		// dialog.setComparator(new JavaElementComparator());
		// dialog.setTitle(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_title);
		// dialog.setMessage(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_description);
		dialog.addFilter(filter);
		dialog.setInput(ElementManager.getInstance().getRoot());
		// dialog.setInitialSelection(initElement);
		// dialog.setHelpAvailable(false);

		if (dialog.open() == Window.OK) {
			return (Element) dialog.getFirstResult();
		}
		return null;
	}
}
