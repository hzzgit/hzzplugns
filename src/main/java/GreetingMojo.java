import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Says "Hi" to the user.
 *
 */
@Mojo( name = "sayhi")
public class GreetingMojo extends AbstractMojo
{
    /**
     * path of the classes folder.
     * @parameter expression="${classFolderPath}"
     */
    private String classFolderPath;
    /**
     * @parameter expression = "${application}"
     */
    private String application;
    @Parameter(
            defaultValue = "${project.build.directory}",
            required = true
    )
    private File outputDirectory;
    @Parameter(
            defaultValue = "${project.build.finalName}",
            readonly = true
    )
    private String finalName;
    @Parameter(
            defaultValue = "${project}",
            readonly = true,
            required = true
    )
    private MavenProject project;

    private String filePath;
    private String targetpath;
    private String modulepath;
    private String mainjarname;
    private String libpath;
    private String modulename;
    private String applicaname;

    public void execute() throws MojoExecutionException
    {
        File file = new File("");
        try {
            filePath = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
         targetpath=filePath+File.separator+"target";
         modulepath=filePath+File.separator+"src\\main\\resources\\module.properties";
         applicaname=filePath+File.separator+"deploy"+File.separator+"sample"+File.separator+"application.properties";
         libpath=targetpath+"\\dependency";
         pomread();
         loadmoduleproperties();
         readlib();
         readmainjar();
    }

    /**
     * 读取配置文件
     */
    private void readresource(){

    }

    private void pomread(){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File( filePath+ "/pom.xml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MavenXpp3Reader reader = new MavenXpp3Reader();

        Model model = null;
        try {
            model = reader.read(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String artifactId = model.getArtifactId();

        String groupId = model.getGroupId();

        String version = model.getVersion();

        String name = model.getName();
        String finalName = model.getBuild().getFinalName();
        System.out.println("finalname="+finalName);
        modulename=finalName;
        mainjarname=finalName+".jar";
    }

    private void loadmoduleproperties(){
        Properties properties=new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(modulepath));
            properties.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties.getProperty("module.name"));
    }

    private void readmainjar(){
        System.out.println("开始上传主包");
        File file1=new File(targetpath+"\\"+mainjarname);
        try {
            String image = HttpUtils.post(HttpUtils.URL_PREFIX_STRING, "image", file1,modulename+File.separator+"libs");
            System.out.println(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("开始上传配置文件");
        File applicafile=new File(applicaname);
        try {
            String image = HttpUtils.post(HttpUtils.URL_PREFIX_STRING, "image", applicafile,modulename+File.separator+"resources");
            System.out.println(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("jar包名:"+file1.getAbsolutePath());
    }

    private void readlib(){
        List<String> jar = FileUtil.scanFilesWithRecursion(libpath, "jar");
        for (String s : jar) {
            System.out.println("读取到lib:"+s);
            File file=new File(s);
            try {
                String image = HttpUtils.post(HttpUtils.URL_PREFIX_STRING, "image", file,modulename+File.separator+"libs");
                System.out.println(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}