package com.agbreder.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.debug.ui.console.ConsoleColorProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPage;

import com.agbreder.plugin.builder.AGBBuilder;
import com.agbreder.plugin.console.BConsole;
import com.agbreder.plugin.monitor.BProgressMonitor;
import com.agbreder.plugin.util.AGBrederLanguageFile;
import com.agbreder.plugin.util.SoUtil;

/**
 * Facilitadores de comportamentos no eclipse
 * 
 * @author bernardobreder
 * 
 */
@SuppressWarnings("restriction")
public class Eclipse {

	/**
	 * Retorna uma propriedade persistente no projeto
	 * 
	 * @param project
	 * @param key
	 * @return valor da propriedade
	 * @throws CoreException
	 */
	public static String getProjectProperty(IProject project, String key) throws CoreException {
		return project.getPersistentProperty(new QualifiedName("", key));
	}

	/**
	 * Salve todos os arquivos
	 */
	public static void saveAll() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				workbench.saveAllEditors(false);
			}
		});
	}

	/**
	 * Retorna o binario do projeto
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFile getBinaryFile(IProject project) throws CoreException {
		return getBinaryFile(project, null);
	}

	/**
	 * Retorna o binario do projeto
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFile getBase64File(IProject project) throws CoreException {
		return getBase64File(project, null);
	}

	/**
	 * Retorna o binario do projeto
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFile getBinaryFile(IProject project, IProgressMonitor monitor) throws CoreException {
		return getBinaryFolder(project, monitor).getFile("binary.agbc");
	}

	/**
	 * Retorna o binario do projeto
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IFile getBase64File(IProject project, IProgressMonitor monitor) throws CoreException {
		return getBinaryFolder(project, monitor).getFile("binary.txt");
	}

	/**
	 * Busca pelo arquivo bin‚Ä°rio
	 * 
	 * @param project
	 * @return
	 * @throws FileNotFoundException
	 * @throws CoreException
	 */
	public static IFile findBinaryFile(IProject project) throws CoreException {
		IFile file = getBinaryFile(project);
		if (!file.exists()) {
			throw Eclipse.throwCoreException("Binary of the BProject '%s' not found", project.getName());
		}
		return file;
	}

	/**
	 * Constroi um Erro
	 * 
	 * @param format
	 * @param objects
	 * @return
	 * @throws CoreException
	 */
	public static CoreException throwCoreException(String format, Object... objects) throws CoreException {
		String message = String.format(format, objects);
		String pluginId = BActivator.PLUGIN_ID;
		throw new CoreException(log(Status.ERROR, message));
	}

	/**
	 * Constroi um Erro
	 * 
	 * @param format
	 * @param objects
	 * @return
	 * @throws CoreException
	 */
	public static CoreException throwCoreException(Throwable e) throws CoreException {
		return Eclipse.throwCoreException(e.getMessage());
	}

	/**
	 * Constroi um Erro
	 * 
	 * @param format
	 * @param objects
	 * @return
	 * @return
	 * @throws CoreException
	 */
	public static Status log(int severity, String message) throws CoreException {
		Status status = new Status(severity, BActivator.PLUGIN_ID, message);
		ConsolePlugin.log(status);
		return status;
	}

	/**
	 * Busca pelo arquivo bin‚Ä°rio
	 * 
	 * @param project
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File toFile(IResource res) {
		return res.getLocation().toFile();
	}

	/**
	 * Retorna a pasta do binario do projeto
	 * 
	 * @param project
	 * @return binary folder
	 * @throws CoreException
	 */
	public static IFolder getBinaryFolder(IProject project) throws CoreException {
		return getBinaryFolder(project, null);
	}

	/**
	 * Retorna a pasta do binario do projeto
	 * 
	 * @param project
	 * @return binary folder
	 * @throws CoreException
	 */
	public static IFolder getBinaryFolder(IProject project, IProgressMonitor monitor) throws CoreException {
		IFolder folder = project.getFolder(BrederProjectConstant.BINARY_FOLDER);
		if (!folder.exists()) {
			folder.create(true, true, monitor);
		}
		return folder;
	}

	/**
	 * Retorna a pasta da fonte do projeto
	 * 
	 * @param project
	 * @return source folder
	 * @throws CoreException
	 */
	public static IFolder getSourceFolder(IProject project) throws CoreException {
		return getSourceFolder(project, null);
	}

	/**
	 * Retorna a pasta da fonte do projeto
	 * 
	 * @param project
	 * @return source folder
	 * @throws CoreException
	 */
	public static IFolder getSourceFolder(IProject project, IProgressMonitor monitor) throws CoreException {
		IFolder folder = project.getFolder(BrederProjectConstant.SOURCE_FOLDER);
		if (!folder.exists()) {
			folder.create(true, true, monitor);
		}
		return folder;
	}

	/**
	 * Retorna todos os Launchs
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static List<ILaunchConfiguration> getAllLaunchs() throws CoreException {
		return Arrays.asList(DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations());
	}

	/**
	 * Retorna todos os Launchs
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static List<ILaunchConfiguration> getAGBLaunchs() throws CoreException {
		List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>();
		for (ILaunchConfiguration conf : getAllLaunchs()) {
			if (conf.getType().equals(getLaunchConfigurationType())) {
				list.add(conf);
			}
		}
		return list;
	}

	/**
	 * Retorna todos os Launchs
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static List<ILaunchConfiguration> getAGBLaunchs(IProject project) throws CoreException {
		List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>();
		for (ILaunchConfiguration conf : getAllLaunchs()) {
			if (conf.getType().equals(getLaunchConfigurationType())) {
				String projectName = getLaunchProperty(conf, BrederProjectConstant.PROJECT_NAME);
				if (projectName.equals(project.getName())) {
					list.add(conf);
				}
			}
		}
		return list;
	}

	/**
	 * Recupera uma propriedade do launch
	 * 
	 * @param launch
	 * @return valor da propriedade
	 * @throws CoreException
	 */
	public static String getLaunchProperty(ILaunchConfiguration launch, String key) throws CoreException {
		return launch.getAttribute(key, "");
	}

	/**
	 * Constroi um Launch
	 * 
	 * @param project
	 * @return launch construido
	 * @throws CoreException
	 */
	public static ILaunchConfiguration addLaunchConfig(IProject project) throws CoreException {
		ILaunchConfigurationType type = getLaunchConfigurationType();
		ILaunchConfigurationWorkingCopy lcwc = type.newInstance(null, project.getName());
		lcwc.setAttribute(BrederProjectConstant.PROJECT_NAME, project.getName());
		lcwc.setAttribute(BrederProjectConstant.MAIN_CLASS_NAME, "Main");
		ILaunchConfiguration lc = lcwc.doSave();
		project.build(IncrementalProjectBuilder.FULL_BUILD, BProgressMonitor.DEFAULT);
		return lc;
	}

	/**
	 * Retorna o launch type
	 * 
	 * @return launch type
	 */
	public static ILaunchConfigurationType getLaunchConfigurationType() {
		return DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(BrederProjectConstant.LAUNCH_CONFIGURATION_ID);
	}

	/**
	 * Constroi um console
	 * 
	 * @param title
	 * @param replace
	 * @return console
	 */
	public static BConsole buildConsole(String title, boolean replace) {
		IOConsole lastConsole = null;
		if (replace) {
			IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
			for (IConsole console : consoles) {
				if (console.getImageDescriptor() == BResource.getInstance().getPerspectiveDescriptor()) {
					if (console.getName().equals(title)) {
						if (console instanceof IOConsole) {
							lastConsole = (IOConsole) console;
							((IOConsole) console).clearConsole();
							break;
						}
					}
				}
			}
		}
		if (lastConsole == null) {
			lastConsole = new IOConsole(title, BResource.getInstance().getPerspectiveDescriptor());
		}
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { lastConsole });
		return new BConsole(lastConsole);
	}

	/**
	 * Deleta as marcaÔøΩ‚Ä∫es
	 * 
	 * @param file
	 * @throws CoreException
	 */
	public static void deleteMarkers(IResource file) throws CoreException {
		file.deleteMarkers(AGBBuilder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	/**
	 * Deleta as marcaÔøΩ‚Ä∫es
	 * 
	 * @param file
	 * @param monitor
	 * @throws CoreException
	 */
	public static void refresh(IResource file, IProgressMonitor monitor) throws CoreException {
		file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	/**
	 * Constroi um processo no console atrav≈Ωs de um processo SO
	 * 
	 * @param launch
	 * @param process
	 * @return processo console
	 */
	public static ProcessConsole buildConsoleProcess(ILaunch launch, Process process, String title) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		ConsoleColorProvider color = new ConsoleColorProvider();
		RuntimeProcess runtime = new RuntimeProcess(launch, process, title, map);
		return new ProcessConsole(runtime, color, "utf-8");
	}

	/**
	 * Constroi uma marcação
	 * 
	 * @param project
	 * @param filename
	 * @param msg
	 * @param severity
	 * @param line
	 * @param column
	 * @param word
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void addMarket(IFile file, String msg, Integer severity, int line, int column, String word) throws CoreException {
		int start = getStartIndex(file, line, column);
		addMarket(file, msg, severity, line, start, start + word.length());
	}

	/**
	 * Constroi uma marcação
	 * 
	 * @param project
	 * @param filename
	 * @param msg
	 * @param severity
	 * @param line
	 * @param column
	 * @param word
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void addMarket(IProject project, String msg, Integer severity, int line, int column, String word) throws CoreException {
		addMarket(project, msg, severity, line, -1, -1);
	}

	/**
	 * Constroi uma marcação
	 * 
	 * @param project
	 * @param file
	 * @param msg
	 * @param severity
	 * @param line
	 * @param begin
	 * @param end
	 * @throws CoreException
	 */
	public static void addMarket(IResource file, String msg, int severity, int line, int begin, int end) throws CoreException {
		IMarker marker = file.createMarker(AGBBuilder.MARKER_TYPE);
		marker.setAttribute(IMarker.MESSAGE, msg);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.LINE_NUMBER, line);
		marker.setAttribute(IMarker.LOCATION, line);
		marker.setAttribute(IMarker.CHAR_START, begin);
		marker.setAttribute(IMarker.CHAR_END, end);
	}

	/**
	 * M≈Ωtodo que retorna o indice baseado na linha e coluna de um arquivo.
	 * Esse indice representa o offset do arquivo at≈Ω a linha e coluna
	 * especificado.
	 * 
	 * @param line
	 * @param column
	 * @param file
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	private static int getStartIndex(IFile file, int line, int column) throws CoreException {
		String content = Eclipse.readStream(file.getContents());
		int start = 0;
		int clin = 1;
		for (int n = 0; n < content.length(); n++) {
			char c = content.charAt(n);
			start++;
			if (c == '\n') {
				clin++;
				if (clin == line) {
					break;
				}
			}
		}
		start += column - 1;
		return start;
	}

	public static void showMessageInWizard(String format, Object... objects) {
		Eclipse.showMessage("AGBreder Launch", format, objects);
	}

	public static void showMessageInLaunch(String format, Object... objects) {
		Eclipse.showMessage("AGBreder Launch", format, objects);
	}

	public static void showMessage(final String title, final String format, final Object... objects) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
				String message = String.format(format, objects);
				MessageDialog.openInformation(shell, title, message);
			}
		});
	}

	/**
	 * Le toda a stream
	 * 
	 * @param input
	 * @return texto
	 * @throws IOException
	 */
	public static String readStream(InputStream input) throws CoreException {
		StringBuilder sb = new StringBuilder();
		try {
			for (int n; ((n = input.read()) != -1);) {
				sb.append((char) n);
			}
		} catch (IOException e) {
			throw Eclipse.throwCoreException(e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
			}
		}
		return sb.toString();
	}

	/**
	 * Constroi o processo de compilação
	 * 
	 * @param project
	 * @param monitor
	 * @return processo
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CoreException
	 */
	public static Process buildAgbcProcess(IProject project, IProgressMonitor monitor) throws CoreException {
		IFolder sourceFolder = Eclipse.getSourceFolder(project);
		IFile binaryFile = Eclipse.getBinaryFile(project);
		IFile base64File = Eclipse.getBase64File(project);
		File brederCompilerFile = getBrederCompilerFile();
		if (SoUtil.isUnix()) {
			try {
				new ProcessBuilder("chmod", "+x", brederCompilerFile.toString()).start();
			} catch (IOException e) {
				throw Eclipse.throwCoreException(e);
			}
		}
		List<String> args = new ArrayList<String>();
		args.add(brederCompilerFile.toString());
		args.add("-o");
		args.add(Eclipse.toFile(binaryFile).getAbsolutePath());
		args.add("-b64");
		args.add(Eclipse.toFile(base64File).getAbsolutePath());
		args.add(Eclipse.toFile(sourceFolder).getAbsolutePath());
		{
			AGBrederLanguageFile agbSrc = new AGBrederLanguageFile("src");
			if (agbSrc.exists()) {
				args.add(agbSrc.getAbsolutePath());
			}
		}
		try {
			return Runtime.getRuntime().exec(args.toArray(new String[0]), null, Eclipse.toFile(project));
		} catch (IOException e) {
			throw Eclipse.throwCoreException(e);
		}
	}

	/**
	 * Loga um erro
	 * 
	 * @param e
	 */
	public static void log(Throwable e) {
		ConsolePlugin.log(e);
	}

	/**
	 * Constroi um processo da M‚Ä°quina Virtual
	 * 
	 * @param project
	 * @param mainClass
	 * @return
	 * @throws CoreException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static Process buildAgbProcess(IProject project, String mainClass) throws CoreException {
		IFile binaryFile = Eclipse.getBinaryFile(project);
		List<String> list = new ArrayList<String>();
		String brederFile = getBrederVirtualMachineFile().toString();
		if (SoUtil.isUnix()) {
			try {
				new ProcessBuilder("chmod", "+x", brederFile.toString()).start().waitFor();
			} catch (InterruptedException e) {
				throw Eclipse.throwCoreException(e);
			} catch (IOException e) {
				throw Eclipse.throwCoreException(e);
			}
		}
		list.add(brederFile);
		list.add(Eclipse.toFile(binaryFile).getAbsolutePath());
		list.add(mainClass);
		try {
			return new ProcessBuilder(list.toArray(new String[0])).directory(Eclipse.toFile(project)).start();
		} catch (IOException e) {
			throw Eclipse.throwCoreException(e);
		}
	}

	private static File getBrederCompilerFile() {
		File file = new AGBrederLanguageFile("bin", "agbc");
		if (!file.exists()) {
			file = new AGBrederLanguageFile("bin", "agbc" + SoUtil.getExtension());
		}
		if (!file.exists()) {
			file = new AGBrederLanguageFile("bin", "agbc.bat");
		}
		return file;
	}

	private static File getBrederVirtualMachineFile() {
		File file = new AGBrederLanguageFile("bin", "agb");
		if (!file.exists()) {
			file = new AGBrederLanguageFile("bin", "agb" + SoUtil.getExtension());
		}
		if (!file.exists()) {
			file = new AGBrederLanguageFile("bin", "agb.bat");
		}
		return file;
	}

	/**
	 * Realiza o build de uma projeto
	 * 
	 * @param project
	 * @param monitor
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void build(IProject project, IProgressMonitor monitor) throws CoreException {
		if (!getBrederCompilerFile().exists()) {
			throw throwCoreException("Not found : " + getBrederCompilerFile().getAbsolutePath());
		}
		Process process = Eclipse.buildAgbcProcess(project, monitor);
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			Eclipse.log(e);
			return;
		}
		buildStream(project, process.getInputStream());
		buildStream(project, process.getErrorStream());
	}

	private static void buildStream(IProject project, InputStream input) throws CoreException {
		String content = Eclipse.readStream(input).trim();
		if (content.length() > 0) {
			Pattern p = Pattern.compile("\\['(.*?)','(.*?)',(.*?),(.*?)\\]:");
			for (String line : content.split("\n")) {
				if (line.startsWith("[") && line.indexOf(':') > 0) {
					Matcher m = p.matcher(line);
					if (m.lookingAt()) {
						String classname = m.group(1);
						String token = m.group(2);
						int lin = new Integer(m.group(3));
						int col = new Integer(m.group(4));
						String message = line.substring(line.lastIndexOf(':') + 1).trim();
						IFile file;
						{
							File projectPath = Eclipse.toFile(project);
							if (classname.startsWith(projectPath.getAbsolutePath())) {
								file = project.getFile(classname.substring(projectPath.getAbsolutePath().length() + 1));
							} else {
								file = project.getFile(classname);
							}
						}
						if (!file.exists()) {
							Eclipse.addMarket(project, message, IMarker.SEVERITY_ERROR, lin, col, token);
						} else {
							Eclipse.addMarket(file, message, IMarker.SEVERITY_ERROR, lin, col, token);
						}
					}
				} else {
					log(Status.ERROR, line);
				}
			}
		}
	}

	public static boolean needSave() {
		IWorkbenchWindow windows[] = Workbench.getInstance().getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			IWorkbenchPage pages[] = windows[i].getPages();
			for (int j = 0; j < pages.length; j++) {
				WorkbenchPage page = (WorkbenchPage) pages[j];
				ISaveablePart[] parts = page.getDirtyParts();
				for (int k = 0; k < parts.length; k++) {
					ISaveablePart part = parts[k];
					if (part.isSaveOnCloseNeeded()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void openFile(final IFile file) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file);
				} catch (PartInitException e) {
				}
			}
		});
	}

	public static IWorkspaceRoot getRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public static IProject getProject(String name) {
		return getRoot().getProject(name);
	}

	public static List<IProject> getProjects() {
		return Arrays.asList(getRoot().getProjects());
	}

	public static String getProjectName(ILaunchConfiguration configuration) throws CoreException {
		return getLaunchProperty(configuration, BrederProjectConstant.PROJECT_NAME);
	}

}
