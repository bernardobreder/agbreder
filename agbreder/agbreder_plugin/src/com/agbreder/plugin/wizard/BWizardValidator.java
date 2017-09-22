package com.agbreder.plugin.wizard;

public abstract class BWizardValidator {

	private final BWizardPage page;

	public BWizardValidator(BWizardPage page) {
		super();
		this.page = page;
	}

	public abstract String validate();

	public final void update() {
		String msg = this.validate();
		if (msg != null) {
			page.setErrorMessage(msg);
		} else {
			page.setErrorMessage(null);
		}
		page.setPageComplete(msg == null);
	}

	public BWizardPage getPage() {
		return page;
	}

}
