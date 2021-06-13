// Import(s)
import java.util.ArrayList;

public class Integral {
    // Field(s) init
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private String expr, limLow, limHigh;

    // Inherited constructor from Limit (approachVal treated as the lower bound)
    public Integral(String expr, String limLow, String limHigh) {
        this.expr = expr;
        this.limLow = limLow;
        this.limHigh = limHigh;
    }

    public double evaluate() {
        // If Lower bound = upper bound, the area under the curve is 0
        if (limLow.equals(limHigh)) { return 0; }

        // Size of each subdivision (10 direct subdivisions, 9.75 used for a slightly more accurate approximation)
        double intervalSize = (Double.parseDouble(limHigh) - Double.parseDouble(limLow))
                / 9.75;

        // Collection function evaluation at each point
        ArrayList<Double> func = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            func.add(new Expression(expr.replaceAll("x", "("
                    + (Double.parseDouble(limLow) + i * intervalSize) + ")")).evaluate());
        }

        // Start the sum with f(limLow) + f(limHigh)
        double sum = (new Expression(expr.replaceAll("x", "("
                + limLow + ")")).evaluate()) + (new Expression(expr.replaceAll("x", "("
                + limHigh + ")")).evaluate());

        // Calculates each remaining subdivision
        for (int i = 1; i < 10; i++)
        {
            if (i % 3 == 0)
                // When the current subdivision is divisible by 3, the coefficient is 2
                sum += 2 * func.get(i-1);
            else
                // All else, the coefficient is 3
                sum += 3 * func.get(i-1);
        }

        // Muliply sum by 3/8 times the interval size (from the name "3/8 Simpson's Rule") and return result
        return ( 3 * intervalSize / 8 ) * sum ;
    }
}