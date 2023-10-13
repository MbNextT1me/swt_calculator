package src;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public class Calculator {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.BORDER);
        shell.setText("Калькулятор");
        shell.setLayout(new GridLayout(4, true));
        shell.setMinimumSize(300, 300);
        shell.setSize(1000, 1000);
        
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        shell.setBackground(black);

        final Text text = new Text(shell, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

        FontData fontData = new FontData("Arial", 24, SWT.BOLD);
        Font font = new Font(display, fontData);
        text.setFont(font);

        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", "=", "+"
        };

        Color buttonColor = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);

        FontData buttonFontData = new FontData("Arial", 18, SWT.BOLD);
        Font buttonFont = new Font(display, buttonFontData);

        for (int i = 0; i < buttonLabels.length; i++) {
            Button button = new Button(shell, SWT.PUSH);
            button.setText(buttonLabels[i]);
            button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            button.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event event) {
                    Button clickedButton = (Button) event.widget;
                    String buttonText = clickedButton.getText();
                    String currentText = text.getText();

                    if (isNumber(buttonText) || buttonText.equals(".")) {
                        text.setText(currentText + buttonText);
                    } else if (buttonText.equals("C")) {
                        text.setText("");
                    } else if (buttonText.equals("=")) {
                        try {
                            double result = evaluateExpression(currentText);
                            text.setText(Double.toString(result));
                        } catch (Exception e) {
                            text.setText("Ошибка");
                        }
                    } else {
                        text.setText(currentText + " " + buttonText + " ");
                    }
                }
            });

            button.setBackground(buttonColor);
            button.setForeground(white);
            button.setFont(buttonFont);
        }

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        text.addDisposeListener(e -> {
            if (font != null && !font.isDisposed()) {
                font.dispose();
            }
        });
        text.dispose();
        display.dispose();
    }

    private static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        double result = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double operand = Double.parseDouble(tokens[i + 1]);
            if (operator.equals("+")) {
                result += operand;
            } else if (operator.equals("-")) {
                result -= operand;
            } else if (operator.equals("*")) {
                result *= operand;
            } else if (operator.equals("/")) {
                if (operand != 0) {
                    result /= operand;
                } else {
                    throw new ArithmeticException("Деление на ноль");
                }
            }
        }
        return result;
    }
}
