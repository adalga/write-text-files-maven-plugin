package ru.yaal.maven.writetextfiles;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * @author Aleksey Yablokov.
 */
@Mojo(name = WriteTextFilesMojo.MOJO_NAME, requiresProject = true)
@SuppressWarnings("unused")
public class WriteTextFilesMojo extends AbstractMojo {
    public static final String MOJO_NAME = "write-text-files";

    @Parameter
    @SuppressWarnings({"unused", "MismatchedReadAndWriteOfArray"})
    private FileParameter[] files;

    @Parameter(defaultValue = "UTF-8")
    @SuppressWarnings("unused")
    private String charset;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (files != null) {
            try {
                for (FileParameter file : files) {
                    File path = file.getPath();
                    if (path == null) {
                        throw new MojoExecutionException("Path is empty");
                    }
                    path.getParentFile().mkdirs();
                    if (!path.createNewFile()) {
                        getLog().info("Overwrite file: " + path.getAbsolutePath());
                    } else {
                        getLog().info("Write to new file: " + path.getAbsolutePath());
                    }
                    Files.write(path.toPath(), Arrays.asList(file.getLines()), Charset.forName(charset));
                }
            } catch (MojoExecutionException e) {
                throw e;
            } catch (Exception e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
        } else {
            getLog().warn("No files specified in configuration.");
        }
    }

}

