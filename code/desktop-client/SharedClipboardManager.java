import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class SharedClipboardManager extends Thread implements ClipboardOwner {

    Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

    public void run() {
        Transferable trans = sysClip.getContents(this);
        regainOwnership(trans);
        System.out.println("Listening to board...");
        while (true) {
        }
    }

    private void waitForSomeTime(long millis) {
        try {
            System.out.println("waitForSomeTime " + millis);
            Thread.sleep(50);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public void lostOwnership(Clipboard c, Transferable t) {
        waitForSomeTime(50);
        try {
            accessAndProcessClip();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            waitForSomeTime(50);
            accessAndProcessClip();
        }
    }

    void accessAndProcessClip() {
        Transferable contents = sysClip.getContents(this); // EXCEPTION
        processContents(contents);
        regainOwnership(contents);
    }

    void processContents(Transferable t) {
        System.out.println("Processing: " + t);
        System.out.println("Processing: " + getClipboardContents(t));
    }

    void regainOwnership(Transferable t) {
        sysClip.setContents(t, this);
    }

    /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an empty
     * String.
     */
    public String getClipboardContents(Transferable contents) {
        String result = "";
        // odd: the Object param of getContents is not currently used
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new SharedClipboardManager().start();
    }
} 
