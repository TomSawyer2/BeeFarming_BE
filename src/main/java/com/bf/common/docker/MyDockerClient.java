package com.bf.common.docker;

import com.bf.modules.batchTasks.model.BatchTask;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MyDockerClient {
    @Value("${docker.max-containers-num}")
    private final Integer MAX_CONTAINER_NUM = 45;

    @Value("${docker.max-pids-num}")
    private Long MAX_PIDS_NUM;
    @Value("${docker.container.image-name}")
    private String CONTAINER_IMAGE_NAME;

    @Value("${docker.container.cpu-count}")
    private Long CONTAINER_CPU_COUNT;

    @Value("${docker.container.memory}")
    private Long CONTAINER_MEMORY;

    private final DockerClient client;
    private final AtomicInteger containersCnt = new AtomicInteger();

    private static final Logger log = LoggerFactory.getLogger(MyDockerClient.class);
    private Random random = new Random();


    public MyDockerClient(DockerClientConfig config) {
        client = DockerClientBuilder.getInstance(config).build();
        Info info = client.infoCmd().exec();
        log.info(info.toString());
    }

    /**
     * create container
     */
    public void tryCreateServerContainer(BatchTask batchTask, String upperOutputFilename, String downOutputFilename) {
        if (batchTask.getContainerId() != null && !batchTask.getContainerId().equals("")) {
            return;
        }
        String containerName = "BF-" + batchTask.getId();

        // 将id对应的文件夹复制到容器中
        String containerPath = "/home/game/code";
        String hostPath = "/home/ubuntu/BF/codeFiles/" + batchTask.getId();
        Bind bind = new Bind(hostPath, new Volume(containerPath));
        // 将/Result文件夹映射
        String resultPath = "/home/game/Result";
        String hostResultPath = "/home/ubuntu/BF/archiveResults/" + batchTask.getId();
        Bind bindResult = new Bind(hostResultPath, new Volume(resultPath));

        HostConfig hostConfig = new HostConfig()
                .withPidsLimit(MAX_PIDS_NUM)
                .withCpuCount(CONTAINER_CPU_COUNT)
                .withMemory(CONTAINER_MEMORY * 1024 * 1024)
                .withBinds(bind, bindResult);

        CreateContainerResponse response = client.createContainerCmd(CONTAINER_IMAGE_NAME)
                .withName(containerName)
                .withEnv("BF_ID=" + batchTask.getId())
                .withEnv("totalRound=" + batchTask.getTotalRounds())
                .withEnv("upperOutputFilename=" + "OUTPUT-UPPER")
                .withEnv("downOutputFilename=" + downOutputFilename)
                .withHostConfig(hostConfig)
                .exec();

        batchTask.setContainerId(response.getId());
        log.info("created container {}", containerName);

    }

    /**
     * copy archive from host to container
     */
    public void copyArchiveToContainerCmd(String containerId, String resource, String remote) {
        client.copyArchiveToContainerCmd(containerId).withHostResource(resource).withRemotePath(remote).exec();
    }

    /**
     * copy archive from container to host
     */
    public InputStream copyArchiveFromContainerCmd(String containerId, String resource) {
        return client.copyArchiveFromContainerCmd(containerId, resource).exec();
    }


    public void startContainer(String containerId) {
        client.startContainerCmd(containerId).exec();
        String statusCmd = "docker inspect " + containerId + " --format='{{.State.Status}}'";
        String codeCmd = "docker inspect " + containerId + " --format='{{.State.ExitCode}}'";
        String s = execRuntimeCmd(statusCmd);
        String s1 = execRuntimeCmd(codeCmd);
        log.info("started container {},status:{}, exitCode{}, now there are {} containers running in total",
                containerId, s, s1, containersCnt.getAndIncrement());
    }


    public void stopContainer(String containerId) {
        try {
            client.stopContainerCmd(containerId).exec();
        } catch (Exception e) {
            return;
        }
        log.info("stopped container {}, now there are {} containers running in total",
                containerId, containersCnt.getAndDecrement());

    }

    public void removeContainer(String containerId) {
        try {
            client.removeContainerCmd(containerId).exec();
        } catch (Exception e) {
            return;
        }
        log.info("removed container {}", containerId);
    }

    public void stopAndRemoveContainer(String containerId) {
        stopContainer(containerId);
        removeContainer(containerId);
    }

    public boolean isAvailable() {
        return containersCnt.get() < MAX_CONTAINER_NUM;
    }

    public String inspectExitCode(String container) {
        String cmd = String.format("docker inspect %s --format='{{.State.ExitCode}}'", container);
        return execRuntimeCmd(cmd);
    }

    public String inspectStatus(String container) {
        String cmd = String.format("docker inspect %s --format='{{.State.Status}}'", container);
        return execRuntimeCmd(cmd);
    }

    public String execTailContainerLogsCmd(String container, int lines) {
        String cmd = String.format("tail -n %s `docker inspect %s  --format='{{.LogPath}}`", lines,container);
        String logs = execRuntimeCmd(cmd);
        log.info("read logs from {}, length {}", container, logs.length());
        return logs;
    }

    public String execRuntimeCmd(String cmd) {
        StringBuilder ret = new StringBuilder();
        try {
            InputStream is = Runtime.getRuntime().exec(cmd).getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                ret.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public String execRuntimeCmd2(String cmd) {
        StringBuffer output = new StringBuffer();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];

            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            // Waits for the command to finish.
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }


    public void execRuntimeCmdWithNoReturn(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getContentFromInputStream(InputStream is, String startChar) {
        StringBuilder ret = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                ret.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = ret.toString();
        s = s.substring(s.indexOf(startChar));
        return s;
    }

}
