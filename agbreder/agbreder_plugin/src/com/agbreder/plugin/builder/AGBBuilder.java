package com.agbreder.plugin.builder;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.Eclipse;

public class AGBBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "agbreder_plugin.sampleBuilder";

	public static final String MARKER_TYPE = "agbreder_plugin.xmlProblem";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		IFile binaryFile = Eclipse.getBinaryFile(this.getProject());
		if (binaryFile.exists()) {
			binaryFile.delete(true, monitor);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		IProject project = this.getProject();
		try {
			if (!this.hasBeenBuilt(project)) {
				return buildLaunchConfiguration(project, monitor);
			} else {
				IResourceDelta deltas = getDelta(getProject());
				for (IResourceDelta delta : deltas.getAffectedChildren()) {
					if (delta.getResource().getName().equals(BrederProjectConstant.SOURCE_FOLDER)) {
						return buildLaunchConfiguration(project, monitor);
					}
				}
			}
		} catch (IOException e) {
		}
		return null;
	}

	private static IProject[] buildLaunchConfiguration(IProject project, IProgressMonitor monitor) throws CoreException, IOException {
		Eclipse.deleteMarkers(project);
		Eclipse.getBinaryFile(project).delete(true, monitor);
		// Eclipse.getBase64File(project).delete(true, monitor);
		Eclipse.build(project, monitor);
		Eclipse.refresh(project, monitor);
		return null;
	}
}
