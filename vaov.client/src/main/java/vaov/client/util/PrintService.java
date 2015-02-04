package vaov.client.util;

import gnu.jpdf.BoundingBox;
import gnu.jpdf.PDFJob;
import gnu.jpdf.StringTooLongException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class PrintService {

	/**
	 * Prints the pdf that should be but into the ballot box.
	 *
	 * @param out
	 *            used to write the pdf-file
	 * @param hash
	 */
	public void print(OutputStream out, String hash) {
		PDFJob job = new PDFJob(out, "geheimes Akkreditierungsdokument");
		PageFormat pf = new PageFormat();
		Paper p = new Paper();
		double mm = 0.0393700787 * 72;
		p.setSize(210 * mm, 297 * mm); // fix the paper size, so that we always
		// know that we have sufficient space.
		p.setImageableArea(30 * mm, 30 * mm, (210 - 60) * mm, (297 - 60) * mm);
		pf.setPaper(p);
		pf.setOrientation(PageFormat.PORTRAIT);
		// Graphics g = job.getGraphics(pf);
		Graphics g = job.getGraphics();

		Dimension d = job.getPageDimension();

		Font title = g.getFont().deriveFont(Font.BOLD, 24); // title font
		Font text = g.getFont().deriveFont(Font.PLAIN, 12); // normal font
		Font id = g.getFont().deriveFont(Font.PLAIN, 10); // small font for Hash
		// string
		int padding = 5;

		System.out.println("total width: " + d.getWidth());
		System.out.println("total height: " + d.getHeight());

		g.setColor(Color.black);
		BoundingBox box = new BoundingBox(new Point(20, 20), new Dimension((int) d.getWidth() - 20, 30));
		/* g.drawRect((int)box.getAbsoluteLocation().getX(),
		 * (int)box.getAbsoluteLocation().getY(),
		 * (int)box.getSize().getWidth(),
		 * (int)box.getSize().getHeight()); */
		g.setFont(text);
		FontMetrics fm = g.getFontMetrics();

		BoundingBox child = null;
		try {
			child = box.getStringBounds("Bitte auf Papier Ausdrucken!", BoundingBox.HORIZ_ALIGN_LEFT,
			BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
			child = box.getStringBounds("Bitte auf Papier Ausdrucken!", BoundingBox.HORIZ_ALIGN_RIGHT,
			BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_RIGHT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		box = new BoundingBox(new Point(20, 50), new Dimension((int) d.getWidth() - 20, 150));
		/* g.drawRect((int)box.getAbsoluteLocation().getX(),
		 * (int)box.getAbsoluteLocation().getY(),
		 * (int)box.getSize().getWidth(),
		 * (int)box.getSize().getHeight()); */
		g.setFont(title);
		fm = g.getFontMetrics();

		try {
			child = box.getStringBounds("Geheimes Akkreditierungsdokument", BoundingBox.HORIZ_ALIGN_LEFT,
			BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		box.subtract(child, BoundingBox.SUBTRACT_FROM_BOTTOM);
		g.setFont(text);
		fm = g.getFontMetrics();
		try {
			child = box
			.getStringBounds(
			"Dieses PDF enthält das Erkennungsmerkmal (ID) deines Accounts. Um mit verifizierten Account (und somit stimmberechtigt) am Online-Beteiligungssystem teilnehmen zu können, musst du dieses Dokument ausdrucken und in die dafür vorgesehene Akkreditierungsurne werfen.",
			BoundingBox.HORIZ_ALIGN_LEFT, BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		box.subtract(child, BoundingBox.SUBTRACT_FROM_BOTTOM);
		try {
			child = box
			.getStringBounds(
			"Wenn du pseudonym an dem Online-Beteiligungstool teilnehmen möchtest, darfst du niemanden dieses Dokument zeigen. Es ist wie ein ausgefüllter, geheimer Stimmzettel.",
			BoundingBox.HORIZ_ALIGN_LEFT, BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		g.setFont(id);
		fm = g.getFontMetrics();
		box = new BoundingBox(new Point(20, 250), new Dimension((int) d.getWidth() - 20, 50));

		/* g.drawRect((int)box.getAbsoluteLocation().getX(),
		 * (int)box.getAbsoluteLocation().getY(),
		 * (int)box.getSize().getWidth(),
		 * (int)box.getSize().getHeight()); */
		try {
			child = box.getStringBounds("ID: " + hash, BoundingBox.HORIZ_ALIGN_LEFT, BoundingBox.VERT_ALIGN_TOP, fm,
			padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		QRCodeWriter qw = new QRCodeWriter();
		BitMatrix bm;
		try {
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // shouldn't
			// actually
			// matter since
			// only ascii
			// symbols
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // use
			// the
			// highest
			// possible
			bm = qw.encode(hash, BarcodeFormat.QR_CODE, (int) d.getWidth() - 100, (int) d.getWidth() - 100);

		} catch (WriterException e) {
			throw new RuntimeException(e);
		}

		BufferedImage buff = MatrixToImageWriter.toBufferedImage(bm);
		g.drawImage(buff, 50, 270, null);

		try {
			box = new BoundingBox(new Point(20, (int) d.getHeight() - 50), new Dimension((int) d.getWidth() - 20, 30));
			g.setFont(text);
			fm = g.getFontMetrics();
			child = box.getStringBounds("Bitte auf Papier Ausdrucken!", BoundingBox.HORIZ_ALIGN_LEFT,
			BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_LEFT);
			child = box.getStringBounds("Bitte auf Papier Ausdrucken!", BoundingBox.HORIZ_ALIGN_RIGHT,
			BoundingBox.VERT_ALIGN_TOP, fm, padding);
			child.drawWrappedString(g, fm, padding, BoundingBox.HORIZ_ALIGN_RIGHT);
		} catch (StringTooLongException stle) {
			throw new RuntimeException(stle);
		}

		// end the page
		g.dispose();
		job.end();
	}

}
