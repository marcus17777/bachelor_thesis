package c8y;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class DockerContainerLsCommand extends CommandExecutor {
    @Override
    public List<DockerContainer> execute() {
        try {
            BufferedReader reader = this.systemCall("docker", "container", "ls", "-a", "--format", "{{ json . }}");

            Gson g = new Gson();
            return reader.lines().map(s -> g.fromJson(s, DockerContainer.class)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}