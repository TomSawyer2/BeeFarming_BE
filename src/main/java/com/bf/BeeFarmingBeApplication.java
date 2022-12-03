package com.bf;

import com.bf.common.docker.MyDockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BeeFarmingBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeeFarmingBeApplication.class, args);
    }

    @Bean
    public MyDockerClient getMyDockerClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://127.0.0.1:2375")
                .build();
        return new MyDockerClient(config);
    }
}
