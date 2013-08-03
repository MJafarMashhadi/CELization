/*******************************************************/
//                                                     //
//               Face Detection Program                //
// Fall 2013 / 1391 - Sharif University of Technology  //
// C++ programming project - course ID: 40153          //
//                                                     //
// @file: BMPImage.h                                   //
// @desc: Some required structures, macros and         //
//        definition of BMPImage class                 //
// @author: MohammadJafar MashhadiEbrahim              //
//                                                     //
/*******************************************************/

#include "BMPImage.h"

BMPImage::~BMPImage()
{
    for (int i = 0 ; i < info.biHeight ; i++)
        delete[] pixelsMatrix[i];

    delete[] pixelsMatrix;
    if (fileAddress != NULL)
        free(fileAddress);
}

bool BMPImage::CreatePicture(const char* address, BITMAPFILEHEADER* _header, BITMAPINFOHEADER* _info)
{
    fileAddress = strdup(address);
    //
    outputFile = fopen(fileAddress, "wb");
    if (outputFile == NULL)
        return false;
    //
    header = *_header;
    info = *_info;
    //
    fwrite(&header, HEADER_SIZE, 1, outputFile);
    fwrite(&info, INFO_SIZE, 1, outputFile);

    pixelsMatrix = new RGBPIXEL*[info.biHeight];
    for (int i = 0 ; i < info.biHeight ; i++ )
        pixelsMatrix[i] = new RGBPIXEL[info.biWidth];
    //
    lineLength = (info.biWidth * sizeof(RGBPIXEL));
    if (lineLength % 4 == 0)
        voidSpaceLength = 0;
    else
        voidSpaceLength = 4 - (lineLength % 4);
    //
    return true;
}

void BMPImage::WriteImageData()
{
    int row, rows = info.biHeight;
    int col, cols = info.biWidth;

    BYTE voidSpace = 0x00;
    for (row = rows-1 ; row >= 0 ; row--)
    {
        for (col = 0 ; col < cols ; col++)
            fwrite(&pixelsMatrix[row][col], sizeof(RGBPIXEL), 1, outputFile);

        fwrite(&voidSpace, sizeof(voidSpace), voidSpaceLength / sizeof(voidSpace), outputFile);
    }

    fflush(outputFile);
    fclose(outputFile);
}
