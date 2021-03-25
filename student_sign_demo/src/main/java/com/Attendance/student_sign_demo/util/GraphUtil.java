package com.Attendance.student_sign_demo.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GraphUtil {

    /**
     2      * 缩放图片(压缩图片质量，改变图片尺寸)
     3      * 若原图宽度小于新宽度，则宽度不变！
     4      * @param newWidth 新的宽度
     5      * @param quality 图片质量参数 0.7f 相当于70%质量
     6      */
//    public static void imageResize(File originalFile, File resizedFile,
//        int maxWidth, int maxHeight, float quality) throws IOException {
//        9
//        10         if (quality > 1) {
//            11             throw new IllegalArgumentException(
//                    12                     "图片质量需设置在0.1-1范围");
//            13         }
//        14
//        15         ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        16         Image i = ii.getImage();
//        17         Image resizedImage = null;
//        18
//        19         int iWidth = i.getWidth(null);
//        20         int iHeight = i.getHeight(null);
//        21
//        22         int newWidth = maxWidth;
//        23         if(iWidth < maxWidth){
//            24             newWidth = iWidth;
//            25         }
//        26
//        27
//        28         if (iWidth >= iHeight) {
//            29             resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
//                    30                     / iWidth, Image.SCALE_SMOOTH);
//            31         }
//        32
//        33
//        34
//        35         int newHeight = maxHeight;
//        36         if(iHeight < maxHeight){
//            37             newHeight = iHeight;
//            38         }
//        39
//        40         if(resizedImage==null && iHeight >= iWidth){
//            41             resizedImage = i.getScaledInstance((newHeight * iWidth) / iHeight,
//                    42                     newHeight, Image.SCALE_SMOOTH);
//            43         }
//        44
//        45         // This code ensures that all the pixels in the image are loaded.
//        46         Image temp = new ImageIcon(resizedImage).getImage();
//        47
//        48         // Create the buffered image.
//        49         BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
//                50                 temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//        51
//        52         // Copy image to buffered image.
//        53         Graphics g = bufferedImage.createGraphics();
//        54
//        55         // Clear background and paint the image.
//        56         g.setColor(Color.white);
//        57         g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        58         g.drawImage(temp, 0, 0, null);
//        59         g.dispose();
//        60
//        61         // Soften.
//        62         float softenFactor = 0.05f;
//        63         float[] softenArray = { 0, softenFactor, 0, softenFactor,
//                64                 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
//        65         Kernel kernel = new Kernel(3, 3, softenArray);
//        66         ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        67         bufferedImage = cOp.filter(bufferedImage, null);
//        68
//        69         // Write the jpeg to a file.
//        70         FileOutputStream out = new FileOutputStream(resizedFile);
//        71
//        72         // Encodes image as a JPEG data stream
//        73         JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        74
//        75         JPEGEncodeParam param = encoder
//        76                 .getDefaultJPEGEncodeParam(bufferedImage);
//        77
//        78         param.setQuality(quality, true);
//        79
//        80         encoder.setJPEGEncodeParam(param);
//        81         encoder.encode(bufferedImage);
//        82     } // Example usage

}
