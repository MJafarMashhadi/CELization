#include <cstdio>
#include <cstdlib>
#include <cstring>
#include "BMPImage.h"
class PerlinNoise
{
public:
    PerlinNoise();
    PerlinNoise(double _persistence, double _frequency, double _amplitude, int _octaves, int _randomseed);


    double getHeight(double x, double y) const;

    double Persistence() const
    {
        return persistence;
    }
    double Frequency() const
    {
        return frequency;
    }
    double Amplitude() const
    {
        return amplitude;
    }
    double Octaves() const
    {
        return octaves;
    }
    double RandomSeed() const
    {
        return randomseed;
    }

    void Set(double _persistence, double _frequency, double _amplitude, int _octaves, int _randomseed);

    void SetPersistence(double _persistence)
    {
        persistence = _persistence;
    }
    void SetFrequency(double _frequency)
    {
        frequency = _frequency;
    }
    void SetAmplitude(double _amplitude)
    {
        amplitude = _amplitude;
    }
    void SetOctaves(double _octaves)
    {
        octaves = _octaves;
    }
    void SetRandomSeed(double _randomseed)
    {
        randomseed = _randomseed;
    }

private:

    double Total(double i, double j) const;
    double GetValue(double x, double y) const;
    double Interpolate(double x, double y, double a) const;
    double Noise(int x, int y) const;

    double persistence, frequency, amplitude;
    int octaves, randomseed;
};

PerlinNoise::PerlinNoise()
{
    persistence = 0;
    frequency = 0;
    amplitude = 0;
    octaves = 0;
    randomseed = 0;
}
PerlinNoise::PerlinNoise(double _persistence, double _frequency, double _amplitude, int _octaves, int _randomseed)
{
    Set(_persistence, _frequency, _amplitude, _octaves, _randomseed);
}

void PerlinNoise::Set(double _persistence, double _frequency, double _amplitude, int _octaves, int _randomseed)
{
    persistence = _persistence;
    frequency = _frequency;
    amplitude = _amplitude;
    octaves = _octaves;
    randomseed = 2 + _randomseed*_randomseed;
}


double PerlinNoise::getHeight(double x, double y) const
{
    return amplitude * Total(x, y);
}

double PerlinNoise::Total(double i, double j) const
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

double PerlinNoise::GetValue(double x, double y) const
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

    double x0y0 = \
                  0.0625 * (n01 + n02 + n03 + n04) + \
                  0.1250 * (n05 + n06 + n07 + n08) + \
                  0.2500 * n09;

    double x1y0 = \
                  0.0625 * (n07 + n12 + n08 + n14) + \
                  0.1250 * (n09 + n16 + n02 + n04) + \
                  0.2500 * n06;

    double x0y1 = \
                  0.0625 * (n05 + n06 + n23 + n24) + \
                  0.1250 * (n03 + n04 + n09 + n28) + \
                  0.2500 * n08;

    double x1y1 = \
                  0.0625 * (n09 + n16 + n28 + n34) + \
                  0.1250 * (n08 + n14 + n06 + n24) + \
                  0.2500 * n04;

    double v1 = Interpolate(x0y0, x1y0, Xfrac);
    double v2 = Interpolate(x0y1, x1y1, Xfrac);

    double fin = Interpolate(v1, v2, Yfrac);

    return fin;
}

double PerlinNoise::Interpolate(double x, double y, double a) const
{
    double negA = 1.0 - a;
    double negASqr = negA * negA;
    double fac1 = 3.0 * (negASqr) - 2.0 * (negASqr * negA);
    double aSqr = a * a;
    double fac2 = 3.0 * aSqr - 2.0 * (aSqr * a);

    return x * fac1 + y * fac2;
}

double PerlinNoise::Noise(int x, int y) const
{
    int n = x + y * 57;
    n = (n << 13) ^ n;
    int t = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff;
    return 1.0 - double(t) * 0.931322574615478515625e-9;
}

int main(int argc, char* argv[])
{
    if (argc != 4)
    {
        fprintf(stderr,"Wrong number of argumants\n");
        return 1;
    }

    int width = atoi(argv[1]);
    int height = atoi(argv[2]);
    int seed = atof(argv[3]);

    // persistence = 1.0
    // frequency = 0.1
    // amplitude = 2.0
    // octaves = 1.0
    // randomseed = 1.0
    PerlinNoise* p = new PerlinNoise(0.5, 0.3, 255, 1, seed);

    BMPImage* outputFile = new BMPImage();
    BITMAPFILEHEADER* header = new BITMAPFILEHEADER;
    BITMAPINFOHEADER* info = new BITMAPINFOHEADER;

    outputFile->lineLength = (width * sizeof(RGBPIXEL));
    if (outputFile->lineLength % 4 == 0)
        outputFile->voidSpaceLength = 0;
    else
        outputFile->voidSpaceLength = 4 - (outputFile->lineLength % 4);


    info->bfIHSize = sizeof(BITMAPINFOHEADER);
    info->biClrImportant = 0;
    info->biWidth = width;
    info->biHeight = height;
    info->biCompression = 0;
    info->biClrImportant = 0;
    info->biClrUsed = 0;
    info->biBitCount = 24;
    info->biSizeImage = sizeof(RGBPIXEL) * (width*height) + (height * outputFile->voidSpaceLength);
    info->biPlanes = 0;
    info->biXPelsPerMeter = 3779;
    info->biYPelsPerMeter = 3779;

    header->bfType = BF_TYPE;
    header->bfReserved1 = 0x00;
    header->bfReserved2 = 0x00;
    header->bfSize = info->bfIHSize + info->biSizeImage;
    header->bfOffBits = 54;

    outputFile->CreatePicture("/home/mjafar/Desktop/noise.bmp", header, info);

    for(int i=0 ; i < width ; i++)
    {
        for (int j=0 ; j < height ; j++)
        {
            outputFile->pixelsMatrix[i][j].rgbRed = (int)abs(p->getHeight(i, j));
            outputFile->pixelsMatrix[i][j].rgbGreen = outputFile->pixelsMatrix[i][j].rgbRed;
            outputFile->pixelsMatrix[i][j].rgbBlue = outputFile->pixelsMatrix[i][j].rgbRed;
        }
    }

    outputFile->WriteImageData();

    delete outputFile;
    delete header;
    delete info;

    return 0;
}
