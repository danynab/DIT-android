package com.miw.dit.wservices.impl;

/**
 * Created by Dani on 7/2/15.
 */
public class WServicesUtil {

    private static final String URL_BASE = "http://156.35.95.67/dit";
//    private static final String URL_BASE = "http://192.168.0.12:5000";


    // * Categories * //

    private static final String GET_CATEGORY = URL_BASE + "/categories/?";

    private static final String GET_CATEGORIES = URL_BASE + "/categories";

    public static String getCategoryUrl(int categoryId) {
        return GET_CATEGORY.replace("?", String.valueOf(categoryId));
    }

    public static String getCategoriesUrl() {
        return GET_CATEGORIES;
    }


    // * Events * //

    private static final String GET_EVENT = URL_BASE + "/events/?";

    private static final String GET_EVENTS = URL_BASE + "/events";

    private static final String GET_EVENTS_BY_CATEGORY = URL_BASE + "/categories/?/events";

    private static final String GET_EVENTS_BY_USER = URL_BASE + "/users/?/events";

    private static final String GET_EVENTS_ATTENDED_BY_USER = URL_BASE + "/users/?/attendees/events";

    public static String getSaveEventUrl() {
        return GET_EVENTS;
    }


    public static String getEventUrl(int eventId) {
        return GET_EVENT.replace("?", String.valueOf(eventId));
    }

    public static String getEventsByUserUrl(String userId, Integer fromId, Integer elements) {
        String urlWithPartition = completeEventsUrlWithPartition(fromId, elements);
        if (!urlWithPartition.isEmpty())
            urlWithPartition = "?" + urlWithPartition;
        return GET_EVENTS_BY_USER.replace("?", userId) + urlWithPartition;
    }

    public static String getNearEventsUrl(double lat, double lng, double radius, Integer fromId, Integer elements) {
        return GET_EVENTS + completeEventsUrlWhitPosition(lat, lng, radius, fromId, elements);
    }

    public static String getNearEventsByCategoryUrl(int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements) {
        return GET_EVENTS_BY_CATEGORY.replace("?", String.valueOf(categoryId)) + completeEventsUrlWhitPosition(lat, lng, radius, fromId, elements);
    }


    public static String getEventsAttendedByUsertUrl(String userId, Integer fromId, Integer elements) {
        String urlWithPartition = completeEventsUrlWithPartition(fromId, elements);
        if (!urlWithPartition.isEmpty())
            urlWithPartition = "?" + urlWithPartition;
        return GET_EVENTS_ATTENDED_BY_USER.replace("?", userId) + urlWithPartition;
    }


    // * Attendees * //

    private static final String GET_ATTENDEES_BY_EVENT = GET_EVENT + "/attendees";

    private static final String GET_ATTENDEE = GET_EVENT + "/attendees/?";

    public static String getAttendeeUrl(int eventId, String userId) {
        return GET_ATTENDEE.replaceFirst("\\?", String.valueOf(eventId)).replaceFirst("\\?", userId);
    }

    public static String getAttendeesByEventUrl(int eventId) {
        return GET_ATTENDEES_BY_EVENT.replace("?", String.valueOf(eventId));
    }

    public static String getSaveAttendeetUrl(int eventId) {
        return getAttendeesByEventUrl(eventId);
    }


    // * Places * //

    private static final String GET_PLACES_BY_CATEGORY = GET_CATEGORY + "/places";

    public static String getPlacesByCategoryUrl(int categoryId, double lat, double lng, double radius, Integer elements) {
        String url = "?lat=" + lat + "&lng=" + lng + "&radius=" + radius;
        if (elements != null)
            url = url + "&elements=" + elements;
        return GET_PLACES_BY_CATEGORY.replace("?", String.valueOf(categoryId)) + url;
    }


    // * Functions * //

    private static String completeEventsUrlWhitPosition(double lat, double lng, double radius, Integer fromId, Integer elements) {
        String url = "?lat=" + lat + "&lng=" + lng + "&radius=" + radius;
        String urlWithPartition = completeEventsUrlWithPartition(fromId, elements);
        if (!urlWithPartition.isEmpty())
            url += "&" + urlWithPartition;
        return url;
    }

    private static String completeEventsUrlWithPartition(Integer fromId, Integer elements) {
        String url = "";
        if (fromId != null)
            url += "fromId=" + fromId;

        if (elements != null) {
            if (!url.isEmpty())
                url += "&";
            url += "elements=" + elements;
        }

        return url;
    }
}
