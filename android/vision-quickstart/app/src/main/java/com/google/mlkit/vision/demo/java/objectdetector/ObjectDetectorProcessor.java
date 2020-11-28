/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java.objectdetector;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.java.ProcessDirection;
import com.google.mlkit.vision.demo.java.VisionProcessorBase;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase;
import java.io.IOException;
import java.sql.Time;
import java.util.List;

/** A processor to run object detector. */
public class ObjectDetectorProcessor extends VisionProcessorBase<List<DetectedObject>> {

  private static final String TAG = "ObjectDetectorProcessor";
  private Time currentDate;
  private ProcessDirection pd;

  private final ObjectDetector detector;
  private final TextToSpeech tts;
  private final Display display;

  public ObjectDetectorProcessor(Context context, ObjectDetectorOptionsBase options, @Nullable TextToSpeech tts,
                                 @Nullable Display display) {

    super(context);
    this.display = display;
    this.tts = tts;
    this.pd = new ProcessDirection(tts);
    currentDate = new Time(0);
    currentDate.setTime(System.currentTimeMillis());
    detector = ObjectDetection.getClient(options);
  }

  @Override
  public void stop() {
    super.stop();
    try {
      detector.close();
    } catch (IOException e) {
      Log.e(TAG, "Exception thrown while trying to close object detector!", e);
    }
  }

  @Override
  protected Task<List<DetectedObject>> detectInImage(InputImage image) {
    return detector.process(image);
  }

  @Override
  protected void onSuccess(
      @NonNull List<DetectedObject> results, @NonNull GraphicOverlay graphicOverlay) {
    for (DetectedObject object : results) {
      graphicOverlay.add(new ObjectGraphic(graphicOverlay, object));

      pd.process(object.getBoundingBox(), display);
    }
  }


  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.e(TAG, "Object detection failed!", e);
  }

  public void say(String instruction) {
    tts.speak(instruction, TextToSpeech.QUEUE_ADD, null);
  }
}
