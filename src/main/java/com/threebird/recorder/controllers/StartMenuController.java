package com.threebird.recorder.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.threebird.recorder.models.schemas.Schema;
import com.threebird.recorder.models.schemas.SchemasManager;
import com.threebird.recorder.models.sessions.SessionManager;
import com.threebird.recorder.utils.EventRecorderUtil;
import com.threebird.recorder.views.TimeBox;

/**
 * Controls the first view the user sees. All these member variables with the @FXML
 * annotation are physical objects I placed in Scene Builder and applied an
 * 'id'. The 'id' must match the variable name. Methods with an @FXML annotation
 * are triggered by events (again, specified in scene builder)
 */
public class StartMenuController
{
  @FXML private TableView< Schema > schemaTable;
  @FXML private TableColumn< Schema, String > clientCol;
  @FXML private TableColumn< Schema, String > projectCol;
  @FXML private Button createSchemaButton;

  @FXML private AnchorPane rightSide;
  @FXML private Text emptyMessage;

  @FXML private VBox mappingsBox;

  @FXML private Pane timeBoxSlot;
  private TimeBox timeBox;

  @FXML private TextField observerField;
  @FXML private TextField therapistField;
  @FXML private TextField conditionField;
  @FXML private TextField sessionField;

  @FXML private Button saveButton;
  @FXML private Button startButton;

  /**
   * load up the FXML file we generated with Scene Builder, "schemas.fxml". This
   * view is controlled by SchemasController.java
   */
  public static void toStartMenuView()
  {
    String filepath = "./views/startMenu.fxml";
    StartMenuController controller = EventRecorderUtil.loadScene( filepath, "Start Menu" );
    controller.init();
  }

  private void init()
  {
    timeBox = new TimeBox( 0 );
    timeBoxSlot.getChildren().add( timeBox );

    rightSide.setVisible( false );
    initSchemaListView();
    initSessionDetails();
  }

  /**
   * Initializes 'schemaList' and binds it to 'schemas'
   */
  private void initSchemaListView()
  {
    clientCol.setCellValueFactory( p -> new SimpleStringProperty( p.getValue().client ) );
    projectCol.setCellValueFactory( p -> new SimpleStringProperty( p.getValue().project ) );

    schemaTable.setItems( SchemasManager.schemas() );
    schemaTable.getSelectionModel()
               .selectedItemProperty()
               .addListener( this::onSchemaSelect );
    schemaTable.getSelectionModel().select( SchemasManager.getSelected() );
  }

  /**
   * Populates the 'mappingBox' with the currently selected Schema's
   * key-behavior mappings
   */
  private void populateMappingsTable( Schema schema )
  {
    mappingsBox.getChildren().clear();

    schema.mappings.values().forEach( mapping -> {
      Label contLbl = new Label();
      contLbl.setText( mapping.isContinuous ? "(cont.)" : "" );
      contLbl.setMinWidth( 45 );

      Label keyLbl = new Label( mapping.key.toString() );
      keyLbl.setMinWidth( 15 );

      Label separator = new Label( ":" );

      Label behaviorLbl = new Label( mapping.behavior );
      behaviorLbl.setWrapText( true );

      HBox hbox = new HBox( contLbl, keyLbl, separator, behaviorLbl );
      hbox.setSpacing( 5 );
      mappingsBox.getChildren().add( hbox );
    } );
  }

  private void initSessionDetails()
  {
    observerField.setText( SessionManager.getObserver() );
    therapistField.setText( SessionManager.getTherapist() );
    conditionField.setText( SessionManager.getCondition() );
    sessionField.setText( SessionManager.getSessionNumber().toString() );

    observerField.textProperty().addListener( ( obsrvr, oldV, newV ) -> SessionManager.setObserver( newV.trim() ) );
    therapistField.textProperty().addListener( ( obsrvr, oldV, newV ) -> SessionManager.setTherapist( newV.trim() ) );
    conditionField.textProperty().addListener( ( obsrvr, oldV, newV ) -> SessionManager.setCondition( newV.trim() ) );

    // Put in some idiot-proof logic for the session # (limit to just digits,
    // prevent exceeding max_value)
    EventHandler< ? super KeyEvent > limiter =
        EventRecorderUtil.createFieldLimiter( sessionField, "0123456789".toCharArray(), 9 );
    sessionField.setOnKeyTyped( limiter );
    sessionField.textProperty().addListener( ( o, old, newV ) -> {
      if (newV.isEmpty()) {
        SessionManager.setSessionNumber( 0 );
      } else {
        SessionManager.setSessionNumber( Integer.valueOf( newV.trim() ) );
      }
    } );
  }

  /**
   * On schema-select: if the new value is null, hide right-hand-side.
   * Otherwise, populate right-hand-side with new schema's data
   */
  private void onSchemaSelect( ObservableValue< ? extends Schema > ov,
                               Schema oldV,
                               Schema newV )
  {
    SchemasManager.setSelected( newV );
    if (newV != null) {
      rightSide.setVisible( true );
      emptyMessage.setVisible( false );
      populateMappingsTable( newV );
      timeBox.setTime( newV.duration );
    } else {
      rightSide.setVisible( false );
      emptyMessage.setVisible( true );
    }
  }

  @FXML private void onCreateSchemaClicked( ActionEvent evt )
  {
    EditSchemaController.toEditSchemaView( null );
  }

  @FXML private void onEditSchemaClicked( ActionEvent evt )
  {
    EditSchemaController.toEditSchemaView( SchemasManager.getSelected() );
  }

  @FXML private void onStartClicked( ActionEvent evt )
  {
    RecordingController.toRecordingView();
  }
}
