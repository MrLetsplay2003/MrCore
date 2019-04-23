package me.mrletsplay.mrcore.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZIPFileUtils {

	/**
	 * Zips a folder
	 * @deprecated Has been replaced by {@link ZipOut}
	 * @param file The file to zip, can be a folder as well
	 * @param zipFile The file to zip to (Will be created if it doesn't exist)
	 * @param compressionLevel The compression level to use (0-9) See {@link ZipOutputStream#setLevel(int)}
	 * @throws IOException If an IO error occurs while zipping the folder
	 */
	@Deprecated
	public static void zipFolder(File file, File zipFile, int compressionLevel) throws IOException {
		if(!zipFile.exists()) {
			zipFile.getParentFile().mkdirs();
			zipFile.createNewFile();
		}
		ZipOutputStream o = new ZipOutputStream(new FileOutputStream(zipFile));
		o.setLevel(compressionLevel);
		addFilesToZip(file, o, file.getParentFile().getAbsolutePath().length()+1);
		o.close();
	}
	
	private static void addFilesToZip(File f, ZipOutputStream out, int sI) throws IOException {
		if(f.isDirectory()){
			for(File fl : f.listFiles()){
				addFilesToZip(fl, out, sI);
			}
		}else{
			FileInputStream fIn = new FileInputStream(f);
			out.putNextEntry(new ZipEntry(f.getAbsolutePath().substring(sI)));
			byte[] buffer = new byte[4096];
			int curr;
			while((curr=fIn.read(buffer))>0){
				out.write(buffer, 0, curr);
			}
			fIn.close();
		}
	}

	/**
	 * Unzips a zip file to a given folder
	 * @param zipFile The file to unzip
	 * @param folderOut The folder to unzip into
	 * @throws IOException If an IO error occurs while unzipping the file
	 */
	public static void unzipFile(File zipFile, File folderOut) throws IOException {
		if(!folderOut.exists()) {
			folderOut.mkdirs();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry currZipEntry;
		while((currZipEntry = zipIn.getNextEntry())!=null) {
			if(!currZipEntry.isDirectory()) {
				File out = new File(folderOut.getAbsolutePath()+"/"+currZipEntry.getName());
				out.getParentFile().mkdirs();
				FileOutputStream fOut = new FileOutputStream(out);
				byte[] buf = new byte[4096];
				int len;
				while((len = zipIn.read(buf))>0) {
					fOut.write(buf, 0, len);
				}
				fOut.close();
			}
		}
		zipIn.close();
	}
	
}
