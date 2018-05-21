package c8y.docker;

import c8y.*;
import c8y.lx.driver.OperationExecutor;
import c8y.lx.driver.OpsUtil;
import c8y.lx.driver.PollingDriver;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.inventory.InventoryApi;

import java.util.HashMap;

public class DockerDriver extends PollingDriver implements OperationExecutor {
    private final static String SUPPORTED_OPERATION_TYPE = "c8y_Docker";

    private InventoryApi inventory;
    private GId gid;


    public DockerDriver() {
        super(SUPPORTED_OPERATION_TYPE, SUPPORTED_OPERATION_TYPE, 5000L);
    }

    @Override
    public void initialize() throws Exception {

    }

    @Override
    public void initialize(Platform platform) throws Exception {
        super.initialize(platform);
        this.inventory = platform.getInventoryApi();
    }

    @Override
    public String supportedOperationType() {
        return SUPPORTED_OPERATION_TYPE;
    }

    @Override
    public OperationExecutor[] getSupportedOperations() {
        return new OperationExecutor[] { this };
    }

    @Override
    public void initializeInventory(ManagedObjectRepresentation mo) {
        super.initializeInventory(mo);
        mo.set(getDockerState());
        OpsUtil.addSupportedOperation(mo, supportedOperationType());
    }

    @Override
    public void discoverChildren(ManagedObjectRepresentation mo) {
        super.discoverChildren(mo);
        gid = mo.getId();
    }

    @Override
    public void run() {
        updateDockerState();
    }

    private Docker getDockerState() {
        Docker dockerState = new Docker();
        dockerState.put("images", new DockerImageLsCommand().execute());
        dockerState.put("containers", new DockerContainerLsCommand().execute());
        return dockerState;
    }

    private void updateDockerState() {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        mo.setId(gid);
        mo.set(getDockerState());
        inventory.update(mo);
    }

    @Override
    public void execute(OperationRepresentation operation, boolean cleanup) throws Exception {
        if (!gid.equals(operation.getDeviceId())) {
            // Silently ignore the operation if it is not targeted to us,
            // another driver will (hopefully) care.
            return;
        }

        if (cleanup) {
            operation.setStatus(OperationStatus.SUCCESSFUL.toString());
        } else {
            HashMap c8y_Docker = (HashMap) operation.getProperty(supportedOperationType());
            String dockerCommand = (String) c8y_Docker.get("dockerCommand");

            CommandExecutor command = null;
            switch (dockerCommand) {
                case "image rm":
                    command = new DockerImageRmCommand(operation, (String) c8y_Docker.get("imageID"));
                    break;

                case "image pull":
                    command = new DockerImagePullCommand(operation, (String) c8y_Docker.get("image"));
                    break;

                case "container rm":
                    command = new DockerContainerRmCommand(operation, (String) c8y_Docker.get("containerID"));
                    break;

                case "container start":
                    command = new DockerContainerStartCommand(operation, (String) c8y_Docker.get("containerID"));
                    break;

                case "container stop":
                    command = new DockerContainerStopCommand(operation, (String) c8y_Docker.get("containerID"));
                    break;

                case "container run":
                    command = new DockerContainerRunCommand(operation,
                            (String) c8y_Docker.get("imageName"),
                            "-d " + c8y_Docker.get("containerOptions"),
                            (String) c8y_Docker.get("containerCmd"),
                            (String) c8y_Docker.get("containerCmdArgs")
                    );
                    break;
            }
            if (command != null) {
                command.setCallback((Object result) -> this.updateDockerState());
                Thread thread = new Thread(command);
                thread.start();
            }
        }
    }
}
