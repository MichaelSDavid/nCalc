@SuppressWarnings({"DuplicatedCode", "FieldMayBeFinal"})
public class Limit {
    // Field(s) init
    protected String expr;
    protected String approachVal;
    private String dxRight = "0.00000001";
    private String dxLeft = "-" + dxRight;
    private String lightDxR = "0.001";
    private String lightDxL = "-" + lightDxR;
    private String midDxR = "0.00001";
    private String midDxL = "-" + midDxR;

    // Constructor
    public Limit(String expr, String approachVal) {
        this.expr = expr;
        this.approachVal = approachVal;
    }

    // LH limit calculation
    private double evalLH() {
        if (expr.toLowerCase().contains("x")) {
            // Calculate inputs and inch forward ever so slightly with each one (and selections for approx later)
            double largeInch = new Expression(expr.replaceAll("x", "("
                    + approachVal + " - 0.01" + ")")).evaluate();
            String lSelection = Double.toString(largeInch).substring(Double.toString(largeInch).indexOf(".") + 1);
            double initial = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + lightDxL + ")")).evaluate();
            String initSelection = Double.toString(initial).substring(Double.toString(initial).indexOf(".") + 1);
            double middle = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + midDxL + ")")).evaluate();
            double second = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + dxLeft + ")")).evaluate();

            // Infinite cases
            if ((second-middle) > (middle-initial) && (second-middle) > 0) {
                // If the inputs grow with each difference and are positive, return +inf
                return Double.POSITIVE_INFINITY;
            } else if (((second-middle) < (middle-initial) && (second-middle) < 0)) {
                // If the inputs grow with each difference and are negative, return -inf
                return Double.NEGATIVE_INFINITY;
            }

            // Use decimal selections to choose the closer limit value (that is not undefined)
            if (!Double.isNaN(largeInch)) {
                if (lSelection.length() >= 6) {
                    if (lSelection.startsWith("999999") || lSelection.startsWith("000000")) {
                        // More accurate approach
                        return largeInch;
                    }
                } else if (lSelection.equals("0")) {
                    // If pure double like "x.0"
                    return largeInch;
                }
            } else if (!Double.isNaN(initial)) {
                if (initSelection.length() >= 6) {
                    if (initSelection.startsWith("999999") || initSelection.startsWith("000000")) {
                        // More accurate approach
                        return initial;
                    }
                } else if (initSelection.equals("0")) {
                    // If pure double like "x.0"
                    return initial;
                }
            }
            return second;
        }
        // Limit of a constant is itself
        return new Expression(expr).evaluate();
    }

    private double evalRH() {
        if (expr.toLowerCase().contains("x")) {
            // Calculate inputs and inch forward ever so slightly with each one (and selections for approx later)
            double largeInch = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + 0.01" + ")")).evaluate();
            String lSelection = Double.toString(largeInch).substring(Double.toString(largeInch).indexOf(".") + 1);
            double initial = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + lightDxR + ")")).evaluate();
            String initSelection = Double.toString(initial).substring(Double.toString(initial).indexOf(".") + 1);
            double middle = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + midDxR + ")")).evaluate();
            double second = new Expression(expr.replaceAll("x", "("
                    + approachVal + " + " + dxRight + ")")).evaluate();

            // Infinite cases
            if ((second-middle) > (middle-initial) && (second-middle) > 0) {
                // If the inputs grow with each difference and are positive, return +inf
                return Double.POSITIVE_INFINITY;
            } else if (((second-middle) < (middle-initial) && (second-middle) < 0)) {
                // If the inputs grow with each difference and are negative, return -inf
                return Double.NEGATIVE_INFINITY;
            }

            // Use decimal selections to choose the closer limit value (that is not undefined)
            if (!Double.isNaN(largeInch)) {
                if (lSelection.length() >= 6) {
                    if (lSelection.startsWith("999999") || lSelection.startsWith("000000")) {
                        // More accurate approach
                        return largeInch;
                    }
                } else if (lSelection.equals("0")) {
                    // If pure double like "x.0"
                    return largeInch;
                }
            } else if (!Double.isNaN(initial)) {
                if (initSelection.length() >= 6) {
                    if (initSelection.startsWith("999999") || initSelection.startsWith("000000")) {
                        // More accurate approach
                        return initial;
                    }
                } else if (initSelection.equals("0")) {
                    // If pure double like "x.0"
                    return initial;
                }
            }
            return second;
        }
        // Limit of a constant is itself
        return new Expression(expr).evaluate();
    }

    public double evaluate() {
        // Average of 2 LH/RH limits will be a more accurate approximation for small inches
        double result = (evalLH() + evalRH()) / 2;

        // Correct the result in case it is shown as "-0.0"
        result = result == -0.0 ? 0.0 : result;

        // Cutoff range for LH not equal to RH (return DNE)
        if (evalRH() - evalLH() > 0.0001) {
            return Double.NaN;
        }

        // Extra rounding using decimal selection
        String selection = Double.toString(result).substring(Double.toString(result).indexOf(".") + 1);
        if (selection.length() >= 6) {
            if (selection.startsWith("999999")) {
                // Round up
                return Math.ceil(result);
            } else if (selection.startsWith("000000")) {
                // Round down
                return Math.floor(result);
            }
        }
        return result;
    }
}