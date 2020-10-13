import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Says "Hi" to the user.
 *
 */
@Mojo( name = "autoStart")
public class BeginStartJar extends AbstractMojo
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


    public void execute() throws MojoExecutionException
    {
        RunCmdUtil.exec("systemctl restart "+finalName,new File("/"));
    }

}