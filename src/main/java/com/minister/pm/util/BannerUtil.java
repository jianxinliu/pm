package com.minister.pm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.minister.pm.log.Logger;

/**
 * 打印启动 banner
 * 
 * @author ljx
 * @Date Mar 31, 2019 1:12:42 AM
 *
 */
public class BannerUtil {

	private final static String defaultBanner = "/banner_alpha.txt";
	private static Logger logger = Logger.getLogger(BannerUtil.class);
	
	public static void printBanner() {
		String path = PMFileIOUtil.getPMFilePath(defaultBanner);
		try (FileInputStream finStream = new FileInputStream(new File(path));
				FileChannel channel = finStream.getChannel();) {
			ByteBuffer buffer = ByteBuffer.allocate(10);
			ByteBuffer[] buffers = new ByteBuffer[100];
			for (int i = 0; i < buffers.length; i++) {
				buffers[i] = ByteBuffer.allocate(10);
			}
			// single buffer read
			while (channel.read(buffer) != -1) {
				buffer.flip();
				while (buffer.hasRemaining())
					logger.plantInfo((char) buffer.get());
				buffer.clear();// clear the buffer and reuse it
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		printBanner();
	}
}
