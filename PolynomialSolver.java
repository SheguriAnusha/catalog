import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileReader;
import java.math.BigInteger;

public class PolynomialSolver {

    public static void main(String[] args) throws Exception {
        // Step 1: Parse JSON file
        FileReader reader = new FileReader("input.json");
        JSONTokener tokener = new JSONTokener(reader);
        JSONObject input = new JSONObject(tokener);

        // Extract keys
        JSONObject keys = input.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        double[][] points = new double[k][2];
        int index = 0;

        // Step 2: Decode roots
        for (String key : input.keySet()) {
            if (key.equals("keys")) continue;

            JSONObject root = input.getJSONObject(key);
            int x = Integer.parseInt(key);
            String value = root.getString("value");
            int base = root.getInt("base");

            // Decode y value using base
            BigInteger decodedY = new BigInteger(value, base);
            points[index][0] = x;
            points[index][1] = decodedY.doubleValue();
            index++;

            if (index == k) break; // Use only k points
        }

        // Step 3: Find constant term using Lagrange Interpolation
        double c = calculateConstant(points, k);
        System.out.println("Constant term (c): " + (long)c);
    }

    // Function to calculate constant term (f(0))
    public static double calculateConstant(double[][] points, int k) {
        double constant = 0;

        for (int i = 0; i < k; i++) {
            double term = points[i][1];
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0 - points[j][0]) / (points[i][0] - points[j][0]);
                }
            }
            constant += term;
        }

        return constant;
    }
}