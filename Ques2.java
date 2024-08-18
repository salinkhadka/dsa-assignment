import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Ques2 extends JFrame {

    private JTextField inputField;
    private JButton[] buttons;
    private JLabel resultLabel;

    public Ques2() {
        setTitle("Basic Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        inputField.setFont(new Font("Calisto MT", Font.PLAIN, 25));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        buttons = new JButton[20];

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "(", "+",
                ")", "C", "=", " "
        };

        for (int i = 0; i < 20; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFont(new Font("Calisto MT", Font.BOLD, 18));
            buttons[i].setFocusPainted(false);

            if (buttonLabels[i].matches("[0-9]")) {
                buttons[i].setForeground(Color.ORANGE);
            } else if (buttonLabels[i].matches("[+\\-*/()]")) {
                buttons[i].setForeground(Color.PINK);
            }

            buttons[i].addActionListener(new ButtonClickListener());
            buttonPanel.add(buttons[i]);
        }

        add(buttonPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("Result: ");
        resultLabel.setFont(new Font("Calisto MT", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(resultLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();

            if (buttonText.equals("=")) {
                String expression = inputField.getText();
                try {
                    int result = evaluate(expression);
                    resultLabel.setText("Result: " + result);
                } catch (Exception ex) {
                    resultLabel.setText("Error: " + ex.getMessage());
                }
            } else if (buttonText.equals("C")) {
                inputField.setText("");
                resultLabel.setText("Result: ");
            } else {
                inputField.setText(inputField.getText() + buttonText);
            }
        }
    }

    private int evaluate(String expression) throws Exception {
        expression = expression.replaceAll("\\s+", "");
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') continue;
            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    sb.append(expression.charAt(i++));
                }
                values.push(Integer.parseInt(sb.toString()));
                i--;
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                    values.push(applyOp(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(ch);
            }
        }
        while (!operators.isEmpty()) {
            values.push(applyOp(operators.pop(), values.pop(), values.pop()));
        }
        return values.pop();
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private int applyOp(char op, int b, int a) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new Exception("Cannot divide by zero");
                return a / b;
            default:
                throw new Exception("Invalid operator: " + op);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Ques2::new);
    }
}
