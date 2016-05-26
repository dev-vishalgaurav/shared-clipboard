import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public final class SharedClipboardManager extends Thread implements ClipboardOwner {
	public static final String SERVER_URL = "https://teak-passage-132116.appspot.com/";
	public static final String API_CLIPPINGS = "add.do";
	public static final String URL_ADD = SERVER_URL + API_CLIPPINGS;

	Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();

	private String userId = null;

	public SharedClipboardManager(String userid) {
		this.userId = userid;
		System.out.println("User id = ..." + userId);
	}

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
    void regainOwnership(Transferable t) {
        sysClip.setContents(t, this);
        System.out.println("regainOwnership...");
    }
	void accessAndProcessClip() {
		Transferable contents = sysClip.getContents(this); // EXCEPTION
		processContents(contents);
		regainOwnership(contents);
	}

	void processContents(Transferable t) {
		System.out.println("Processing: " + t);
		String clipboard = getClipboardContents(t);
		System.out.println("Processing: " + clipboard);
		sendClippingToServer(clipboard);
	}

	void sendClippingToServer(String clipping) {
		try {
			URL url = new URL(URL_ADD + "?" + "passcode=" + userId + "&input_type=desktop&clipping="
					+ URLEncoder.encode(clipping, "UTF-8"));
			URL obj = new URL(url.toString());
			System.out.println("obj = " + obj.toString());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
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
		new SharedClipboardManager(args[0]).start();
	}
}
