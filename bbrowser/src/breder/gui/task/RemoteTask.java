package breder.gui.task;

public abstract class RemoteTask extends Task {

	public abstract void perform() throws Throwable;

	public abstract void updateUI();

	public abstract void handler(Throwable t);

	public abstract void step(String title, double percent);

	public void stop() {
	}

	public void start() {
		this.start(null);
	}

	public void start(Object index) {
	}

}
