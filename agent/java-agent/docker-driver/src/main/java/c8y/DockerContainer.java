package c8y;

public class DockerContainer {
    private String Command;
    private String CreatedAt;
    private String ID;
    private String Image;
    private String Labels;
    private String LocalVolumes;
    private String Mounts;
    private String Names;
    private String Networks;
    private String Ports;
    private String RunningFor;
    private String Size;
    private String Status;

    public String getCommand() { return Command; }
    public String getCreatedAt() { return CreatedAt; }
    public String getID() { return ID; }
    public String getImage() { return Image; }
    public String getLabels() { return Labels; }
    public String getLocalVolumes() { return LocalVolumes; }
    public String getMounts() { return Mounts; }
    public String getNames() { return Names; }
    public String getNetworks() { return Networks; }
    public String getPorts() { return Ports; }
    public String getRunningFor() { return RunningFor; }
    public String getSize() { return Size; }
    public String getStatus() { return Status; }
}