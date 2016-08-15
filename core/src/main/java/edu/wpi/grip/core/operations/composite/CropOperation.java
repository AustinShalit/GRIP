package edu.wpi.grip.core.operations.composite;

import edu.wpi.grip.core.Operation;
import edu.wpi.grip.core.OperationDescription;
import edu.wpi.grip.core.sockets.InputSocket;
import edu.wpi.grip.core.sockets.OutputSocket;
import edu.wpi.grip.core.sockets.SocketHint;
import edu.wpi.grip.core.sockets.SocketHints;
import edu.wpi.grip.core.util.Icon;

import com.google.common.collect.ImmutableList;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;

import java.util.List;

/**
 * An {@link Operation} that crops an image using two points.
 */
public class CropOperation implements Operation {

  /**
   * Describes this operation. This is used by the 'Operations' class to add operations to GRIP.
   */
  public static final OperationDescription DESCRIPTION =
      OperationDescription.builder()
          .name("Crop")
          .summary("Crop an image")
          .category(OperationDescription.Category.IMAGE_PROCESSING)
          .icon(Icon.iconStream("crop"))
          .build();

  private final SocketHint<Mat> inputHint = SocketHints.Inputs.createMatSocketHint("Input", false);
  private final SocketHint<Point> point1Hint
      = SocketHints.Inputs.createPointSocketHint("Point 1", false);
  private final SocketHint<Point> point2Hint
      = SocketHints.Inputs.createPointSocketHint("Point 2", false);

  private final SocketHint<Mat> outputHint = SocketHints.Inputs.createMatSocketHint("Output", true);

  private final InputSocket<Mat> inputSocket;
  private final InputSocket<Point> point1;
  private final InputSocket<Point> point2;

  private final OutputSocket<Mat> outputSocket;

  @SuppressWarnings("JavadocMethod")
  public CropOperation(InputSocket.Factory inputSocketFactory, OutputSocket.Factory
      outputSocketFactory) {
    this.inputSocket = inputSocketFactory.create(inputHint);
    this.point1 = inputSocketFactory.create(point1Hint);
    this.point2 = inputSocketFactory.create(point2Hint);

    this.outputSocket = outputSocketFactory.create(outputHint);
  }
  @Override
  public List<InputSocket> getInputSockets() {
    return ImmutableList.of(
        inputSocket,
        point1,
        point2
    );
  }

  @Override
  public List<OutputSocket> getOutputSockets() {
    return ImmutableList.of(
        outputSocket
    );
  }

  @Override
  public void perform() {
    final Rect rect = new Rect(point1.getValue().get(), point2.getValue().get());
    final Mat tmp = new Mat(inputSocket.getValue().get(), rect);
    tmp.copyTo(outputSocket.getValue().get());
  }
}
