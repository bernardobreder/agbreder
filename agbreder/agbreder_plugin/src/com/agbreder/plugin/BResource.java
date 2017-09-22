package com.agbreder.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class BResource {

	private final Image logoWizard;

	private final Image sampleImage;

	private final Image projectImage;

	private final Image resourceImage;

	private final Image sourceImage;

	private final Image packageImage;

	private final Image classImage;

	private final Image folderImage;

	private final Image perspectiveImage;

	private final ImageDescriptor perspectiveDescriptor;

	private static final BResource instance = new BResource();

	private BResource() {
		logoWizard = BActivator.getImageDescriptor("sample.gif").createImage();
		sampleImage = BActivator.getImageDescriptor("sample.gif").createImage();
		folderImage = BActivator.getImageDescriptor("folder.gif").createImage();
		projectImage = BActivator.getImageDescriptor("project.gif").createImage();
		resourceImage = BActivator.getImageDescriptor("resource.gif").createImage();
		packageImage = BActivator.getImageDescriptor("package.gif").createImage();
		classImage = BActivator.getImageDescriptor("class.gif").createImage();
		sourceImage = BActivator.getImageDescriptor("source.gif").createImage();
		perspectiveDescriptor = BActivator.getImageDescriptor("perspective.gif");
		perspectiveImage = this.perspectiveDescriptor.createImage();
	}

	public Image getLogoWizard() {
		return logoWizard;
	}

	public Image getSampleImage() {
		return sampleImage;
	}

	public Image getProjectImage() {
		return projectImage;
	}

	public Image getResourceImage() {
		return resourceImage;
	}

	public Image getFolderImage() {
		return folderImage;
	}

	public Image getSourceImage() {
		return sourceImage;
	}

	public Image getPackageImage() {
		return packageImage;
	}

	public static BResource getInstance() {
		return instance;
	}

	public Image getClassImage() {
		return classImage;
	}

	public Image getPerspectiveImage() {
		return perspectiveImage;
	}

	public ImageDescriptor getPerspectiveDescriptor() {
		return perspectiveDescriptor;
	}

}
