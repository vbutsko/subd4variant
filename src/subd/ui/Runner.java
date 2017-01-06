package subd.ui;

import javax.swing.*;


public class Runner {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ControlFrame frame = new ControlFrame();
                frame.setTitle("Music");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLocation(200,200);
                frame.setSize(400, 400);
                frame.setVisible(true);
            }
        });
    }
}
