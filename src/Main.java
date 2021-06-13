// Import(s)
import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

// Main class
@SuppressWarnings("DuplicatedCode")
public class Main {
    // Menu for limit calculation
    static void limitMenu(Scanner s) {
        while (true) {
            // Collect input
            double result;
            System.out.println("\nEnter the function you want to take the limit of:");
            String func = s.nextLine();
            System.out.println("Enter the approaching value:");
            String av = s.nextLine();

            // Check if input is a proper expression, else state the error
            try {
                result = new Limit(func, av).evaluate();
            } catch (RuntimeException e) {
                System.out.println("\n" + e.getMessage());

                // Collect input again
                continue;
            }

            // If the limit is undefined, state as DNE, else state as normal
            System.out.println(Double.isNaN(result) ? "\nThe result is DNE" : "\nThe result is " + result);

            // Write calculation to history (including date and time)
            try {
                FileWriter writer = new FileWriter("history.txt",true);
                writer.write("\nlimit " + func + " as x -> " + av
                        + " = " + result + "   --" + LocalDate.now() + " " + LocalTime.now());
                writer.close();
            } catch (IOException e) {
                // Prints if the input did not go through correctly
                System.out.println("Failed to add to history");
                break;
            }

            break;
        }
    }

    // Menu for derivative calculation
    static void derivativeMenu(Scanner s) {
        while (true) {
            // Collect input
            double result;
            System.out.println("\nEnter the function you want to take the derivative of:");
            String func = s.nextLine();
            System.out.println("Enter the x-value you want to take the derivative at:");
            String pt = s.nextLine();

            // Check if input is a proper expression, else state the error
            try {
                result = new Limit(func, pt).evaluate();
            } catch (RuntimeException e) {
                System.out.println("\n" + e.getMessage());

                // Collect input again
                continue;
            }

            // If the limit is undefined, state as Undefined, else state as normal
            System.out.println(Double.isNaN(result) ? "\nThe result is Undefined" : "\nThe result is " + result);

            // Write calculation to history (including date and time)
            try {
                FileWriter writer = new FileWriter("history.txt",true);
                writer.write("\nderivative " + func + " at (x = " + pt + ") = "
                        + result + "   --" + LocalDate.now() + " " + LocalTime.now());
                writer.close();
            } catch (IOException e) {
                // Prints if the input did not go through correctly
                System.out.println("Failed to add to history");
                break;
            }

            break;
        }
    }

