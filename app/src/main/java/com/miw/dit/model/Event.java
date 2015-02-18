package com.miw.dit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 8/2/15.
 */
public class Event implements Parcelable {

    private int id;
    private String title;
    private String description;
    private String headerImage;
    private String address;
    private long time;
    private double lat;
    private double lng;
    private String userId;
    private String profileImage;
    private int categoryId;
    private List<Attendee> attendees;

    public Event() {
        attendees = new ArrayList<>();
    }

    public Event(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        headerImage = in.readString();
        address = in.readString();
        time = in.readLong();
        lat = in.readDouble();
        lng = in.readDouble();
        userId = in.readString();
        profileImage = in.readString();
        categoryId = in.readInt();
        attendees = new ArrayList<>();
        in.readTypedList(attendees, Attendee.CREATOR);
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

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", headerImage='" + headerImage + '\'' +
                ", address='" + address + '\'' +
                ", time=" + time +
                ", lat=" + lat +
                ", lng=" + lng +
                ", userId='" + userId + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", categoryId=" + categoryId +
                ", attendees=" + attendees +
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
        dest.writeString(headerImage);
        dest.writeString(address);
        dest.writeLong(time);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(userId);
        dest.writeString(profileImage);
        dest.writeInt(categoryId);
        dest.writeTypedList(attendees);
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
