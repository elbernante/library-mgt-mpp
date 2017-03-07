package application;
	
import dao.base.DaoSession;
import dao.base.DaoSession.DbType;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		DaoSession.start(DbType.SQLITE, "library_manager.db");
		
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			DaoSession.getDb().getPerson(); // Example of querying db
				
		} catch(Exception e) {
			e.printStackTrace();
		}
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
