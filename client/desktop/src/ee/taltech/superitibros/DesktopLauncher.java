package ee.taltech.superitibros;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ee.taltech.superitibros.GameInfo.GameClient;
import ee.taltech.superitibros.Screens.MainMenu;
import ee.taltech.superitibros.Screens.MenuScreen;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;

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
		GameClient gameClient = new GameClient();
		new Lwjgl3Application(gameClient, config);
	}
}
