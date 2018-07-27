package org.sdrc.scsl.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

public class ImageEncoder {

	/** 
	 * @author Sarita Panigrahi, created on: 23-Oct-2017
	 * @param path
	 * @param areaName
	 * @return
	 * areaName is added only for direct jpg download
	 */
	
	private SimpleDateFormat timeFormatter = new SimpleDateFormat(ResourceBundle.getBundle("messages/messages").getString(Constants.Web.YEAR_MONTH_DATE_TIME_FORMATTER_NO_HYPHEN));
	
	public String createImgFromFile(String path, String areaName) {
		// Create a JPEG transcoder
		String filePath = ResourceBundle.getBundle("spring/app").getString("filepath.pdsa")+"//scsl chart//";
		
		File filePathDirect = new File(filePath);
		if (!filePathDirect.exists())
			filePathDirect.mkdir();
		
		String date = timeFormatter.format(new Date());
		String fileName = "";
		
		if(null!=areaName){
			fileName = filePath+ areaName+"_" + date + ".jpg";
		}else
			fileName = filePath+ "CHART_" + date + ".jpg";
		try {
			JPEGTranscoder t = new JPEGTranscoder();

			// Set the transcoding hints.
			t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));

			// Create the transcoder input.
			String svgURI = new File(path).toURI().toURL().toString();

			TranscoderInput input = new TranscoderInput(svgURI);

			OutputStream ostream = new FileOutputStream(fileName);
			TranscoderOutput output = new TranscoderOutput(ostream);

			// Save the image.

			t.transcode(input, output);
			ostream.flush();
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Flush and close the stream.

		return fileName;
	}

}
