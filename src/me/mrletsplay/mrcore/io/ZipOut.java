package me.mrletsplay.mrcore.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import me.mrletsplay.mrcore.misc.FriendlyException;

/**
 * Provides tools for zipping files
 * @author MrLetsplay2003
 */
public class ZipOut implements Closeable {
	
	public static final Consumer<File> EMPTY_HANDLER = f -> {};
	
	private ZipOutputStream out;

	/**
	 * Creates a ZipOut to the specified {@link File}<br>
	 * @param zipFile The file to write to
	 * @throws IOException If an I/O error occurs
	 * @throws FileNotFoundException As specified by {@link FileOutputStream#FileOutputStream(File) FileOutputStream(File)}
	 */
	public ZipOut(File zipFile) throws IOException {
		IOUtils.createFile(zipFile);
		this.out = new ZipOutputStream(new FileOutputStream(zipFile));
	}
	
	/**
	 * Creates a ZipOut to the specified {@link OutputStream}<br>
	 * @param out The OutputStream to write to
	 */
	public ZipOut(OutputStream out) {
		this.out = new ZipOutputStream(out);
	}

	/**
	 * Creates a ZipOut to the specified {@link ZipOutputStream}<br>
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
		writeFile(file, EMPTY_HANDLER);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param fileHandler A handler function which gets called when a file is being added
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file, Consumer<File> fileHandler) throws IOException {
		writeFile(file, new ArrayList<>(), fileHandler);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param exceptions Files to exclude
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file, List<File> exceptions) throws IOException {
		writeFile(file, exceptions, EMPTY_HANDLER);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param filter Predicate to determine which files to include
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file, Predicate<File> filter) throws IOException {
		writeFile(file, filter, EMPTY_HANDLER);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param exceptions Files to exclude
	 * @param fileHandler A handler function which gets called when a file is being added
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file, List<File> exceptions, Consumer<File> fileHandler) throws IOException {
		writeFile("", file, exceptions, fileHandler);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param filter Predicate to determine which files to include
	 * @param fileHandler A handler function which gets called when a file is being added
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(File file, Predicate<File> filter, Consumer<File> fileHandler) throws IOException {
		writeFile("", file, filter, fileHandler);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param subPath The sub path/folder to write to
	 * @param exceptions Files to exclude
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(String subPath, File file, List<File> exceptions) throws IOException {
		writeFile(subPath, file, exceptions, EMPTY_HANDLER);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param subPath The sub path/folder to write to
	 * @param filter Predicate to determine which files to include
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(String subPath, File file, Predicate<File> filter) throws IOException {
		writeFile(subPath, file, filter, EMPTY_HANDLER);
	}
	
	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param subPath The sub path/folder to write to
	 * @param exceptions Files to exclude
	 * @param fileHandler A handler function which gets called when a file is being added
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(String subPath, File file, List<File> exceptions, Consumer<File> fileHandler) throws IOException {
		file = file.getAbsoluteFile();
		addFilesToZip(file, out, subPath, absFilePath(file).length() - file.getName().length() - 1, f -> !isException(f, exceptions), fileHandler);
	}

	/**
	 * Writes a file (possibly a folder) to the zip file<br>
	 * If the provided file is a folder, a subfolder will be created inside the zip file and all its contents (and possibly subfolders) will be added
	 * @param file A {@link File} object (can be a folder)
	 * @param subPath The sub path/folder to write to
	 * @param filter Predicate to determine which files to include
	 * @param fileHandler A handler function which gets called when a file is being added
	 * @throws IOException If an I/O error occurs
	 */
	public void writeFile(String subPath, File file, Predicate<File> filter, Consumer<File> fileHandler) throws IOException {
		file = file.getAbsoluteFile();
		addFilesToZip(file, out, subPath, absFilePath(file).length() - file.getName().length() - 1, filter, fileHandler);
	}
	
	private static void addFilesToZip(File f, ZipOutputStream out, String subPath, int sI, Predicate<File> filter, Consumer<File> fileHandler) throws IOException{
		if(f.isDirectory()){
			if(filter.test(f)) return;
			for(File fl : f.listFiles()){
				addFilesToZip(fl, out, subPath, sI, filter, fileHandler);
			}
		}else{
			if(filter.test(f)) return;
			fileHandler.accept(f);
			FileInputStream fIn = new FileInputStream(f);
			out.putNextEntry(new ZipEntry((subPath.isEmpty() ? "" : subPath + "/") + absFilePath(f).substring(sI + 1)));
			byte[] buffer = new byte[4096];
			int curr;
			while((curr = fIn.read(buffer)) > 0){
				out.write(buffer, 0, curr);
			}
			fIn.close();
		}
	}
	
	private static boolean isException(File f, List<File> exceptions) {
		return exceptions.stream().anyMatch(e -> {
			try {
				return e.getCanonicalPath().equals(f.getCanonicalPath());
			} catch (IOException e1) {
				throw new FriendlyException(e1);
			}
		});
	}
	
	private static String absFilePath(File f) throws IOException {
		return f.getCanonicalPath().replace('\\', '/');
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
	@Override
	public void close() throws IOException {
		out.close();
	}
	
}
