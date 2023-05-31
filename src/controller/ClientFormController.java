package controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.*;
import java.net.Socket;

public class ClientFormController extends Thread {
    public Label userName;
    public VBox vbox_messages;
    public TextField txtTyping;
    public Button btn_send;


    BufferedReader reader;
    PrintWriter writer;
    Socket socket;


    public void initialize() throws IOException {
        String userName=LoginFormController.name;
        this.userName.setText(userName);
        try {
            socket = new Socket("localhost", 4000);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {


                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];


                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]+" ");
                }


                String[] msgToAr = msg.split(" ");
                String st = "";
                for (int i = 0; i < msgToAr.length - 1; i++) {
                    st += msgToAr[i + 1] + " ";
                }


                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);

                }



                    TextFlow tempFlow = new TextFlow();


                    if (!cmd.equalsIgnoreCase(userName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color: rgb(15,125,242);" +
                                " -fx-background-radius: 10px");
                        tempFlow.setPadding(new Insets(3,10,3,10));
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200); //200

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12); //12




                    if (!cmd.equalsIgnoreCase(userName.getText() + ":")) {


                        vbox_messages.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                        hBox.setPadding(new Insets(5,5,5,10));

                    } else {

                        Text text2 = new Text(fullMsg + ": Me");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2,5,2,10));

                       flow2.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color: rgb(191,241,9);" +
                                "-fx-background-radius: 10px");
                        flow2.setPadding(new Insets(3,10,3,10));

                    }

                    Platform.runLater(() -> vbox_messages.getChildren().addAll(hBox));
                }
            } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void sendmsgOnAction(ActionEvent actionEvent) {
        String msg = txtTyping.getText();
        writer.println(userName.getText() + ": " + msg);
        txtTyping.clear();

        if(msg.equalsIgnoreCase("!Bye") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }
    }

    public void ImageMouseClicked(MouseEvent mouseEvent) {

    }

    public void entersend(ActionEvent actionEvent) {
        btn_send.fire();
    }
}


