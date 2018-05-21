package c8y;

public class DockerImage {
    private String Containers;
    private String CreatedAt;
    private String CreatedSince;
    private String Digest;
    private String ID;
    private String Repository;
    private String SharedSize;
    private String Size;
    private String Tag;
    private String UniqueSize;
    private String VirtualSize;

    public String getContainers() { return Containers; }
    public String getCreatedAt() { return CreatedAt; }
    public String getCreatedSince() { return CreatedSince; }
    public String getDigest() { return Digest; }
    public String getID() { return ID; }
    public String getRepository() { return Repository; }
    public String getSharedSize() { return SharedSize; }
    public String getSize() { return Size; }
    public String getTag() { return Tag; }
    public String getUniqueSize() { return UniqueSize; }
    public String getVirtualSize() { return VirtualSize; }
}
