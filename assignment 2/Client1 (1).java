import java.io.*;
import java.net.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.*;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.*;
import javafx.collections.*;
import javafx.event.*;

public class Client1 extends Application{

  public static String compName = "";
  public static String sharedPath = "";

  private BorderPane layout;
  private Button _btn1, _btn2;

  public void start(Stage primaryStage) throws Exception {
      String uploadArray[] = new String[1000];
      String downloadArray[] = new String[1000];

      try{
      Socket socket = new Socket("localhost", 6666);

      GridPane editArea = new GridPane();
      editArea.setPadding(new Insets(10, 10, 10, 10));
      editArea.setVgap(10);
      editArea.setHgap(10);

      _btn1 = new Button("DOWNLOAD");
    //  _btn1.setDefaultButton(true);
      editArea.add(_btn1, 0, 0);

      _btn1.setOnAction(new EventHandler<ActionEvent>(){
        public void handle(ActionEvent event){
          

        }
      });

      _btn2 = new Button("UPLOAD");
      editArea.add(_btn2,1,0);
      _btn2.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try{
                try{
              File folder = new File(sharedPath);
              String uploadArray[] = new String[1000];
              uploadArray = listFilesForFolder(folder);
              for (String val : uploadArray) {
                sendFile(val, socket);
              }
            }catch(java.net.SocketException e){System.out.println(e);}
          }catch(IOException e){System.out.println(e);}

          }
      });


      primaryStage.setTitle("File Sharer v1.0");

      // initialize the border pane
      Scene scene = new Scene(editArea, 600, 600);

      primaryStage.setScene(scene);

      primaryStage.show();

      final ObservableList<String> candidates = FXCollections.observableArrayList(uploadArray);
      final ListView<String> candidatesListView = new ListView<>(candidates);
      editArea.add(candidatesListView, 0, 3);

      final ObservableList<String> selected = FXCollections.observableArrayList(uploadArray);
      final ListView<String> heroListView = new ListView<>(selected);
      editArea.add(heroListView, 2, 3);

      socket.close();
    }catch(Exception e) {System.out.println(e);}
  }

    public void sendFile(String file, Socket s) throws IOException {
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    dos.flush();
    dos.writeUTF("Connection Established.");
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[4096];

		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}

		fis.close();
		dos.close();
	}


  public String[] listFilesForFolder(final File folder) {
    String fldr[] = new String[1000];
    int constVal = 0;

    for (final File fileEntry : folder.listFiles()) {
            fldr[constVal] = fileEntry.getName();
            constVal++;
    }

    return fldr;
  }

  public static void main(String[] args) {
            compName = args[0];
            sharedPath = args[1];

            System.out.println("Computer Name: " + compName
                              + "\nShared Folder Path: " + sharedPath);
            launch(args);

    }

}
