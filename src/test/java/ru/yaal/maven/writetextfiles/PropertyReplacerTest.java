package ru.yaal.maven.writetextfiles;

import org.apache.maven.project.MavenProject;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Yablokov Aleksey
 */
public class PropertyReplacerTest {

    @Test
    public void replace() throws Exception {
        MavenProject project = mock(MavenProject.class);
        when(project.getVersion()).thenReturn("1.0-SNAPSHOT");
        when(project.getArtifactId()).thenReturn("maven-plugin");
        when(project.getGroupId()).thenReturn("ru.yaal.maven");

        FileParameter fp = new FileParameter();
        fp.setNullValue("line-empty");
        fp.setLines(new String[]{
                "Description: ${project.description}",
                "Version: ${project.version}",
                "Artifact Id: ${project.artifactId}",
                "Artifact path: ${project.groupId}:${project.artifactId}:${project.version}",
                "$${project.groupId}: ${project.groupId}",
                "$$",
                "",
                null
        });
        PropertyReplacer r = new PropertyReplacer(project, "GLOBAL-EMPTY", false);
        r.replace(fp);

        assertThat(fp.getLines(), arrayContaining(
                "Description: line-empty",
                "Version: 1.0-SNAPSHOT",
                "Artifact Id: maven-plugin",
                "Artifact path: ru.yaal.maven:maven-plugin:1.0-SNAPSHOT",
                "${project.groupId}: ru.yaal.maven",
                "$$",
                "",
                ""
        ));
    }
}
