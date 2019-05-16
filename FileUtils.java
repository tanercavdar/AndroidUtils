package tr.com.bracket.aiku360.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static boolean deleteFile(String file) {
        return new File(file).delete();
    }

    public static  boolean copyFile(String sourceFile, String targetFile) {
        try {
            FileInputStream inStream = new FileInputStream(new File(sourceFile));
            FileOutputStream outStream = new FileOutputStream(new File(targetFile));
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean renameFile(String sourceFile, String newNameWithExt) {
        return new File(sourceFile).renameTo(new File(newNameWithExt));
    }

    public  static boolean createFileAppend(String sourceFile, String content) {
        try {
            File logFile = new File(sourceFile);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(content);
            buf.newLine();
            buf.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public  static boolean createFile(String sourceFile) {
        try {
            File file = new File(sourceFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public  static List<FileModel> getFileList(String sourceFolder, FilenameFilter... filenameFilter) {
        List<FileModel> list = new ArrayList<>();
        File[] files = new File(sourceFolder).listFiles();
        if (files == null)
            return list;
        for (File f : files) {
            if (f.isFile()) {
                FileModel model = new FileModel();
                model.sFileName = f.getName();
                model.sExtension = f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length());
                model.sFullFileName = f.getPath();
                model.nSizeKb = (int) f.length() / 1024;
                model.file = f;
                list.add(model);
            }
        }
        return list;
    }

    public static  boolean isExistFile(String sourceFile) {
        return new File(sourceFile).exists();
    }

    public static  boolean isExistFolder(String folder) {
        return new File(folder).exists();
    }

    public  static void createFolder(String sourceFolder, String folderName) {
         new File(sourceFolder + "/" + folderName).mkdirs();
    }

  static   class FileModel {

        public String sFullFileName = "";
        public String sExtension = "";
        public String sFileName = "";
        public Integer nSizeKb = 0;
        public File file = null;

    }

}
