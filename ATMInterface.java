package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String pin;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}

class ATM {
    private User user;
    private double balance;

    public ATM(User user, double balance) {
        this.user = user;
        this.balance = balance;
    }

    public boolean authenticate(String userId, String pin) {
        return user.getUserId().equals(userId) && user.getPin().equals(pin);
    }

    public List<Transaction> getTransactionHistory() {
        return user.getTransactionHistory();
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        user.addTransaction(new Transaction("Withdraw", amount));
        return true;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            return;
        }
        balance += amount;
        user.addTransaction(new Transaction("Deposit", amount));
    }

    public boolean transfer(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        user.addTransaction(new Transaction("Transfer", amount));
        return true;
    }

    public double getBalance() {
        return balance;
    }
}

public class ATMInterface extends JFrame implements ActionListener {
    private ATM atm;
    private JTextField userIdField;
    private JPasswordField pinField;
    private JTextArea transactionHistoryArea;
    private JTextField amountField;

    public ATMInterface() {
        setTitle("ATM Interface");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(10);
        panel.add(userIdLabel);
        panel.add(userIdField);

        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField(10);
        panel.add(pinLabel);
        panel.add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        panel.add(loginButton);

        transactionHistoryArea = new JTextArea(5, 20);
        panel.add(new JScrollPane(transactionHistoryArea));

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);
        panel.add(amountLabel);
        panel.add(amountField);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(this);
        panel.add(withdrawButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(this);
        panel.add(depositButton);

        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(this);
        panel.add(transferButton);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Login")) {
            String userId = userIdField.getText();
            String pin = new String(pinField.getPassword());
            if (atm.authenticate(userId, pin)) {
                updateTransactionHistory();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid User ID or PIN. Please try again.");
            }
        } else if (e.getActionCommand().equals("Withdraw")) {
            double amount = Double.parseDouble(amountField.getText());
            if (atm.withdraw(amount)) {
                updateTransactionHistory();
                JOptionPane.showMessageDialog(null, "Withdrawal successful. Remaining balance: " + atm.getBalance());
            } else {
                JOptionPane.showMessageDialog(null, "Withdrawal failed. Insufficient balance or invalid amount.");
            }
        } else if (e.getActionCommand().equals("Deposit")) {
            double amount = Double.parseDouble(amountField.getText());
            atm.deposit(amount);
            updateTransactionHistory();
            JOptionPane.showMessageDialog(null, "Deposit successful. New balance: " + atm.getBalance());
        } else if (e.getActionCommand().equals("Transfer")) {
            double amount = Double.parseDouble(amountField.getText());
            if (atm.transfer(amount)) {
                updateTransactionHistory();
                JOptionPane.showMessageDialog(null, "Transfer successful. Remaining balance: " + atm.getBalance());
            } else {
                JOptionPane.showMessageDialog(null, "Transfer failed. Insufficient balance or invalid amount.");
            }
        }
    }

    private void updateTransactionHistory() {
        transactionHistoryArea.setText("");
        List<Transaction> transactions = atm.getTransactionHistory();
        for (Transaction transaction : transactions) {
            transactionHistoryArea.append(transaction.getType() + ": $" + transaction.getAmount() + "\n");
        }
    }

    public void setATM(ATM atm) {
        this.atm = atm;
    }

    public static void main(String[] args) {
        User user = new User("123456", "1234");
        ATM atm = new ATM(user, 1000.0);

        ATMInterface atmInterface = new ATMInterface();
        atmInterface.setATM(atm);
        atmInterface.setVisible(true);
    }
}
