package Components.Dialogs;

import Components.User.User;

import javax.swing.*;
import java.awt.*;

public class LastUpdatedDialog extends JDialog {

    public LastUpdatedDialog(JFrame parentFrame, User lastUpdatedUser) {
        super(parentFrame, "Last Updated User", true);
        setSize(300, 150);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        add(resultLabel, BorderLayout.CENTER);

        if (lastUpdatedUser != null) {
            resultLabel.setText("<html>The last updated user is:<br>" + lastUpdatedUser.getName() +
                    "<br>UUID: " + lastUpdatedUser.getId() +
                    "<br>at " + lastUpdatedUser.getUpdatedTime() + "</html>");
        } else {
            resultLabel.setText("No users found.");
        }

        // Confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> dispose());
        add(confirmButton, BorderLayout.SOUTH);
    }
}