package com.miw.dit.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dani on 17/2/15.
 */
public class Attendee implements Parcelable {
    private String userId;
    private int eventId;
    private String profileImage;

    public Attendee() {
    }

    public Attendee(Parcel in) {
        userId = in.readString();
        eventId = in.readInt();
        profileImage = in.readString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "userId='" + userId + '\'' +
                ", eventId=" + eventId +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attendee attendee = (Attendee) o;

        if (eventId != attendee.eventId) return false;
        if (!userId.equals(attendee.userId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + eventId;
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(eventId);
        dest.writeString(profileImage);
    }

    public static final Parcelable.Creator<Attendee> CREATOR = new Parcelable.Creator<Attendee>() {
        public Attendee createFromParcel(Parcel in) {
            return new Attendee(in);
        }

        public Attendee[] newArray(int size) {
            return new Attendee[size];
        }
    };
}
