package com.google.sps.image;

import com.google.api.gax.rpc.HeaderProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.LocationInfo;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class LogoAnalyser implements Analyser {

  public static ImageAnnotatorSettings getSettings() throws IOException {
    HeaderProvider headerProvider =
            FixedHeaderProvider.create("X-Goog-User-Project","google.com:gpostcard");
    return ImageAnnotatorSettings.newBuilder().setHeaderProvider(headerProvider).build();
  }

  @Override
  public void analyse(String imageUrl) {
    try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(getSettings())) {
      // Builds the image annotation request
      List<AnnotateImageRequest> requests = new ArrayList<>();
      ImageSource imageSource = ImageSource.newBuilder().setImageUri("https://upload.wikimedia.org/wikipedia/commons/e/ee/Googleplex_Welcome_Sign.jpg").build();
      Image img = Image.newBuilder().setSource(imageSource).build();
      Feature label = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
      AnnotateImageRequest labelRequest =
          AnnotateImageRequest.newBuilder().addFeatures(label).setImage(img).build();
      requests.add(labelRequest);

      // Performs label detection on the image file
      BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          System.out.format("Error: %s%n", res.getError().getMessage());
          return;
        }

        // For full list of available annotations, see http://g.co/cloud/vision/docs
        for (EntityAnnotation annotation : res.getLogoAnnotationsList()) {
          System.out.println(annotation.getDescription());
        }
      }
    } catch(Exception ex) {
      System.out.println(ex);
    }
  }
}