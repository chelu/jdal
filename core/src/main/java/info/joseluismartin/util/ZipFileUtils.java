/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.joseluismartin.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility library to manage Zip files.
 * 
 * @author Jose Luis Martin -(jlm@joseluismartin.info)
 */
public abstract class ZipFileUtils {
	/** commons log */
	private static Log log = LogFactory.getLog(ZipFileUtils.class);
	/** bufferr to copy entries */
	private static final int BUFFER = 1024;

	/**
	 * Unzip file to dirname
	 * 
	 * @param file file to unzip
	 * @param dirname dir name to store unzipped entries
	 */
	public static void unzip(ZipFile file, String dirname) {
		File dir = new File(dirname);
		dir.mkdirs();

		List<ZipEntry> dirs = new ArrayList<ZipEntry>();
		List<ZipEntry> files = new ArrayList<ZipEntry>();

		Enumeration< ? extends ZipEntry> e = file.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			
			if (entry.isDirectory()) {
				dirs.add(entry);
			} else {
				files.add(entry);
			}
		}
		for (ZipEntry entry : dirs) {
			File eDir = new File(dirname + "/" + entry.getName());
			eDir.mkdirs();
		}

		for (ZipEntry entry : files) {
			log.debug("Extracting: " + entry);
			unzipEntry(file, entry, dirname);
		}
	}

	/**
	 * Unzip a Entry
	 * @param file file to unzip
	 * @param entry entry to extract
	 * @param dir dir to store entry
	 */
	private static void unzipEntry(ZipFile file, ZipEntry entry, String dir) {
		if (entry == null) {
			return;
		}

		try {
			InputStream is = 
				new BufferedInputStream(file.getInputStream(entry));

			int count;
			byte data[] = new byte[BUFFER];
			FileOutputStream fos = 
				new FileOutputStream(dir + "/" + entry.getName());
			OutputStream dest = new BufferedOutputStream(fos, BUFFER);
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
			is.close();
		} catch (IOException e) {
			log.error(e);
		}
	}

	/**
	 * Create a zip file from zipFileName
	 * 
	 * @param file to compress
	 * @param zipFilename to create
	 * @throws Exception if fail
	 */
	public static void zip(File file, String zipFilename) throws Exception {
		// get a list of files from current directory
		if (file.isDirectory()) {
			zip(file.listFiles(), zipFilename);
		} else {
			zip(new File[] { file }, zipFilename);
		}
	}

	/**
	 * Create a zip file with files 
	 * @param files to compress
	 * @param zipFilename zip file to create
	 * @throws Exception if fail
	 */
	public static void zip(File[] files, String zipFilename) throws Exception {
		OutputStream os =
			new BufferedOutputStream(new FileOutputStream(zipFilename));
		ZipOutputStream zos = new ZipOutputStream(os);
		byte data[] = new byte[BUFFER];
		
		for (int i = 0; i < files.length; i++) {
			log.debug("Adding: " + files[i]);
			InputStream is = 
				new BufferedInputStream(new FileInputStream(files[i]), BUFFER);
			ZipEntry entry = new ZipEntry(files[i].getName());
			zos.putNextEntry(entry);
		
			int count;
			while ((count = is.read(data, 0, BUFFER)) != -1) {
				zos.write(data, 0, count);
			}
			is.close();
		}
		zos.close();

		log.debug("Zipped file: " + zipFilename);
	}

	/**
	 * Add File to Zip File
	 * @param zipFile to add on
	 * @param addFile file to add
	 * @throws Exception if fail
	 */
	public static void addFile(File zipFile, File addFile) throws Exception {
		OutputStream os = new 
			BufferedOutputStream(new FileOutputStream(zipFile));
		ZipOutputStream zos = new ZipOutputStream(os);

		log.debug("Adding: " + addFile.getName());

		InputStream is = 
			new BufferedInputStream(new FileInputStream(addFile), BUFFER);
		ZipEntry entry = new ZipEntry(addFile.getName());
		zos.putNextEntry(entry);

		copy(is, zos);
		
		log.debug("Added " + addFile.getName() + " to " + zipFile.getName());
	}

	/**
	 * Copy Streams
	 * @param is InputStream to read
	 * @param os OuputStream to write
	 * @throws IOException if fail
	 */
	private static void copy(InputStream is, OutputStream os) 
		throws IOException {
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			os.write(data, 0, count);
		}
		is.close();
		os.close();
	}
	
	/**
	 * Read a Zip Entry as byte[]
	 * @param file zip file
	 * @param ze zip entry
	 * @return byte with data 
	 * @throws IOException if fail
	 */
	public static byte[] readEntryAsByteArray(ZipFile file, ZipEntry ze) 
		throws IOException {
		InputStream is = file.getInputStream(ze);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		copy(is, os);
		return os.toByteArray();
	}
}
