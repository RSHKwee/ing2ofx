package sandbox;

import java.io.*;

public class DisplayFileInformation {

  public static void main(String args[]) {
    String Filename;// = args[0];
    Filename = "D:\\git\\ing2ofx\\target\\ing2ofx.exe";

    File f1 = new File(Filename);

    if (f1.isFile()) {
      System.out.println("File Name: " + f1.getName());
      System.out.println("File last modified: " + f1.lastModified());
      System.out.println("File Size: " + f1.length() + " Bytes");
      System.out.println("File Path: " + f1.getPath());
      System.out.println("Absolute Path: " + f1.getAbsolutePath());
      System.out.println("Parent: " + f1.getParent());
      System.out.println(f1.canWrite() ? "File is writable" : "File is not writable");
      System.out.println(f1.canRead() ? "File is readable" : "File is not readable");
      System.out.println(f1.isHidden() ? "File is hidden" : "File is not hidden");
    } else if (f1.isDirectory()) {
      System.out.println("Contents of " + Filename + " directory are: ");

      int count = 0;
      String[] s = f1.list();

      for (String str : s) {
        File f = new File(Filename, str);
        if (f.isFile())
          count++;
      }

      System.out.println("There are " + count + " Files in given directory. Subdirectories not considered.");
    }
  }
}
