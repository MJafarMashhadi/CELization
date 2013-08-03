public class PerlinNoiseGenerator
{
	public double persistence;
	public double frequency;
	public double amplitude;
    public int octaves;
    public int randomseed;
    
    public PerlinNoiseGenerator()
    {
		persistence = 0;
		frequency = 0;
		amplitude = 0;
		octaves = 0;
		randomseed = 0;		
	}
	
    public PerlinNoiseGenerator(double persistence, double frequency, double amplitude, int octaves, int randomseed)
    {
		this.persistence = persistence;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.octaves = octaves;
		this.randomseed = 2 + randomseed*randomseed;
	}
	
	public double get(double x, double y)
	{
		return amplitude * Total(x, y);
	}

    private double Total(double i, double j)
    {
		double t = 0.0f;
		double _amplitude = 1;
		double freq = frequency;

		for(int k = 0; k < octaves; k++)
		{
			t += GetValue(j*freq + randomseed, i*freq + randomseed) * _amplitude;
			_amplitude *= persistence;
			freq *= 2;
		}

		return t;
	}

    private double GetValue(double x, double y)
    {
		int Xint = (int) x;
		int Yint = (int) y;

		double Xfrac = x - Xint;
		double Yfrac = y - Yint;

		double n01 = Noise(Xint - 1, Yint - 1);
		double n02 = Noise(Xint + 1, Yint - 1);
		double n03 = Noise(Xint - 1, Yint + 1);
		double n04 = Noise(Xint + 1, Yint + 1);
		double n05 = Noise(Xint - 1, Yint    );
		double n06 = Noise(Xint + 1, Yint    );
		double n07 = Noise(Xint    , Yint - 1);
		double n08 = Noise(Xint    , Yint + 1);
		double n09 = Noise(Xint    , Yint    );
		double n12 = Noise(Xint + 2, Yint - 1);
		double n14 = Noise(Xint + 2, Yint + 1);
		double n16 = Noise(Xint + 2, Yint    );
		double n23 = Noise(Xint - 1, Yint + 2);
		double n24 = Noise(Xint + 1, Yint + 2);
		double n28 = Noise(Xint    , Yint + 2);
		double n34 = Noise(Xint + 2, Yint + 2);

		double x0y0 = 
					  0.0625 * (n01 + n02 + n03 + n04) + 
					  0.1250 * (n05 + n06 + n07 + n08) + 
					  0.2500 * n09;

		double x1y0 = 
					  0.0625 * (n07 + n12 + n08 + n14) + 
					  0.1250 * (n09 + n16 + n02 + n04) + 
					  0.2500 * n06;

		double x0y1 = 
					  0.0625 * (n05 + n06 + n23 + n24) + 
					  0.1250 * (n03 + n04 + n09 + n28) + 
					  0.2500 * n08;

		double x1y1 = 
					  0.0625 * (n09 + n16 + n28 + n34) + 
					  0.1250 * (n08 + n14 + n06 + n24) + 
					  0.2500 * n04;

		double v1 = Interpolate(x0y0, x1y0, Xfrac);
		double v2 = Interpolate(x0y1, x1y1, Xfrac);

		double fin = Interpolate(v1, v2, Yfrac);

		return fin;
	}
	
    private double Interpolate(double x, double y, double a) 
    {
		double negA = 1.0 - a;
		double negASqr = negA * negA;
		double fac1 = 3.0 * (negASqr) - 2.0 * (negASqr * negA);
		double aSqr = a * a;
		double fac2 = 3.0 * aSqr - 2.0 * (aSqr * a);

		return x * fac1 + y * fac2;
	}

    private double Noise(int x, int y)
   	{
		int n = x + y * 57;
		n = (n << 13) ^ n;
		int t = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff;
		return 1.0 - (double)t * 0.931322574615478515625e-9;
	}

}
