package com.google.mlkit.vision.demo.java;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Display;
import android.view.WindowMetrics;

import androidx.annotation.RequiresApi;

import com.google.mlkit.vision.demo.GraphicOverlay;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ProcessDirection {
    // private final double deviationFromCenter = 0.002;
    private final double closeToObject = 0.4;  // object takes x% of screen
    private final TextToSpeech tts;

    public ProcessDirection(TextToSpeech tts) {
        this.tts = tts;
    }
    /* suppose the axis is centered in the middle of the phone camera,
     * could do another function before to move the points
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void process(Rect rect, GraphicOverlay g){
         int dimensionX = g.getImageWidth();
         int dimensionY = g.getImageHeight();
         double cameraArea = dimensionX * dimensionY;
         double rectArea = (rect.width() * rect.height());
         double ratio = (rectArea / cameraArea);
         // rectangle will "touch" the symmetry axis
         double deviationFromCenter = rect.width() / 2.0;

         double movedAxisX = dimensionX / 2.0;

         // center of gravity of rectangle
        float centerX = rect.exactCenterX();

//        System.out.println("dimensionX screen = " + dimensionX);
//        System.out.println("dimensionY screen = " + dimensionY);
//        System.out.println("rect width = " + rect.width());
//        System.out.println("rect height = " + rect.height());
//
//        System.out.println(" movedAxisX = " + movedAxisX);
//        System.out.println(" centerX of rectangle = " + centerX);
//        System.out.println(" ---------------------------------");
//        System.out.println();

        if (ratio > closeToObject) {
            System.out.println("ratio" + ratio);
             say("You are close enough to hit the button.");
         } else if(abs(movedAxisX - centerX) < deviationFromCenter) {
             say("Step forwards.");
         } else if(centerX < movedAxisX) {
             say("Step forwards on the left.");
        } else if (centerX >= movedAxisX){
            say("Step forwards on the right.");
        }

    }

    private double distance(PointF p, PointF q) {
        return sqrt(pow((p.x - q.x),2) + pow((p.y - q.y),2));
    }

    public void say(String instruction) {
        tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
    }

}


/*

    */
/* suppose the axis is centered in the middle of the phone camera,
     * could do another function before to move the points
     *//*

    public void processDirectionFromPoints(PointF topLeft, PointF topRight, PointF botLeft, PointF botRight,
                                           int dimensionX, int dimensionY) {

        // Step 1: see how far they are (size of square compared to the dimension of screen

        double cameraArea = dimensionX * dimensionY;
        double lengthSide1 = (distance(topLeft, topRight) + distance(botRight, botRight)) / 2;
        double lengthSide2 = (distance(botLeft, topLeft) + distance(botRight, topRight)) / 2;
        double squareArea = lengthSide1 * lengthSide2;
        double ratio = squareArea / cameraArea;

        float midDiag1 = (topLeft.x + botRight.x + topRight.x + botLeft.x) / 4;
        float midDiag2 = (topLeft.y + botRight.y + topRight.y + botLeft.y) / 4;
        PointF center = new PointF(midDiag1, midDiag2);

        if (ratio < closeToObject) {
            speaker.say("You are close enough to hit the button.");
        } else if(abs(center.x) < deviationFromCenter) {
            speaker.say("Step forwards.");
        } else if(center.x < 0) {
            speaker.say("Step forwards on the left.");
        } else {
            speaker.say("Step forwards on the right.");
        }

    }*/