    // Menu for integral calculation
    @SuppressWarnings("UnusedAssignment")
    static void integralMenu(Scanner s) {
        while (true) {
            // Collect input
            double result;
            System.out.println("\nEnter the function you want to calculate the integral of:");
            String func = s.nextLine();
            System.out.println("Enter the lower bound:");
            String lb = s.nextLine();
            System.out.println("Enter the upper bound:");
            String ub = s.nextLine();

            // Check if input is a proper expression, else state the error
            try {
                result = new Expression(func.replaceAll("x", "(1)")).evaluate();
            } catch (RuntimeException e) {
                System.out.println("\n" + e.getMessage());

                // Collect input again
                continue;
            }

            // Check if the result is divergent by out-of-bounds index in Simpson's rule
            try {
                result = new Integral(func, lb, ub).evaluate();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("\nThe result is Divergent");
                break;
            }

            // Check if the result is divergent by infinite answer
            if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
                System.out.println("\nThe result is Divergent");
                break;
            }

            // Print the result
            System.out.println("\nThe result is " + result);

            // Write calculation to history (including date and time)
            try {
                FileWriter writer = new FileWriter("history.txt",true);
                writer.write("\nintegral " + func + " dx from " + lb + " to "
                        + ub + " = " + result + "   --" + LocalDate.now() + " " + LocalTime.now());
                writer.close();
            } catch (IOException e) {
                // Prints if the input did not go through correctly
                System.out.println("Failed to add to history");
                break;
            }

            break;
        }
    }

    public static void main(String[] args) {
        // Intro screen with logo
        System.out.println("            /$$$$$$            /$$          " + "\n"
                + "           /$$__  $$          | $$          " + "\n"
                + " /$$$$$$$ | $$  \\__/  /$$$$$$ | $$  /$$$$$$$" + "\n"
                + "| $$__  $$| $$       |____  $$| $$ /$$_____/" + "\n"
                + "| $$  \\ $$| $$        /$$$$$$$| $$| $$      " + "\n"
                + "| $$  | $$| $$    $$ /$$__  $$| $$| $$      " + "\n"
                + "| $$  | $$|  $$$$$$/|  $$$$$$$| $$|  $$$$$$$" + "\n"
                + "|__/  |__/ \\______/  \\_______/|__/ \\_______/" + "\n");
        System.out.println("          Numerical Calculus Solver          ");

        Scanner sc = new Scanner(System.in);
        String in;

        // Create history text file (if it does not exist already)
        File file = new File("history.txt");
        if (!file.exists()){
            try {
                FileWriter writer = new FileWriter("history.txt");
                writer.write("[CALCULATION HISTORY]");
                writer.close();
            } catch (IOException e) {
                // Prints if the operation did not go through correctly
                System.out.println("The history file could not be created");
            }
        }

        // Main menu
        while (true) {
            System.out.println("\nC͟a͟l͟c͟u͟l͟a͟t͟e:\n"
                    + "1 - Limit\n"
                    + "2 - Derivative\n"
                    + "3 - Integral\n"
                    + "4 - Show Info\n"
                    + "5 - Clear History\n"
                    + "6 - Exit");
//            sc.nextLine();
            in = sc.nextLine();

            // Cases to go to each menu
            switch (in) {
                case "1":
                    // Calculate limit
                    limitMenu(sc);
                    break;
                case "2":
                    // Calculate derivative
                    derivativeMenu(sc);
                    break;
                case "3":
                    // Calculate integral
                    integralMenu(sc);
                    break;
                case "4":
                    // Show information about capabilities and functions (unicode bolded text included)
                    System.out.println("\nA͟v͟a͟i͟l͟a͟b͟l͟e͟ ͟f͟u͟n͟c͟t͟i͟o͟n͟s͟ ͟(͟a͟l͟l͟ ͟i͟n͟ ͟t͟e͟r͟m͟s͟ ͟o͟f͟ ͟x͟)\n"
                            + "Normal: a+b, a-b, a*b, a/b, b*(a+b), a^b\n"
                            + "Constants: π ≈ 3.14159265359, e ≈ 2.71828182845\n"
                            + "Special: sqrt(x), cbrt(x), sin(x), cos(x), tan(x), csc(x), sec(x), cot(x), log(x), ln(x) [trig in radians]"
                            + "(All expressions parsed in recursive descent)");
                    System.out.println("\nL͟i͟m͟i͟t͟ ͟S͟o͟l͟v͟e͟r\n"
                            + "Calculates the limit of a function by approximating the approaching"
                            + "value on the \nleft side and right side (prompted for <function> and then <approachingValue>). \n"
                            + "\uD835\uDDDF\uD835\uDDF6\uD835\uDDFA\uD835\uDDF6\uD835\uDE01\uD835\uDE00 \uD835\uDDEE\uD835\uDE01 "
                            + "\uD835\uDDF6\uD835\uDDFB\uD835\uDDF3\uD835\uDDF6\uD835\uDDFB\uD835\uDDF6\uD835\uDE01\uD835\uDE06 "
                            + "are \uD835\uDDFB\uD835\uDDFC\uD835\uDE01 \uD835\uDE00\uD835\uDE02\uD835\uDDFD\uD835\uDDFD\uD835"
                            + "\uDDFC\uD835\uDDFF\uD835\uDE01\uD835\uDDF2\uD835\uDDF1.");
                    System.out.println("\nD͟e͟r͟i͟v͟a͟t͟i͟v͟e͟ ͟S͟o͟l͟v͟e͟r\n"
                            + "Calculates the \uD835\uDDF3\uD835\uDDF6\uD835\uDDFF\uD835\uDE00\uD835\uDE01 derivative of a "
                            + "function at a defined x-value by approximating \nthe difference quotient as a limit (prompted "
                            + "for <function> and then <xValue>).");
                    System.out.println("\nI͟n͟t͟e͟g͟r͟a͟l͟ ͟S͟o͟l͟v͟e͟r\n"
                            + "Calculates the definite integral of a function between two x-value bounds by approximation using \n"
                            + "the 3/8 Simpson's Rule (prompted for <function> then <lowerBound> and finally <upperBound>).\n"
                            + "\uD835\uDDDC\uD835\uDDFA\uD835\uDDFD\uD835\uDDFF\uD835\uDDFC\uD835\uDDFD\uD835\uDDF2\uD835\uDDFF "
                            + "\uD835\uDDDC\uD835\uDDFB\uD835\uDE01\uD835\uDDF2\uD835\uDDF4\uD835\uDDFF\uD835\uDDEE\uD835\uDDF9\uD835\uDE00 "
                            + "are \uD835\uDDFB\uD835\uDDFC\uD835\uDE01 \uD835\uDE00\uD835\uDE02\uD835\uDDFD\uD835\uDDFD\uD835\uDDFC\uD835"
                            + "\uDDFF\uD835\uDE01\uD835\uDDF2\uD835\uDDF1.");
                    break;
                case "5":
                    // Clear calculation history
                    try {
                        FileWriter writer = new FileWriter("history.txt");
                        writer.write("[CALCULATION HISTORY]");
                        writer.close();
                    } catch (IOException e) {
                        // Prints if the operation did not go through correctly
                        System.out.println("An error occured");
                    }
                    break;
                case "6":
                    // Exit program (status 0 for success)
                    System.exit(0);
            }
        }
    }
}