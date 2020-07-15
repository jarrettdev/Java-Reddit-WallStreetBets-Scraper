package Yeet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ignition {

    public Ignition() {

    }

    Expery e1;
    Profit p1;
    private boolean eAlive;

    public boolean iseAlive() {
        return eAlive;
    }

    public void seteAlive(boolean eAlive) {
        this.eAlive = eAlive;
    }

    public boolean ispAlive() {
        return pAlive;
    }

    public void setpAlive(boolean pAlive) {
        this.pAlive = pAlive;
    }

    private boolean pAlive;

    void begin() {
        new GraphicsShit().doStuff();
        //e1 = new Expery();
        //e1.start();
    }

    void checkProfit() {
        p1 = new Profit();
        p1.start();
    }

    void experyTimer() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        Expery.mealRunning = true;
        System.out.println("experyTimer started");

        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!Profit.profitRunning == true) {
                    begin();
                    System.out.println("starting reddit scan...");

                } else {
                    run();
                }
            }
        }, 0, 4, TimeUnit.HOURS);


    }

    void profitTimer() {
        ScheduledExecutorService ses1 = Executors.newSingleThreadScheduledExecutor();
        System.out.println("profitTimer started");
        ses1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!Expery.mealRunning == true) {
                    System.out.println("checking profits");
                    checkProfit();

                } else {
                    run();
                    System.out.println("not ready");
                }
            }
        }, 15, 15, TimeUnit.MINUTES);
    }


    void liveCheck() {
        if (e1.isAlive()) {
            seteAlive(true);
        } else {
            seteAlive(false);
        }
        if (p1.isAlive()) {
            setpAlive(true);
        } else {
            setpAlive(false);
        }
    }


}

class GraphicsShit extends JFrame {
    JPanel jp1;
    WebBot b1;

    void doStuff() {
        JButton jb1 = new JButton("Free $");
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Expery().start();
                System.out.println("Button clicked");
                b1 = new WebBot();
            }
        });

        JTextField jtf1 = new JTextField("Enter yeetables");
        jp1 = new JPanel();
        this.add(jp1);
        jp1.add(jb1);
        jp1.add(jtf1);
        this.setSize(300, 300);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
