package controller;

import java.util.function.Consumer;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Helperklasse zur Bereitstellung von Hilfsfunktionen für die Controller.
 *
 */
public class ControllerHelper {

    /**
     * Starten den angegebenen Task erst nach der angegebene Zeit.
     * 
     * @param timeToWait
     *            Wartezeit bis zur Ausführung des Task
     * @param task
     *            Auszuführende Aufgabe
     */
    public static void waitBeforeTask(final long timeToWait, final Consumer<WorkerStateEvent> task) {
	Task<Void> sleeper = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		try {
		    Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
		    System.err.println("Task: " + e.getLocalizedMessage());
		}
		return null;
	    }
	};
	sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		task.accept(event);
	    }
	});
	new Thread(sleeper).start();
    }
}
