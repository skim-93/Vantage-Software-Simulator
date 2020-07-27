package gui.controller;

import gui.model.DeserializedData;
import gui.view.Frame;
import java.awt.EventQueue;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

//Checkstyle and PMD: done - Hung Vu.
/**
 * This class changes the GUI based on given weather data.
 * 
 * @author Hung Vu
 *
 */
@SuppressWarnings({
  
    "deprecation", "PMD.SystemPrintln", "PMD.StdCyclomaticComplexity",
    "PMD.AssignmentToNonFinalStatic", "PMD.ModifiedCyclomaticComplexity",
    "PMD.CyclomaticComplexity", "PMD.AvoidCatchingGenericException",
    "PMD.AvoidCatchingNPE", "PMD.LawOfDemeter", "PMD.AvoidFileStream",
    "PMD.CloseResource"
    
  })
public class Controller implements Observer, Runnable {

  /**
   * The deserialized data.
   */
  private static final DeserializedData DESERIALIZED_DATA = new DeserializedData();

  /**
   * Sleep time for a thread.
   */
  private static final long SLEEP_TIME = 3000L;

  /**
   * The GUI's display.
   */
  private static Frame myFrame;

  /**
   * Default constructor for controller.
   */
  public Controller() {

    DESERIALIZED_DATA.addObserver(this);

    // Start the frame in in another thread.
    // For this project, it won't matter, but not a recommended approach though.
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {

        myFrame = new Frame();

      }

    });
  }

  /**
   * Get data after a certain time.
   */
  @Override
  public void run() {

    try {

      receiver();
      Thread.sleep(SLEEP_TIME);
      run();

    } catch (InterruptedException e) {

      System.out.println("Controller thread error");

    }

  }

  /**
   * Update the GUI after the data has been changed.
   */
  @Override
  public void update(final Observable theO, final Object arg) {

    // Update the frame (on a separated thread) with given data.
    try {

      // Pass the values to their respective places.
      for (final Map.Entry<String, String> entry : DESERIALIZED_DATA.getData().entrySet()) {

        if (entry.getKey().equals("Temp in")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Baro pressure")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Baro trend")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Temp out")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Station number")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Wind direction")) {

          myFrame.getMyWindCompassPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Rain")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Wind speed")) {

          myFrame.getMyWindCompassPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Rain rate")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Hum out")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Wind chill")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Hum in")) {

          myFrame.getMyDataPanel().changeDisplay(entry.getKey(), entry.getValue());

        } else if (entry.getKey().equals("Rain graph")) {

          myFrame.getMyGraphPanel().changeDisplay(entry.getKey(), entry.getValue());
          // Haven't implemented (07-24)
          // Not working (07-26)

        }

      }

      // Update current weather state (icons)
      myFrame.getMyWeatherPanel().changeDisplay(

          DESERIALIZED_DATA.getData().get("Temp out"), DESERIALIZED_DATA.getData().get("Hum out"),
          DESERIALIZED_DATA.getData().get("Rain rate")

      );

      // Display statement about current weather state.
      myFrame.getMyMessagePanel().changeDisplay(

          DESERIALIZED_DATA.getData().get("Temp out"), DESERIALIZED_DATA.getData().get("Hum out"),
          DESERIALIZED_DATA.getData().get("Rain rate")

      );

    } catch (NullPointerException e) {

      System.out.println(

          "'Frame' field in controller has just been partially created using thread.\n" 
          + "Escape (this) error. \n" + "Non-destructive error. Ignore it. \n"

      );

    }

  }

  /**
   * Receive the data, deserialize then update the model.
   */
  public void receiver() {

    // Clear the model from last run.
    DESERIALIZED_DATA.getData().clear();

    // Deserialization process.
    try {

      final FileInputStream dataInput = new FileInputStream("data.ser");
      final ObjectInputStream objectInput = new ObjectInputStream(dataInput);

      // Read all object in the stream.
      while (true) {

        try {

          // Received data: String format - "<DataName>: <value>"
          final String data = (String) objectInput.readObject();
          final String[] keyAndValue = data.split(": ");

          // Update model with new key and value.
          DESERIALIZED_DATA.setData(keyAndValue[0], keyAndValue[1]);

        } catch (EOFException e) {
          // End of file, break the loop.

          break;

        } catch (ClassNotFoundException e) {

          System.out.println("Error occured in controller.Controller.receiver()");

        }

      }

      objectInput.close();
      dataInput.close();

    } catch (IOException e) {

      System.out.println("Error occured in controller.Controller.receiver()");

    }

  }

}
