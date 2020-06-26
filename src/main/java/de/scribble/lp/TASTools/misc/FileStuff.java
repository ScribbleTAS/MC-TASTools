package de.scribble.lp.TASTools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.Files;

import de.scribble.lp.TASTools.CommonProxy;

public class FileStuff {
	
	public static void writeThings(StringBuilder output, File file, String logmessage){
		try {
			CommonProxy.logger.debug(logmessage);
			Files.write(output.toString().getBytes(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> readThings(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> out= new ArrayList<String>();
		String s;
		while ((s=reader.readLine()) != null) {
			out.add(s);
		}
		reader.close();
		return out;
	}
	
	public static List<String> readThingsFromURL(URL url, File file) throws IOException{
		ReadableByteChannel readableByteChannel;
		readableByteChannel = Channels.newChannel(url.openStream());
		FileOutputStream fileOutputStream;
		fileOutputStream = new FileOutputStream(file);
		FileChannel fileChannel = fileOutputStream.getChannel();
		fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		fileOutputStream.close();
		return readThings(file);
	}
	
	public static int countLines(File file)throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		return lines;
	}
}
