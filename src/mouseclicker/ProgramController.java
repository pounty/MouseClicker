package mouseclicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.stream.Stream;

public class ProgramController implements Initializable {
    @FXML TextField tfintervall;

    static Integer intervall_value;

    static Timer referenceTimer;

    public ProgramController() {}

    static void startProgram() throws IOException {
        System.out.println("Starte Programm");
        System.out.println("Intervall value: " + intervall_value);
        FileWriter fileWriter = new FileWriter("settings.cfg");
        fileWriter.write(intervall_value.toString());
        fileWriter.close();

        Timer timer = new Timer(true);
        timer.schedule(new MoveMouseTimer(), 0, intervall_value * 1000);
        getTimerReference(timer);
    }

    static void stopProgram() {
        System.out.println("Stoppe Programm");
        referenceTimer.cancel();
        referenceTimer.purge();
    }

    private static void getTimerReference(Timer timer) {
        referenceTimer = timer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        String filepath = "settings.cfg";

        tfintervall.textProperty().addListener((obs, oldText, newText) -> {
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter("settings.cfg");
                fileWriter.write(newText);
                fileWriter.close();
                intervall_value = new Integer(newText);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        if (new File(filepath).isFile()) {
            tfintervall.setText(readLineByLineJava8(filepath));
        }

        intervall_value = new Integer(tfintervall.getText());
    }

    private String readLineByLineJava8(String filepath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filepath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}