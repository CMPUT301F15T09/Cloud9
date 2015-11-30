package com.example.yunita.tradiogc;

/**
 * This class handles the application's interactions with the webserver.
 */
public class WebServer {
    private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t09/user9/";
    // Specifies the limit size of elastic search (its default = 10)
    private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t09/user9/_search?size=1000000";

    private static final String RESOURCE_PHOTO_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t09/photo9/";
    private static final String SEARCH_PHOTO_URL = "http://cmput301.softwareprocess.es:8080/cmput301f15t09/photo9/_search?size=1000000";

    /**
     * Class constructor for the webserver.
     */
    public WebServer() {

    }

    /**
     * Gets the resource URL.
     *
     * @return RESOURCE_URL URL of the resource
     */
    public String getResourceUrl() {
        return RESOURCE_URL;
    }

    /**
     * Gets the search URL.
     *
     * @return SEARCH_URL URL for searching the webserver
     */
    public String getSearchUrl() {
        return SEARCH_URL;
    }

    /**
     * Gets the resource photo URL.
     *
     * @return RESOURCE_PHOTO_URL URL for accessing item photo resources
     */
    public String getResourcePhotoUrl() {
        return RESOURCE_PHOTO_URL;
    }

    /**
     * Searches and returns a photo URL.
     *
     * @return SEARCH_PHOTO_URL URL for the photo that was searched for
     */
    public String getSearchPhotoUrl() {
        return SEARCH_PHOTO_URL;
    }
}
