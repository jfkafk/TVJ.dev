package ee.taltech.superitibros;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ee.taltech.superitibros.Screens.MainMenu;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		// Gets display size from users system.
		Graphics.DisplayMode display = Lwjgl3ApplicationConfiguration.getDisplayMode();
		// Sets windowed max size menu screen
		config.setWindowedMode(display.width, display.height);
		config.setResizable(true);
		config.setForegroundFPS(60);
		config.setTitle("Super ITI Bros.");
		MainMenu mainM = new MainMenu();
		new Lwjgl3Application(mainM, config);
	}
}
