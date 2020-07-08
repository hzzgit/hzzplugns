import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * TODO:递归扫描指定文件夹下面的指定文件
     *
     * @return ArrayList<Object>
     * @throws FileNotFoundException
     */
    public static List<String> scanFilesWithRecursion(String folderPath,String searchfiletype) {

        try {
            List<String> scanFiles = new ArrayList();
            File directory = new File(folderPath);
            if (!directory.isDirectory()) {
                throw new FileNotFoundException('"' + folderPath + '"' + " input path is not a Directory , please input the right path of the Directory. ^_^...^_^");
            }
            if (directory.isDirectory()) {
                File[] filelist = directory.listFiles();
                for (int i = 0; i < filelist.length; i++) {
                    /**如果当前是文件夹，进入递归扫描文件夹**/
                    if (filelist[i].isDirectory()) {
                        /**递归扫描下面的文件夹**/

                        scanFiles.addAll(scanFilesWithRecursion(filelist[i].getAbsolutePath(),searchfiletype));
                    }
                    /**非文件夹**/
                    else {
                        String filename=filelist[i].getName();
                        String filetype = filename.substring(filename.lastIndexOf("."));
                        if(("."+searchfiletype).equalsIgnoreCase(filetype)){
                            scanFiles.add(filelist[i].getAbsolutePath());
                        }
                    }
                }
            }
            return scanFiles;
        } catch (FileNotFoundException e) {
            return null;
        }
    }


}
