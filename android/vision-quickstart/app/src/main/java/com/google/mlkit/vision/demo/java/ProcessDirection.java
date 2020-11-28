package com.google.mlkit.vision.demo.java;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.util.Size;
import android.view.Display;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class ProcessDirection {
    // private final double deviationFromCenter = 0.002;
    private final double closeToObject = 0.7;  // object takes x% of screen
    private final TextToSpeech tts;

    public ProcessDirection(TextToSpeech tts) {
        this.tts = tts;
    }
    /* suppose the axis is centered in the middle of the phone camera,
     * could do another function before to move the points
     */
    public void process(Rect rect, Display display){
         Point size = new Point();
         display.getSize(size);
         int dimensionX = size.x;
         int dimensionY = size.y;
         double cameraArea = dimensionX * dimensionY;
         double rectArea = (double)(rect.width() * rect.height());
         double ratio = (rectArea / cameraArea);
         // rectangle will "touch" the symmetry axis
         double deviationFromCenter = rect.width() / 2.0;

         int movedAxisX = dimensionX / 2;

         // center of gravity of rectangle
        PointF center = new PointF(rect.exactCenterX(), rect.exactCenterY());

        if (ratio > closeToObject) {
             say("You are close enough to hit the button.");
         } else if(abs(movedAxisX - center.x) < deviationFromCenter) {
             say("Step forwards.");
         } else if(center.x < movedAxisX) {
             say("Step forwards on the left.");
        } else {
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
