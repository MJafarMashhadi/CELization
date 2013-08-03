#include <cstdio>
#include <cstdlib>
#include <cstring>

typedef char  BYTE;  // 1 byte
typedef short WORD;  // 2 bytes
typedef int DWORD; // 4 bytes
typedef long long QWORD; // 8 bytes

/*** RGB Structure ***/
typedef struct
{
    BYTE rgbBlue;     /* Blue value */
    BYTE rgbGreen;    /* Green value */
    BYTE rgbRed;      /* Red value */
} RGBPIXEL;

/** Bitmap filetype structures  */
#pragma pack(2)
typedef struct              /**** BMP file header structure ****/
{
    WORD  bfType;           /* Magic number for file */ // 2 bytes
    DWORD bfSize;           /* Size of file */          // 4 bytes
    WORD  bfReserved1;      /* Reserved */              // 2 bytes
    WORD  bfReserved2;      /* Reserved */              // 2 bytes
    DWORD bfOffBits;        /* Offset to bitmap data */ // 4 bytes
} BITMAPFILEHEADER;
#pragma pack()

#define BF_TYPE 0x4D42        /* "BM" */

typedef struct                /**** BMP file info structure ****/
{
    DWORD   bfIHSize;         /* Size of info header  */       // 4 bytes (value = 40 Bytes)
    DWORD   biWidth;          /* Width of image */             // 4 bytes signed int
    DWORD   biHeight;         /* Height of image */            // 4 bytes ...
    WORD    biPlanes;         /* Number of color planes */     // 2 bytes
    WORD    biBitCount;       /* Number of bits per pixel */   // 2 bytes ( color depth )
    DWORD   biCompression;    /* Type of compression to use */ // 4 bytes compression method -> _CompressionMethod
    DWORD   biSizeImage;      /* Size of image data */         // 4 bytes the size of the raw bitmap data (see below), and should not be confused with the file size
    DWORD   biXPelsPerMeter;  /* X pixels per meter */         // 4 bytes the horizontal resolution of the image. (pixel per meter, signed integer)
    DWORD   biYPelsPerMeter;  /* Y pixels per meter */         // 4 bytes the vertical resolution of the image. (pixel per meter, signed integer)
    DWORD   biClrUsed;        /* Number of colors used */      // 4 bytes the number of colors in the color palette, or 0 to default to 2^n.
    DWORD   biClrImportant;   /* Number of important colors */ // 4 bytes the number of important colors used, or 0 when every color is important; generally ignored.
} BITMAPINFOHEADER;

#define HEADER_SIZE sizeof(BITMAPFILEHEADER)
#define INFO_SIZE sizeof(BITMAPINFOHEADER)

class BMPImage
{
public:
    /** Bitmap Header structure **/
    BITMAPFILEHEADER header;
    /** File Header structure **/
    BITMAPINFOHEADER info;
    /** Default destructor */
    virtual ~BMPImage();
    /** ofsset related variables **/
    unsigned int voidSpaceLength;
    unsigned int lineLength;

    /** Output File Pointer **/
    FILE* outputFile;
    /** Create Picture **/
    bool CreatePicture(const char*, BITMAPFILEHEADER*, BITMAPINFOHEADER*);
    /** Draw blue rectangles around faces **/
    void WriteImageData();
    /** Picture pixels Matrix **/
    RGBPIXEL** pixelsMatrix;
    /** working file address **/
    char* fileAddress;
private:
    /** BMP file load subroutines **/
    bool LoadPrechecks();  // Existance, readability , ...
    bool OpenStream();     // Open file / read-binary
    bool LoadHeader();     // Read headers from file
};

