import com.allaire.cfx.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sf.jmimemagic.*;

import imageUtil.*;

public class Image implements CustomTag {
	
	public void processRequest( Request request, Response response ) throws Exception {
		String action = request.getAttribute("action", "").toUpperCase();
		String filePath = request.getAttribute("file");
		
		// JPEGResize uses a different argument name.
		if (filePath == null)
			filePath = request.getAttribute("source");
		
		// Check again if a filePath was found.
		if (filePath == null)
			throw new IllegalArgumentException("Required arguments 'file' or 'source' not specified.");
		
		imageUtil.Image img = fileSecurityChecks(filePath);
		
		if (action.equals("RESIZE"))
			resizeAction(request, img);
		else if (action.equals("READ"))
			readAction(response, filePath, img);
		else 
			throw new IllegalArgumentException("Invalid action specified. Action given was " + action + ".");
	}
	
	private void resizeAction( Request request, imageUtil.Image img ) throws Exception {
		String outputPath = getOutputPath(request);
		String quality = request.getAttribute("quality", "80");
		int width = request.getIntAttribute("x", 0);
		int height = request.getIntAttribute("y", 0);
		String thumbnail = request.getAttribute("thumbnail", "No").toUpperCase();
		
		// Couple more jpegresize translators.
		if (width <= 0 && height <= 0) {
			width = request.getIntAttribute("width", 0);
			height = request.getIntAttribute("height", 0);
		}
				
		imageUtil.Image resized = null;
		if (width > 0) {
			resized = img.getResizedToWidth(width);
		} else if (height > 0) {
			resized = img.getResizedToHeight(height);
		} else if (thumbnail.equals("YES")) {
			int squareSize = 100;
			if (width > 0) {
				squareSize = width;
			} else if (height > 0) {
				squareSize = height;
			}
			
			resized = img.getResizedToSquare(squareSize, 0);
		} else
			throw new IllegalArgumentException("Specify either a width, height, or thumbnail size.");
		
		try {
			File of = new File(outputPath);
			if (of.exists() && !of.canWrite())
				throw new IllegalArgumentException("File already exists and the path is not writable.");
				
			resized.writeToFile(new File(outputPath));
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Unable to write output file.");
		}
	}
	
	private void readAction( Response response, String filePath, imageUtil.Image img ) throws Exception {
		File imgFile;
		imgFile = new File(filePath);
		
		String name = imgFile.getName();
		int pos = name.lastIndexOf('.');
		String ext = name.substring(pos+1);
		
		response.setVariable("IMG_FILE", imgFile.getPath());
		response.setVariable("IMG_TYPE", ext);
		response.setVariable("IMG_CTIME", Long.toString(imgFile.lastModified()) );
		response.setVariable("IMG_ATIME", Long.toString(imgFile.lastModified()) );
		response.setVariable("IMG_MTIME", Long.toString(imgFile.lastModified()) );
		response.setVariable("IMG_SIZE", Long.toString(imgFile.getTotalSpace()) );
		response.setVariable("IMG_WIDTH", Integer.toString(img.getWidth()) );
		response.setVariable("IMG_HEIGHT", Integer.toString(img.getHeight()) );
		response.setVariable("IMG_COLORS", "0");
		response.setVariable("IMG_TRANSPARENT", "0");
		response.setVariable("IMG_INTERLACE", "FALSE");
		response.setVariable("IMG_COMMENT", "");
	}
	
	private static String getOutputPath( Request request ) throws Exception {
		String outputPath = request.getAttribute("output");
		
		// JPEGResize uses a different argument name.
		if (outputPath == null)
			outputPath = request.getAttribute("filename");

		// Be compatible with cfimage.
		if (outputPath == null)
			outputPath = request.getAttribute("destination");

		if (outputPath == null)
			throw new IllegalArgumentException("Required arugments 'output', 'filename', or 'destination' not found.");

		return outputPath;
	}
	
	private static imageUtil.Image fileSecurityChecks( String filePath ) throws Exception {
		imageUtil.Image img = null;
		File f = null;
		
		try {
			f = new File(filePath);
			if (!f.exists() || !f.isFile())
				throw new IllegalArgumentException("Invalid path to image.");
			
			MagicMatch imgMime = Magic.getMagicMatch(f, false, true);
			if (!imgMime.getMimeType().startsWith("image/")) {
				f.delete();
				throw new IllegalArgumentException("Invalid image type.");
			}
			
			img = ImageLoader.fromFile(f);
			if (img.getSourceType() == ImageType.UNKNOWN) {
				try {
					f.delete();
				} catch (SecurityException se) {
					throw new IllegalArgumentException("Error removing non-image file.");
				}
				throw new IllegalArgumentException("Invalid image type.");
			}
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Invalid path to image.");
		}
		
		return img;
	}
	
}