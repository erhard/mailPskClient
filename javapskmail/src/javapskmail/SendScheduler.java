/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.Timer;

/**
 *
 * @author sebastian_pohl
 */
public class SendScheduler {

    private boolean debugFlag = false;

    private ConcurrentLinkedQueue<SendCommand> scheduledQueue;
    private arq q;
    private javax.swing.Timer resetTimer = null;

    public SendScheduler(arq q) {
        scheduledQueue = new ConcurrentLinkedQueue<SendCommand>();

        this.q = q;
    }

    private void printDebugInfo(String str) {
        if (debugFlag) {
            System.out.println(str);
        }
    }

    public boolean offer(SendCommand command) {
        if (scheduledQueue.size() < 1) {
            return scheduledQueue.offer(command);
        } else {
//            System.out.println("Command cannot be queued.");
            return false;
        }
    }

    private void startResetTimer() {
        Main.logFile.writeToLog("Timer runs.");
        resetTimer = new Timer(40000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                q.send_mode_command(Main.defaultmode);
            }
        });
        resetTimer.setRepeats(false);                   // only send one reset mode
        resetTimer.start();                             // start the timer (30 seconds)
    }

    private void stopResetTimer() {
        resetTimer.stop();
        resetTimer = null;
        Main.logFile.writeToLog("Timer stopped.");
    }

    public synchronized void acknowledgeLastCommand() {
        if (scheduledQueue.peek() != null)
            scheduledQueue.peek().commandAcknowledged();                   // external removal of command in queue
        printDebugInfo("peek command termination send.");
        // if command has been executed
    }

    public void execute() {
        if (resetTimer != null) {
            if (q.getMode() == Main.defaultmode && !resetTimer.isRunning()) {
                resetTimer = null;
                printDebugInfo("killing resetTimer.");
            }
        }

//        for (SendCommand temp : scheduledQueue) {
//            System.out.println(temp);
//        }

        printDebugInfo("size of queue: " + scheduledQueue.size());

        if (scheduledQueue.size() > 0) {
            if (resetTimer != null) {
                stopResetTimer();                                // stop reset timer
            }
            SendCommand temp = scheduledQueue.peek();   // peek at the command without removing

            if (temp.hasNextCommand()) {                // if command is not yet finished
                if (!Main.Connected
                        & !Main.Connecting
                        & !Main.Bulletinmode
                        & !Main.IACmode
                        & !Main.TXActive) {
                    temp.executeCommand();                  // execute next command if possible (which should)
                    printDebugInfo("executing command.");
                }
            } else {
                printDebugInfo("command is scheduled for kill, killing ...");
                temp = scheduledQueue.poll();           // remove Command if finished
            }
        } else {
            // System.out.println(q.getMode() + "," + resetTimer);
            if (q.getMode() != Main.defaultmode && resetTimer == null) {
                startResetTimer();
            }
        }
    }
}
