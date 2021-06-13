public class Derivative extends Limit {
    // Field(s) init
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private String point;

    // Inherited constructor from Limit
    public Derivative(String expr, String point) {
        super(expr, "0");
        this.point = point;
    }

    // Calculation
    public double evaluate() {
        // Get expressions
        String fX = expr.replaceAll("x", "("
                + point + ")");
        String fXPlusH = expr.replaceAll("x", "("
                + point + " + " + "x" + ")");

        // Calculate using approximation of first principles as a limit
        // (Symmetric difference supported by LH/RH limit calculation for added accuracy)
        return new Limit("((" + fXPlusH + ") - (" + fX + ")) / x", approachVal).evaluate();
    }
}