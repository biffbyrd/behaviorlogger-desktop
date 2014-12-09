package com.threebird.recorder.utils;

import com.threebird.recorder.EventRecorder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EventRecorderUtil
{
  /**
   * Creates an EventHandler that will prevent a field's text from containing
   * any characters not in "acceptableKeys" and from exceeding the character
   * limit
   * 
   * @param field
   *          - the input-field that this EventHandler reads
   * @param acceptableKeys
   *          - a list of characters that the user is allowed to input
   * @param limit
   *          - the max length of the field
   * @return an EventHandler that consumes a KeyEvent if the typed Char is
   *         outside 'acceptableKeys' or if the length of 'field' is longer than
   *         'limit'
   */
  public static EventHandler< ? super KeyEvent >
    createFieldLimiter( TextField field, char[] acceptableKeys, int limit )
  {
    return evt -> {
      if (field.getText().trim().length() == limit
          || !String.valueOf( acceptableKeys ).contains( evt.getCharacter() ))
        evt.consume();
    };
  }

  /**
   * Display a simple dialog-box that displays a message and allows the user to
   * click two buttons
   * 
   * @param msg
   *          - the main message to display
   * @param leftBtn
   *          - the text displayed on the left-button
   * @param rightBtn
   *          - the text displayed on the right-button
   * @param onLeftClicked
   *          - what happens when user clicks left-button
   * @param onRightClicked
   *          - what happens when user clicks right-button
   */
  public static void dialogBox( String msg,
                                String leftBtn,
                                String rightBtn,
                                EventHandler< ActionEvent > onLeftClicked,
                                EventHandler< ActionEvent > onRightClicked )
  {
    Stage dialogStage = new Stage();
    dialogStage.initModality( Modality.WINDOW_MODAL );
    dialogStage.initStyle( StageStyle.UTILITY );
    dialogStage.initOwner( EventRecorder.STAGE );
    Label question = new Label( msg );
    question.setAlignment( Pos.BASELINE_CENTER );

    Button left = new Button( leftBtn );
    left.setOnAction( evt -> {
      dialogStage.close();
      onLeftClicked.handle( evt );
    } );

    Button right = new Button( rightBtn );
    right.setOnAction( evt -> {
      dialogStage.close();
      onRightClicked.handle( evt );
    } );

    HBox hBox = new HBox();
    hBox.setAlignment( Pos.BASELINE_RIGHT );
    hBox.setSpacing( 20.0 );
    hBox.getChildren().addAll( left, right );

    VBox vBox = new VBox();
    vBox.setSpacing( 20.0 );
    vBox.getChildren().addAll( question, hBox );
    vBox.setPadding( new Insets( 10 ) );

    dialogStage.setScene( new Scene( vBox ) );
    dialogStage.show();
  }

}
