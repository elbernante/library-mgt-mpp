package application;

import application.dao.base.DaoSession;
import application.dao.base.DaoSession.DbType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	public static final String appName = "Library Management System";
	@Override
	public void start(Stage primaryStage) {
		DaoSession.start(DbType.SQLITE, "library_manager.db");

		try {
			Parent root = FXMLLoader.load(getClass().getResource("scene/Authentication.fxml"));
			Scene scene = new Scene(root, 300, 300);
			scene.getStylesheets().add(getClass().getResource("css/Main.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Library Management System");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

		DaoSession.getDb().getPerson(); // Example of querying db
	}

	@Override
	public void stop() throws Exception {
		DaoSession.stop();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
