package com.miw.dit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dani on 8/2/15.
 */
public class Event implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String image;
    private String address;
    private long time;
    private float lat;
    private float lng;
    private int userId;
    private int categoryId;
    private int placeId;

    public Event() {

    }

    public Event(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        image = in.readString();
        address = in.readString();
        time = in.readLong();
        lat = in.readFloat();
        lng = in.readFloat();
        userId = in.readInt();
        categoryId = in.readInt();
        placeId = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", lat=" + lat +
                ", lng=" + lng +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", placeId=" + placeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(address);
        dest.writeLong(time);
        dest.writeFloat(lat);
        dest.writeFloat(lng);
        dest.writeInt(userId);
        dest.writeInt(categoryId);
        dest.writeInt(placeId);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
