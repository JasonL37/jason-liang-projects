package ui;

import model.Event;
import model.EventLog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * Represents the actions of the window
 */
public class BudgetAppWindow extends WindowAdapter {
    BudgetAppWindow() {
        super();
    }

    // EFFECTS: Prints the event log when the user closes the window.
    @Override
    public void windowClosing(WindowEvent e) {
        printLog(EventLog.getInstance());
    }

    // Inspired from the printLog method in AlarmSystem
    public void printLog(EventLog eventLog) {
        for (Event next : eventLog) {
            System.out.println(next.toString() + "\n");
        }
    }
}
