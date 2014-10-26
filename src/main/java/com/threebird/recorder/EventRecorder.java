package com.threebird.recorder;

import java.io.IOException;

import com.threebird.recorder.controllers.RecordingController;
import com.threebird.recorder.models.Schema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is considered the main entry point by extending {@link Application}. The
 * one method we must override is start(Stage stage), which we invoke by calling
 * the launch(...) method.
 */
public class EventRecorder extends Application
{
  /**
   * We define a global, static variable for the Stage so that it's easily
   * accessible across the whole app
   */
  public static Stage STAGE;
  public static Scene schemaScene;

  public static void main( String[] args )
  {
    launch( args );
  }

  @Override public void start( Stage primaryStage ) throws Exception
  {
    STAGE = primaryStage;

    // load up the FXML file we generated with Scene Builder, "schemas.fxml".
    // This view is controlled by SchemasController.java
    Parent root =
        FXMLLoader.load( EventRecorder.class.getResource( "./views/schemas.fxml" ) );

    // Initialize the Schema view and keep it around in memory so we can go back
    // to it later
    schemaScene = new Scene( root );

    toSchemaView();
  }

  /**
   * Sets the stage to the Schema view
   * 
   * @param schema
   *          - the currently selected schema, or null if no schema is selected
   */
  public static void toSchemaView()
  {
    STAGE.setTitle( "Scheme Select" );
    STAGE.setScene( schemaScene );
    STAGE.show();
  }

  /**
   * Sets the stage to the CreateScema view.
   */
  public static void toCreateSchemaView()
  {
    FXMLLoader fxmlLoader =
        new FXMLLoader( EventRecorder.class.getResource( "./views/create_schema.fxml" ) );

    Parent root;
    try {
      root = (Parent) fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException( e );
    }

    Scene scene = new Scene( root );
    STAGE.setTitle( "Create Schema" );
    STAGE.setScene( scene );
  }

  /**
   * Sets the stage to the Recording view
   * 
   * @param schema
   *          - the currently selected Schema. This parameter must not be null
   */
  public static void toRecordingView( Schema schema )
  {
    FXMLLoader fxmlLoader =
        new FXMLLoader( EventRecorder.class.getResource( "./views/recording.fxml" ) );

    Parent root;
    try {
      root = (Parent) fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException( e );
    }
    Scene scene = new Scene( root );

    fxmlLoader.< RecordingController > getController().init( schema );

    STAGE.setTitle( "Recording" );
    STAGE.setScene( scene );
    STAGE.show();
  }
}