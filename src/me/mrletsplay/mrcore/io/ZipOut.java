package me.mrletsplay.mrcore.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides tools for zipping files
 * @author MrLetsplay2003
 */
public class ZipOut {
	
	private ZipOutputStream out;

	/**
	 * Creates a ZipOut to the specified {@link File}<br>
	 * This class does not implement {@link Closeable} and thus doesn't need to be closed after the writing is finished<br>
	 * You can continue to write to the underlying OutputStream afterwards. To retrieve it, use {@link #getOutputStream()}<br>
	 * Alternatively, you can safely close the OutputStream using {@link #close()}
	 * @param zipFile The file to write to
	 * @throws IOException If an I/O error occurs
	 * @throws FileNotFoundException As specified by {@link FileOutputStream#FileOutputStream(File) FileOutputStream(File)}
	 */
	public ZipOut(File zipFile) throws IOException {
		if(!zipFile.exists()) {
			zipFile.getAbsoluteFile().getParentFile().mkdirs();
			zipFile.createNewFile();
		}
		this.out = new ZipOutputStream(new FileOutputStream(zipFile));
	}
	
	/**
	 * Creates a ZipOut to the specified {@link OutputStream}<br>
	 * This class does not implement {@link Closeable} and thus doesn't need to be closed after the writing is finished<br>
	 * You can continue to write to the underlying OutputStream afterwards. To retrieve it, use {@link #getOutputStream()}<br>
	 * Alternatively, you can safely close the OutputStream using {@link #close()}
	 * @param out The OutputStream to write to
	 */
	public ZipOut(OutputStream out) {
		this.out = new ZipOutputStream(out);
	}

	/**
	 * Creates a ZipOut to the specified {@link ZipOutputStream}<br>
	 * This class does not implement {@link Closeable} and thus doesn't need to be closed after the writing is finished<br>
	 * You can continue to write to the underlying OutputStream afterwards. To retrieve it, use {@link #getOutputStream()}<br>
	 * Alternatively, you can safely close the OutputStream using {@link #close()}
	 * @param out The ZipOutputStream to write to
	 */
	public ZipOut(ZipOutputStream out) {
		this.out = out;
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file) throws IOException {
		writeFile("", file);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param subPath The sub path/folder to write to
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(String subPath, File file) throws IOException {
		file = file.getAbsoluteFile();
		addFilesToZip(file, out, subPath, file.getAbsolutePath().length() - file.getName().length() - 1);
	}
	
	private static void addFilesToZip(File f, ZipOutputStream out, String subPath, int sI) throws IOException{
		if(f.isDirectory()){
			for(File fl : f.listFiles()){
				addFilesToZip(fl, out, subPath, sI);
			}
		}else{
			FileInputStream fIn = new FileInputStream(f);
			out.putNextEntry(new ZipEntry(subPath + "/" + f.getAbsolutePath().substring(sI + 1)));
			byte[] buffer = new byte[4096];
			int curr;
			while((curr=fIn.read(buffer))>0){
				out.write(buffer, 0, curr);
			}
			fIn.close();
		}
	}
	
	/**
	 * Retrieves the underlying {@link ZipOutputStream} this instance uses to write data
	 * @return The {@link ZipOutputStream} for this instance
	 */
	public ZipOutputStream getOutputStream() {
		return out;
	}
	
	/**
	 * Closes the underlying OutputStream using {@link ZipOutputStream#close()}
	 * @throws IOException If an I/O error occurs while closing the stream
	 */
	public void close() throws IOException {
		out.close();
	}
	
}
