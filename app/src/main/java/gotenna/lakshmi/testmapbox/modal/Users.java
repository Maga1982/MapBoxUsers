package gotenna.lakshmi.testmapbox.modal;

public class Users {

    private String name;
    private String desc;
    private String latitude;
    private String longitude;

    public Users(String name, String desc, String latitude, String longitude) {
        this.name = name;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
