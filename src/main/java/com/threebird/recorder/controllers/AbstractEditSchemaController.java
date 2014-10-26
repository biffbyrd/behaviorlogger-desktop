package com.threebird.recorder.controllers;

import com.threebird.recorder.models.Schema;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class AbstractEditSchemaController
{
  @FXML protected VBox mappingsBox;
  @FXML protected Button addRowButton;

  @FXML protected TextField hoursField;
  @FXML protected TextField minutesField;
  @FXML protected TextField secondsField;

  protected static int defaultNumBoxes = 10;
  protected static String digits = "0123456789";
  protected static String acceptableKeys =
      "abcdefghijklmnopqrstuvwxyz1234567890`-=[]\\;',./";
  private static final Insets insets = new Insets( .5, .5, .5, .5 );

  protected static int strToInt( String s )
  {
    return Integer.valueOf( s.isEmpty() ? "0" : s );
  }

  /**
   * Converts the contents of hoursField, minutesField, and secondsField into
   * the equivalent number of seconds and
   */
  protected int getDuration()
  {
    Integer hours = strToInt( hoursField.getText() );
    Integer mins = strToInt( minutesField.getText() );
    Integer secs = strToInt( secondsField.getText() );
    return (hours * 60 * 60) + (mins * 60) + secs;
  }

  /**
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
  protected EventHandler< ? super KeyEvent >
    createFieldLimiter( TextField field, String acceptableKeys, int limit )
  {
    return evt -> {
      if (field.getText().trim().length() == limit
          || !acceptableKeys.contains( evt.getCharacter() ))
        evt.consume();
    };
  }

  /**
   * Adds 2 adjacent text fields and a checkbox to 'mappingsBox'. Attaches a
   * KeyTyped EventHandler to the first field that prevents the user from typing
   * more than 1 key
   */
  protected void addMappingBox( String key, String behavior )
  {
    TextField keyField = new TextField( key );
    keyField.setMaxWidth( 40 );
    HBox.setHgrow( keyField, Priority.NEVER );
    HBox.setMargin( keyField, insets );
    keyField.setOnKeyTyped( createFieldLimiter( keyField, acceptableKeys, 1 ) );

    TextField behaviorField = new TextField( behavior );
    HBox.setHgrow( behaviorField, Priority.ALWAYS );
    HBox.setMargin( behaviorField, insets );

    CheckBox checkbox = new CheckBox();
    HBox.setHgrow( checkbox, Priority.NEVER );
    HBox.setMargin( checkbox, new Insets( 3, 30, .5, 10 ) );

    mappingsBox.getChildren()
               .add( new HBox( keyField, behaviorField, checkbox ) );
  }

  /**
   * Runs through the key-behavior pairs in schema and populates the mappingsBox
   */
  protected void populateMappingsBox( Schema schema )
  {
    mappingsBox.getChildren().clear();

    schema.mappings.forEach( ( key, mapping ) -> {
      addMappingBox( mapping.key.toString(), mapping.behavior );
    } );

    // add some extra boxes at the end to match 'defaultNumBoxes'
    for (int i = schema.mappings.size(); i < defaultNumBoxes; i++) {
      addMappingBox( "", "" );
    }
  }

  /**
   * When user clicks "Add Row" under the key-behavior mappingsBox
   */
  @FXML protected void onAddRowClicked( ActionEvent evt )
  {
    addMappingBox( "", "" );
  }

}