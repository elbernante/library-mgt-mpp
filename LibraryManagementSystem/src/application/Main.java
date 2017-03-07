package application;
	
import dao.base.DaoSession;
import dao.base.DaoSession.DbType;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		DaoSession.start(DbType.SQLITE, "library_manager.db");
		
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Authentication.fxml"));
			Scene scene = new Scene(root, 300, 300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Library Management System");
			primaryStage.show();
		} catch(Exception e) {
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
