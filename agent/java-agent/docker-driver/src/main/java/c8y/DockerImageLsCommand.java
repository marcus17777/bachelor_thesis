package c8y;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class DockerImageLsCommand extends CommandExecutor {
    public List<DockerImage> execute() {
        try {
            BufferedReader reader = this.systemCall("docker", "image", "ls", "--format", "{{ json . }}");

            Gson g = new Gson();
            return reader.lines().map((s -> g.fromJson(s, DockerImage.class))).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}