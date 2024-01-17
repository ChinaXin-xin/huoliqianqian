package xin.h5.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGeneratorUtils {

    /**
     * @param url
     * @param filePath
     * @param isWhiteEdge
     * @throws WriterException
     * @throws IOException     是否要白边，true是要，false是不要
     */
    public static void generateQRCodeImage(String url, String filePath, Boolean isWhiteEdge)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 设置边距为0以去除白边
        if (isWhiteEdge)
            hints.put(EncodeHintType.MARGIN, 1);
        else
            hints.put(EncodeHintType.MARGIN, 0);

        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 350, 350, hints);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

/*    public static void main(String[] args) {
        try {
            generateQRCodeImage("https://www.baidu.com", "MyQRCode.png", true);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }*/
}

