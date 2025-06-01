package com.hai811i.mobileproject.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hai811i.mobileproject.R;

public class DndFragment extends Fragment {
    private NotificationManager notificationManager;
    private Switch dndToggle;
    private GridLayout ticTacToeBoard;
    private TextView gameStatus;
    private Button resetButton;
    private boolean isPlayerXTurn = true;
    private String[][] boardState = new String[3][3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dnd, container, false);

        // Initialize views
        dndToggle = view.findViewById(R.id.dnd_toggle);
        ticTacToeBoard = view.findViewById(R.id.tic_tac_toe_board);
        gameStatus = view.findViewById(R.id.game_status);
        resetButton = view.findViewById(R.id.reset_button);

        // Get notification manager
        notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        dndToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableDoNotDisturb();  // Changed from isDoNotDisturbEnabled()
            } else {
                disableDoNotDisturb();
            }
        });

        // Initialize Tic-Tac-Toe game
        initializeGame();

        return view;
    }

    private void enableDoNotDisturb() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
                Toast.makeText(getContext(), "Please grant DND permission", Toast.LENGTH_LONG).show();
            } else {
                try {
                    NotificationManager.Policy policy = new NotificationManager.Policy(
                            NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS,
                            NotificationManager.Policy.PRIORITY_SENDERS_ANY,
                            0);
                    notificationManager.setNotificationPolicy(policy);

                    // Verify the change was applied
                    if (isDoNotDisturbEnabled()) {
                        Toast.makeText(getContext(), "Do Not Disturb enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to enable DND", Toast.LENGTH_SHORT).show();
                        dndToggle.setChecked(false);
                    }
                } catch (SecurityException e) {
                    Toast.makeText(getContext(), "Permission denied: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    dndToggle.setChecked(false);
                }
            }
        } else {
            Toast.makeText(getContext(), "Do Not Disturb requires Android 6.0+", Toast.LENGTH_SHORT).show();
            dndToggle.setChecked(false);
        }
    }

    private void disableDoNotDisturb() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager.Policy policy = new NotificationManager.Policy(
                    NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS |
                            NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS |
                            NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS |
                            NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES |
                            NotificationManager.Policy.PRIORITY_CATEGORY_CALLS |
                            NotificationManager.Policy.PRIORITY_CATEGORY_SYSTEM,
                    NotificationManager.Policy.PRIORITY_SENDERS_ANY,
                    0);
            notificationManager.setNotificationPolicy(policy);
            Toast.makeText(getContext(), "Do Not Disturb disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeGame() {
        // Clear the board
        ticTacToeBoard.removeAllViews();

        // Initialize board state
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = "";
            }
        }

        // Create the game board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button(getContext());
                button.setLayoutParams(new GridLayout.LayoutParams());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(j, 1f);
                params.rowSpec = GridLayout.spec(i, 1f);
                params.setMargins(8, 8, 8, 8);
                button.setLayoutParams(params);
                button.setTextSize(32);
                button.setBackgroundResource(R.drawable.button_border);

                final int row = i;
                final int col = j;

                button.setOnClickListener(v -> {
                    if (boardState[row][col].isEmpty()) {
                        if (isPlayerXTurn) {
                            button.setText("X");
                            boardState[row][col] = "X";
                        } else {
                            button.setText("O");
                            boardState[row][col] = "O";
                        }

                        if (checkForWin()) {
                            String winner = isPlayerXTurn ? "X" : "O";
                            gameStatus.setText("Player " + winner + " wins!");
                            disableAllButtons();
                        } else if (isBoardFull()) {
                            gameStatus.setText("It's a draw!");
                        } else {
                            isPlayerXTurn = !isPlayerXTurn;
                            gameStatus.setText("Player " + (isPlayerXTurn ? "X" : "O") + "'s turn");
                        }
                    }
                });

                ticTacToeBoard.addView(button);
            }
        }

        // Set up reset button
        resetButton.setOnClickListener(v -> {
            isPlayerXTurn = true;
            gameStatus.setText("Player X's turn");
            initializeGame();
        });
    }

    private boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!boardState[i][0].isEmpty() &&
                    boardState[i][0].equals(boardState[i][1]) &&
                    boardState[i][0].equals(boardState[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!boardState[0][j].isEmpty() &&
                    boardState[0][j].equals(boardState[1][j]) &&
                    boardState[0][j].equals(boardState[2][j])) {
                return true;
            }
        }

        // Check diagonals
        if (!boardState[0][0].isEmpty() &&
                boardState[0][0].equals(boardState[1][1]) &&
                boardState[0][0].equals(boardState[2][2])) {
            return true;
        }

        if (!boardState[0][2].isEmpty() &&
                boardState[0][2].equals(boardState[1][1]) &&
                boardState[0][2].equals(boardState[2][0])) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableAllButtons() {
        for (int i = 0; i < ticTacToeBoard.getChildCount(); i++) {
            ticTacToeBoard.getChildAt(i).setEnabled(false);
        }
    }
    private boolean isDoNotDisturbEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NotificationManager.Policy policy = notificationManager.getNotificationPolicy();
            return policy != null &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_ALARMS) != 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS) == 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_REMINDERS) == 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_EVENTS) == 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_MESSAGES) == 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_CALLS) == 0 &&
                    (policy.priorityCategories & NotificationManager.Policy.PRIORITY_CATEGORY_SYSTEM) == 0;
        }
        return false;
    }
}