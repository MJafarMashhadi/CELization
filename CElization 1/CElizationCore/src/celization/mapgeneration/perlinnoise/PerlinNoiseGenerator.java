package celization.mapgeneration.perlinnoise;

import java.io.Serializable;

public class PerlinNoiseGenerator implements Serializable {
    private static final long serialVersionUID = -7185609512998046490L;

    PerlinNoiseParameters parameters;

    public PerlinNoiseGenerator() {
    }

    public PerlinNoiseGenerator(PerlinNoiseParameters parameters) {
        this.parameters = parameters;
    }

    public void changeParameters(double persistence, double frequency, double amplitude, int octaves, int randomseed) {
        parameters.changeParameters(persistence, frequency, amplitude, octaves, randomseed);
    }

    public void changeParameters(PerlinNoiseParameters newParams) {
        parameters = newParams;
    }

    public double get(double x, double y) {
        return parameters.amplitude * getTotalNoise(x, y);
    }

    private double getTotalNoise(double i, double j) {
        double totalValue;
        double amplitudeStep;
        double freq;

        totalValue = 0.0f;
        amplitudeStep = 1;
        freq = parameters.frequency;
        for (int stepNumber = 0; amplitudeStep > 0 && stepNumber < parameters.octaves; stepNumber++) {
            // make little noises and add them together
            // sum ( f(noise) )
            totalValue += getSingleNoiseValue(j * freq + parameters.randomseed, i * freq + parameters.randomseed) * amplitudeStep;

            // change parameters of next noise
            amplitudeStep *= parameters.persistence;  // a = p ^ i
            freq *= 2;                                // f = 2 ^ i
        }

        return Math.abs(totalValue / 2);
    }

    private double getSingleNoiseValue(double x, double y) {
        long Xint = (long) Math.floor(x);
        long Yint = (long) Math.floor(y);

        double n01 = noise(Xint - 1L, Yint - 1L);
        double n02 = noise(Xint + 1L, Yint - 1L);
        double n03 = noise(Xint - 1L, Yint + 1L);
        double n04 = noise(Xint + 1L, Yint + 1L);
        double n05 = noise(Xint - 1L, Yint);
        double n06 = noise(Xint + 1L, Yint);
        double n07 = noise(Xint, Yint - 1L);
        double n08 = noise(Xint, Yint + 1L);
        double n09 = noise(Xint, Yint);
        double n12 = noise(Xint + 2L, Yint - 1L);
        double n14 = noise(Xint + 2L, Yint + 1L);
        double n16 = noise(Xint + 2L, Yint);
        double n23 = noise(Xint - 1L, Yint + 2L);
        double n24 = noise(Xint + 1L, Yint + 2L);
        double n28 = noise(Xint, Yint + 2L);
        double n34 = noise(Xint + 2L, Yint + 2L);

        double x0y0 = 0.0625 * (n01 + n02 + n03 + n04) + 0.1250 * (n05 + n06 + n07 + n08) + 0.2500 * n09;
        double x1y0 = 0.0625 * (n07 + n12 + n08 + n14) + 0.1250 * (n09 + n16 + n02 + n04) + 0.2500 * n06;
        double x0y1 = 0.0625 * (n05 + n06 + n23 + n24) + 0.1250 * (n03 + n04 + n09 + n28) + 0.2500 * n08;
        double x1y1 = 0.0625 * (n09 + n16 + n28 + n34) + 0.1250 * (n08 + n14 + n06 + n24) + 0.2500 * n04;

        return  interpolate(
                    interpolate(x0y0, x1y0, x - Xint),
                    interpolate(x0y1, x1y1, x - Xint),
                    y - Yint);
    }

    private double interpolate(double x, double y, double a) {
        double negA = 1.0 - a;
        double fac1 = negA * negA * (3.0 - 2.0 * negA);
        double fac2 = a    * a    * (3.0 - 2.0 * a   );

        return x * fac1 + y * fac2;
    }

    private double noise(long x, long y) {
        long n = x + y * 57;
        n = (n << 13) ^ n;
        long t = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff;
        return 1.0 - (double) t * 0.931322574615478515625e-9;
    }
}
