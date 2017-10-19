package com.example.nath.take3app.activity.dataModels;

/**
 * Created by nath on 17-Oct-17.
 */

public class PhotoData {

    private boolean google_visiond;
    private String data_created;
    private String image_path;
    private String user_id;
    private String vision_analysis;

    public PhotoData() {
    }

    public PhotoData(boolean google_visiond, String data_created, String image_path, String user_id, String vision_analysis) {
        this.google_visiond = google_visiond;
        this.data_created = data_created;
        this.image_path = image_path;
        this.user_id = user_id;
        this.vision_analysis = vision_analysis;
    }

    public boolean isGoogle_visiond() {
        return google_visiond;
    }

    public void setGoogle_visiond(boolean google_visiond) {
        this.google_visiond = google_visiond;
    }

    public String getData_created() {
        return data_created;
    }

    public void setData_created(String data_created) {
        this.data_created = data_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVision_analysis() {
        return vision_analysis;
    }

    public void setVision_analysis(String vision_analysis) {
        this.vision_analysis = vision_analysis;
    }


    @Override
    public String toString() {
        return "PhotoData{" +
                "google_visiond=" + google_visiond +
                ", data_created='" + data_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", user_id='" + user_id + '\'' +
                ", vision_analysis='" + vision_analysis + '\'' +
                '}';
    }
}
