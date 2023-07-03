package zandbak;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import javax.swing.*;
import java.io.File;

public class FileDialogTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;

    @Override
    protected void onSetUp() {
        // Create and show a JFrame containing a JButton that opens a file dialog
        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame("File Dialog Example");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton button = new JButton("Open File");
            button.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(f);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Process the selected file
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                }
            });

            f.add(button);
            f.pack();
            f.setVisible(true);

            return f;
        });

        // Create a Robot and associate it with the JFrame
        Robot robot = BasicRobot.robotWithCurrentAwtHierarchy();
        robot.settings().delayBetweenEvents(100); // Optional: Set a delay between events for better visualization

        // Initialize the FrameFixture
        window = new FrameFixture(robot, frame);
        window.show(); // Display the JFrame
    }

    @Override
    protected void onTearDown() {
        window.cleanUp(); // Clean up the FrameFixture resources
    }

    public void testFileDialog() {
        // Click the "Open File" button
        window.button("Open File").click();

        // Interact with the file dialog
        JFileChooserFixture fileChooser = window.fileChooser();
        fileChooser.setCurrentDirectory(new File("/path/to/initial/directory"));
        fileChooser.fileNameTextBox().setText("file.txt"); // Set the desired file name
        fileChooser.approve();

        // Additional assertions or verifications can be performed here
    }
}

