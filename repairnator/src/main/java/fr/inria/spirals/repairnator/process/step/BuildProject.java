package fr.inria.spirals.repairnator.process.step;

import fr.inria.spirals.repairnator.process.ProjectInspector;
import fr.inria.spirals.repairnator.process.maven.MavenErrorHandler;
import fr.inria.spirals.repairnator.process.maven.MavenHelper;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by urli on 03/01/2017.
 */
public class BuildProject extends AbstractStep {

    public BuildProject(ProjectInspector inspector) {
        super(inspector);
    }

    protected void businessExecute() {
        this.getLogger().debug("Start building project with maven (skip tests).");
        Properties properties = new Properties();
        properties.setProperty("maven.test.skip","true");

        MavenHelper helper = new MavenHelper(this.getPom(), "test", properties, this.getClass().getName(), this.inspector, true);

        int result = helper.run();

        if (result == MavenHelper.MAVEN_SUCCESS) {
            this.state = ProjectState.BUILDABLE;
        } else {
            this.getLogger().info("Repository "+this.inspector.getRepoSlug()+" cannot be built.");
            this.shouldStop = true;
        }
    }
}