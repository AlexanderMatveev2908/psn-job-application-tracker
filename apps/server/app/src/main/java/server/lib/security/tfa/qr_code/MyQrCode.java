package server.lib.security.tfa.qr_code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import server.decorators.flow.ErrAPI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class MyQrCode {

  private static final int SIZE = 300;
  private static final Color TWD_BLUE_600 = new Color(37, 99, 235);
  private static final Color WHITE = Color.WHITE;

  private BitMatrix getMatrix(String data) {
    try {
      QRCodeWriter writer = new QRCodeWriter();

      Map<EncodeHintType, Object> hints = Map.of(EncodeHintType.MARGIN, 2);

      BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 0, 0, hints);

      return matrix;
    } catch (Exception err) {
      throw new ErrAPI("err defining matrix");
    }
  }

  private record CellCtx(int cells, int cellSize, int realSize) {
  }

  private CellCtx genCellCtx(BitMatrix matrix) {
    int cells = matrix.getWidth();
    int cellSize = Math.max(1, SIZE / cells);
    int realSize = cells * cellSize;

    return new CellCtx(cells, cellSize, realSize);
  }

  private void defineDots(Graphics2D g, BitMatrix matrix, CellCtx rec) {
    g.setColor(TWD_BLUE_600);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int y = 0; y < rec.cells(); y++)
      for (int x = 0; x < rec.cells(); x++)
        if (matrix.get(x, y)) {
          int px = x * rec.cellSize();
          int py = y * rec.cellSize();

          g.fillRect(px, py, rec.cellSize(), rec.cellSize());
        }
  }

  public Mono<byte[]> genQrBinary(String data) {
    return Mono.fromCallable(() -> {
      try {
        var matrix = getMatrix(data);
        var cellCtx = genCellCtx(matrix);

        BufferedImage img = new BufferedImage(cellCtx.realSize(), cellCtx.realSize(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(WHITE);
        g.fillRect(0, 0, cellCtx.realSize(), cellCtx.realSize());

        defineDots(g, matrix, cellCtx);

        g.dispose();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
          ImageIO.write(img, "png", bos);
          return bos.toByteArray();
        }
      } catch (IOException e) {
        throw new ErrAPI("err generating rounded qr bytes");
      }
    }).subscribeOn(Schedulers.boundedElastic());
  }
}
