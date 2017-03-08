package application.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowUtil {
	public static void loadWindow(String loc, String title, boolean resizable) {
		try {
			Parent parent = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("application/" + loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.setResizable(resizable);
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(WindowUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void loadWindow(String loc, String title, boolean resizable, FxmlLoadCallback callback) {
		try {
			FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("application/" + loc));
			
			Parent parent = loader.load();
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.setResizable(resizable);
			callback.callback(loader);
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(WindowUtil.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
	
	public static void messageBox(String message) {
		new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
	}

}
