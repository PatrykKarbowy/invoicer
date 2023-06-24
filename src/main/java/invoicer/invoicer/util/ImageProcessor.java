package invoicer.invoicer.util;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.*;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


public class ImageProcessor {
    private static IplImage convertToIplImageFromMat(Mat matImage) {
        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        return iplConverter.convertToIplImage(iplConverter.convert(matImage));
    }
    public static IplImage convertToIplImage(BufferedImage bufferedImage) {
        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        return iplConverter.convert((java2DFrameConverter.convert(bufferedImage)));
    }
    private static BufferedImage convertToBufferedImage(IplImage iplImage) {
        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        Frame frame = iplConverter.convert(iplImage);
        return java2DFrameConverter.getBufferedImage(frame);
    }
    private static IplImage downScaleImage(IplImage srcImage, int percent){
        IplImage destImage = cvCreateImage(cvSize((srcImage.width() * percent / 100), (srcImage.height() * percent / 100)),
                srcImage.depth(), srcImage.nChannels());
        cvResize(srcImage, destImage);
        return destImage;
    }
    private static IplImage cannyEdgeDetection(IplImage srcImage, int percent) {
        IplImage destImage = downScaleImage(srcImage, percent);
        cvSmooth(destImage, destImage, CV_BLUR,3,0,0,0);
        Mat mat = cvarrToMat(destImage);
        Mat mat_f = cvarrToMat(destImage);
        //Edge Detection
        cvtColor(mat, mat_f, Imgproc.COLOR_RGB2BGR);
        cvtColor(mat_f, mat_f, Imgproc.COLOR_RGB2GRAY);
        GaussianBlur(mat_f,mat_f,new Size(15,15), 0.0,0.0,BORDER_DEFAULT);
        imwrite("C:\\Users\\Togo\\Desktop\\Coding\\blur.jpg", mat_f);
        Canny(mat_f, mat_f, 300, 600, 5, true);
        dilate(mat_f,mat_f,new Mat());
        IplImage cannyImage = convertToIplImageFromMat(mat_f);
        Mat matFinal = cvarrToMat(cannyImage);
        imwrite("C:\\Users\\Togo\\Desktop\\Coding\\canny.jpg", matFinal);
        return cannyImage;
    }
    private static ArrayList<Point> findContour(IplImage cannyImage) {
        IplImage foundedContoursImage = cvCloneImage(cannyImage);
        CvMemStorage memory = CvMemStorage.create();
        CvSeq contours = new CvSeq();
        cvFindContours(foundedContoursImage, memory, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
        int maxWidth = 0;
        int maxHeight = 0;
        CvRect contour = null;
        CvSeq seqFounded = null;
        CvSeq nextSeg = new CvSeq();
        for (nextSeg = contours; nextSeg != null; nextSeg = nextSeg.h_next()) {
            contour = cvBoundingRect(nextSeg, 0);
            if ((contour.width() >= maxWidth) && (contour.height() >= maxHeight)) {
                maxWidth = contour.width();
                maxHeight = contour.height();
                seqFounded = nextSeg;
            }
        }
        CvSeq result = cvApproxPoly(seqFounded, Loader.sizeof(CvContour.class), memory, CV_POLY_APPROX_DP, cvContourPerimeter(seqFounded) * 0.02, 0);
        ArrayList<Point> points = optimumResult(result);
        for (Point point : points){
            cvDrawCircle(foundedContoursImage, point.asBuffer(), 5, CvScalar.BLUE, 20, 8, 0);
        }
        Mat matFinal = cvarrToMat(foundedContoursImage);
        imwrite("C:\\Users\\Togo\\Desktop\\Coding\\contour.jpg", matFinal);
        return points;
    }
    private static ArrayList<Point> optimumResult(CvSeq result) {
        ArrayList<Integer> yPointsValues = new ArrayList<>();
        ArrayList<Integer> yTopPoints = new ArrayList<>();
        ArrayList<Integer> yBottomPoints = new ArrayList<>();
        ArrayList<Point> topPoints = new ArrayList<>();
        ArrayList<Point> bottomPoints = new ArrayList<>();
        ArrayList<Point> pointsOptimum = new ArrayList<>();

        for (int i = 0; i < result.total(); i++) {
            CvPoint v = new CvPoint(cvGetSeqElem(result, i));
            yPointsValues.add(v.y());
        }
        Collections.sort(yPointsValues);

        for (int i = 0; i < result.total(); i++) {
            CvPoint v = new CvPoint(cvGetSeqElem(result, i));
            if ((v.y() == yPointsValues.get(0)) || (v.y() == yPointsValues.get(1))) {
                yTopPoints.add(v.y());
            }
        }

        for (int i = 0; i < result.total(); i++) {
            CvPoint v = new CvPoint(cvGetSeqElem(result, i));
            if ((v.y() == yPointsValues.get(yPointsValues.size() - 1)) || (v.y() == yPointsValues.get(yPointsValues.size() - 2))) {
                yBottomPoints.add(v.y());
            }
        }

        Collections.sort(yTopPoints);
        System.out.println(yTopPoints);
        Collections.sort(yBottomPoints);

        for (int i = 0; i < result.total(); i++) {
            CvPoint v = new CvPoint(cvGetSeqElem(result, i));
            for (int pointY : yTopPoints) {
                if (pointY == v.y()) {
                    topPoints.add(new Point(v.x(), pointY));
                }
            }
            for (int pointYBot : yBottomPoints) {
                if (pointYBot == v.y()) {
                    bottomPoints.add(new Point(v.x(), pointYBot));
                }
            }
        }
        if (topPoints.get(0).x() > topPoints.get(1).x()){
            Collections.swap(topPoints,0,1);
        }
        if (bottomPoints.get(0).x() > bottomPoints.get(1).x()){
            Collections.swap(bottomPoints,0,1);
        }
        pointsOptimum.add(topPoints.get(0));
        pointsOptimum.add(topPoints.get(1));
        pointsOptimum.add(bottomPoints.get(0));
        pointsOptimum.add(bottomPoints.get(1));

        return pointsOptimum;
    }
    private static IplImage cropImage(IplImage srcImage, int fromX, int fromY, int toWidth, int toHeight){
        cvSetImageROI(srcImage, cvRect(fromX, fromY, toWidth, toHeight));
        IplImage destImage = cvCloneImage(srcImage);
        cvCopy(srcImage, destImage);
        return destImage;
    }
    private static IplImage cutAndTransformatedImage(IplImage srcImage, ArrayList<Point> contour, int percent){
        IplImage warpImage = cvCloneImage(srcImage);

        for (int i = 0; i < contour.size(); i++){
            Point resizedPoint = new Point(((contour.get(i).x() * 100) / percent),((contour.get(i).y() * 100) / percent));
            contour.set(i,resizedPoint);
        }

        Point topLeftPoint = contour.get(0);
        Point topRightPoint = contour.get(1);
        Point bottomLeftPoint = contour.get(2);
        Point bottomRightPoint = contour.get(3);

        int resultWidth = (topRightPoint.x() - topLeftPoint.x());
        int bottomWidth = (bottomRightPoint.x() - bottomLeftPoint.x());
        if (resultWidth < bottomWidth){
            resultWidth = bottomWidth;
        }
        System.out.println(resultWidth);
        int resultHeight = (bottomLeftPoint.y() - topLeftPoint.y());
        int rightHeight = (bottomRightPoint.y() - topRightPoint.y());
        if (resultHeight > rightHeight){
            resultHeight = rightHeight;
        }
        System.out.println(resultHeight);

        float[] sourcePoints = { topLeftPoint.x(), topLeftPoint.y(), topRightPoint.x(), topRightPoint.y(), bottomLeftPoint.x(),
                bottomLeftPoint.y(), bottomRightPoint.x(), bottomRightPoint.y() };
        float [] destinationPoints = { 0, 0, resultWidth, 0, 0, resultHeight, resultWidth, resultHeight};
        CvMat homography = cvCreateMat(3, 3, CV_32FC1);
        cvGetPerspectiveTransform(sourcePoints, destinationPoints, homography);
        IplImage resultImage = cvCloneImage(warpImage);
        cvWarpPerspective(warpImage, resultImage, homography, CV_INTER_LINEAR, CvScalar.ZERO);
        Mat matFinal = cvarrToMat(resultImage);
        imwrite("C:\\Users\\Togo\\Desktop\\Coding\\warp.jpg", matFinal);
        return cropImage(resultImage, 0, 0, resultWidth, resultHeight);
    }
    private static IplImage transformedImage (BufferedImage bufferedImage, int percent){
        IplImage srcImage = convertToIplImage(bufferedImage);
        IplImage cannyImage = cannyEdgeDetection(srcImage,percent);
        ArrayList<Point> contour = findContour(cannyImage);
        IplImage cutAndTransformedImage = cutAndTransformatedImage(srcImage, contour, percent);
        return cutAndTransformedImage;
    }
    public static BufferedImage processForOCR(BufferedImage srcImage, int percent){
        IplImage transformedImage = transformedImage(srcImage, percent);
        IplImage destImage = cvCreateImage(cvGetSize(transformedImage), IPL_DEPTH_8U, 1);
        cvCvtColor(transformedImage, destImage, CV_BGR2GRAY);
        cvSmooth(destImage, destImage, CV_MEDIAN,3,0,0,0);
        cvThreshold(destImage,destImage,0,255,CV_THRESH_OTSU);
        Mat matFinal = cvarrToMat(destImage);
        imwrite("C:\\Users\\Togo\\Desktop\\Coding\\wynik.jpg", matFinal);
        BufferedImage ocrReady = convertToBufferedImage(destImage);
        return ocrReady;
    }
}

