import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class ImageFrame {

	private Window w;
	private BufferedImage currentImage;

	private int width = 1000;
	private int height = 1000;
	private float opacity = 0.5f;
	
	// Create an initial (hidden) Window
	@SuppressWarnings("serial")
	public ImageFrame() {
		w = new Window(null);

		w.add(new JComponent() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;

				g2.drawImage(currentImage, 0, 0, this);
			}

			public Dimension getPreferredSize() {
				return new Dimension(width, height);
			}
		});
		w.pack();
		w.setLocationRelativeTo(null);
		w.setAlwaysOnTop(true);
		w.setVisible(false);
		w.setBackground(new Color(0, 0, 0, 0));
	}

	// Constructor tiiiime
	public ImageFrame(int width, int height, float opacity) {
		this();
		this.width = width;
		this.height = height;
		this.opacity = opacity;
	}

	// Open the Window and render the current img
	public void openWindow() {
		w.setVisible(true);
		setTransparent(w);
	}
	
	// Hide the window
	public void hideWindow() {
		w.setVisible(false);
	}
	
	// Close the window
	public void closeWindow() {
		w.dispose();
	}
	
	// Change the window's location
	public void setWindowLocation(int x, int y) {
		if (x == -1) 
			x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - width / 2;
		if (y == -1)
			y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - height / 2;
		
		System.out.println("Setting location to "+x+ " "+ y);
		w.setLocation(x,y);
	}

	// Sets the current window see-through (windows only)
	private static void setTransparent(Component w) {
		WinDef.HWND hwnd = getHWnd(w);
		int wl = User32.INSTANCE.GetWindowLong(hwnd, WinUser.GWL_EXSTYLE);
		wl = wl | WinUser.WS_EX_LAYERED | WinUser.WS_EX_TRANSPARENT;
		User32.INSTANCE.SetWindowLong(hwnd, WinUser.GWL_EXSTYLE, wl);
	}

	// Get the Window's Handle
	private static HWND getHWnd(Component w) {
		HWND hwnd = new HWND();
		hwnd.setPointer(Native.getComponentPointer(w));
		return hwnd;
	}

	// Sets the current bg img. Returns false if there was an error downloading
	public boolean setCurrentImageFromURL(String url) {
		try {
			// Load img from URL
			BufferedImage prefImg = ImageIO.read(new URL(url));
			if (prefImg == null) {
				return false;
			}

			// Convert to ARGB
			BufferedImage newImage = new BufferedImage(prefImg.getWidth(), prefImg.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(prefImg, 0, 0, null);
			g.dispose();

			// Resize and overwrite
			prefImg = resizeImage(prefImg, width, height);
			prefImg = changeImageOpacity(prefImg, opacity);
			currentImage = prefImg;

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Resize an img to the given size
	private BufferedImage resizeImage(BufferedImage img, int width, int height) {
		if (width < 1) {
			width = 1;
		}
		if (height <= 0) {
			double aspectRatio = (double) width / img.getWidth() * 0.5;
			height = (int) Math.ceil(img.getHeight() * aspectRatio);
		}
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Image scaled = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		resized.getGraphics().drawImage(scaled, 0, 0, null);
		return resized;
	}

	// Make an img opaque
	private BufferedImage changeImageOpacity(BufferedImage original, float opacity) {
		BufferedImage resizedImage = new BufferedImage(original.getWidth(), original.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
		g.dispose();

		return resizedImage;
	}
}