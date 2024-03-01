package ee.taltech.superitibros;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ee.taltech.superitibros.Screens.MainMenu;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(2000, 2000);
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.useVsync(true);
		config.setTitle("Super ITI Bros.");
		MainMenu mainM = new MainMenu();
		new Lwjgl3Application(mainM);
	}
}
