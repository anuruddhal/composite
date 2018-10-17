package org.wso2.composite;

import io.fabric8.docker.client.DockerClient;
import io.fabric8.docker.dsl.EventListener;
import io.fabric8.docker.dsl.OutputHandle;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.wso2.composite.utils.DockerGenUtils.printError;


/**
 * Docker Image Builder.
 */
public class DockerImageBuilder {
    /**
     * Build Docker image.
     *
     * @param dockerFilePath Path to Dockerfile
     * @param tag            Image tag
     */
    public void buildImage(String dockerFilePath, String tag) throws
            IOException, InterruptedException {
        CountDownLatch buildDone = new CountDownLatch(1);
        DockerClient client = new io.fabric8.docker.client.DefaultDockerClient();
        OutputHandle buildHandle = client.image()
                .build()
                .withRepositoryName(tag)
                .withNoCache()
                .alwaysRemovingIntermediate()
                .usingListener(new EventListener() {
                    @Override
                    public void onSuccess(String message) {
                        buildDone.countDown();
                    }

                    @Override
                    public void onError(String message) {
                        printError("Unable to build Docker image: " + message);
                        buildDone.countDown();
                    }

                    @Override
                    public void onError(Throwable t) {
                        printError("Unable to build Docker image: " + t.getMessage());
                        buildDone.countDown();
                    }

                    @Override
                    public void onEvent(String event) {
                        printError(event);
                    }
                })
                .fromFolder(dockerFilePath);

        buildDone.await();
        buildHandle.close();
        client.close();

    }


}

