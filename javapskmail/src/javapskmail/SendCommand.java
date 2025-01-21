/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sebastian_pohl
 */

public class SendCommand {

    public enum CommandMode {
        EMERGENCY,
        LINK,
        BEACON,
        APRS
    }

    private boolean debugOutput = false;

    private arq q;
    private CommandMode type;
    private modemmodeenum[] mode;
    private String optionalMessage;
    private boolean once = false;

    private Integer internalCounter = 0;

    private javax.swing.Timer killTimer;

    public SendCommand(arq q,
            CommandMode type,
            modemmodeenum[] mode,
            String optionalMessage) {
        this.q = q;
        this.type = type;
        this.mode = mode.clone();
        this.optionalMessage = optionalMessage;        

        internalCounter = 0;
    }

    public SendCommand(arq q,
            CommandMode type,
            modemmodeenum[] mode,
            String optionalMessage,
            boolean once) {
        this(q, type, mode, optionalMessage);

        this.once = once;
    }

//    @Override
//    protected void finalize() throws Throwable
//    {
//        System.out.println("command collected.");   //do finalization here
//        super.finalize(); //not necessary if extending Object.
//    }

    private void debugOutput(String str) {
        if (debugOutput) {
            System.out.println(str);
        }
    }

    private void startKillTimer() {
        killTimer = new javax.swing.Timer(50000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commandAcknowledged();
                Main.logFile.writeToLog("Command killed.");
            }
        });
        killTimer.setRepeats(false);
        killTimer.start();
        Main.logFile.writeToLog("kill timer for command started.");
    }

    private void stopKillTimer() {
        if (killTimer != null) {
            killTimer.stop();
            killTimer = null;
        }

    }

    public synchronized void executeCommand() {
        debugOutput("next Command scheduled for execution. " + internalCounter);
        if (Main.enableSend && internalCounter <= 2) {

            debugOutput("Command execution. " + internalCounter);

            stopKillTimer();
            startKillTimer();

            Main.enableSend = false;

            Main.logFile.writeToLog(type + "," + mode[internalCounter]);

            try {
                Thread.sleep(500);
                q.send_mode_command(mode[internalCounter]);
                Thread.sleep(500);
                
                switch (type) {

                    case EMERGENCY:
                        q.Message("Send emergency", 5);
                        q.set_txstatus(txstatus.TXEmergency);
                        q.send_emergency(optionalMessage);
                        break;

                    case LINK:
                        q.Message("Link to server", 5);
                        q.set_txstatus(txstatus.TXlinkreq);
                        q.send_link();
                        break;

                    case BEACON:
                        q.Message("Send beacon", 5);
                        q.set_txstatus(txstatus.TXBeacon);
                        q.send_beacon();
                        break;

                    case APRS:
                        q.set_txstatus((txstatus.TXaprsmessage));
                        q.send_aprsmessage(optionalMessage);
                        break;

                    default:    // no known type
                        internalCounter = 3; // should not happen there
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (once) {
                internalCounter = 3;    // kill command after one execution if once
            } else {
                internalCounter++;      // increment counter since Command was executed and not once
            }

            debugOutput("Command executed. " + internalCounter);

        } else {
            Main.logFile.writeToLog("Send not enabled. " + internalCounter);
        }

        debugOutput("Leaving execution scheduling.");

    }

    // checks if there is next command
    public synchronized boolean hasNextCommand() {
        debugOutput("Command still alive.");
        return internalCounter <= 2;
    }

    // gives the kill command to this sequence, since there was an acknowledge
    public synchronized void commandAcknowledged() {
        debugOutput("Command will be killed.");
        internalCounter = 3;    // kill command sequence by forwarding the end        
    }

}
