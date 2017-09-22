package com.agbreder.ide.model.io.event;

import java.util.ArrayList;
import java.util.List;

import breder.util.task.EventTask;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.IFolder;

/**
 * Classe que implementa os eventos de sistema de arquivo
 * 
 * @author bernardobreder
 */
public class BFileEvent implements IFileEvent {

  /** Instancia unica */
  public static final BFileEvent DEFAULT = new BFileEvent();

  /** Lista de Eventos */
  private final List<FileEvent> list = new ArrayList<FileEvent>();

  /**
   * Construtor
   */
  private BFileEvent() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void addListener(FileEvent event) {
    this.list.add(event);
  }

  /**
	 * {@inheritDoc}
	 */
	@Override
	public void fireRefresh(final IFolder folder) {
		for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.refreshFolder(folder);
          }
        });
      }
    }		
	}

	/**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireCreatedFolder(final IFolder folder) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.createdFolder(folder);
          }
        });
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireCreatedFile(final IFile file) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.createdFile(file);
          }
        });
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireDeletedFolder(final IFolder folder) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.deletedFolder(folder);
          }
        });
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireDeletedFile(final IFile file) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.deletedFile(file);
          }
        });
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireChangedFolder(final IFolder folder) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.changedFolder(folder);
          }
        });
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void fireChangedFile(final IFile file) {
    for (final FileEvent event : this.list) {
      if (event != null) {
        EventTask.invokeLater(new Runnable() {
          @Override
          public void run() {
            event.changedFile(file);
          }
        });
      }
    }
  }

}
