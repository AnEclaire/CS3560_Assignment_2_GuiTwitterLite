package Components.Dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountDisplayDialog extends JDialog {

    public CountDisplayDialog(JFrame parent, String title, String message) {
        super(parent, title, true); // true makes it modal

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message);
        panel.add(label, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dialog
            }
        });
        panel.add(confirmButton, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(panel);
        pack(); // Size the dialog to fit its contents
        setLocationRelativeTo(parent); // Center the dialog relative to the parent frame
    }
}
