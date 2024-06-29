package Components.Dialogs;

import javax.swing.*;

import Components.User.User;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IDVerifyDialog extends JDialog {

    public IDVerifyDialog(JFrame parentFrame, Map<UUID, User> users) {
        super(parentFrame, "User/Group ID Verify", true);
        setSize(300, 150);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        add(resultLabel, BorderLayout.CENTER);

        // Verify IDs
        if (areIDsUnique(users)) {
            resultLabel.setText("All IDs are valid.");
        } else {
            resultLabel.setText("ID conflict found.");
        }

        // Confirm button
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> dispose());
        add(confirmButton, BorderLayout.SOUTH);
    }

    private boolean areIDsUnique(Map<UUID, User> users) {
        Set<UUID> idSet = new HashSet<>();
        for (UUID id : users.keySet()) {
            if (!idSet.add(id)) {
                return false;  // Duplicate ID found
            }
        }
        return true;  // All IDs are unique
    }
}