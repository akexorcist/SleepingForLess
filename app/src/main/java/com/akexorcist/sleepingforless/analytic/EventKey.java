package com.akexorcist.sleepingforless.analytic;

/**
 * Created by Akexorcist on 3/23/2016 AD.
 */
public class EventKey {
    public static class Category {
        public static final String CONTENT_TRACKING = "Content Tracking";
        public static final String SORT_TYPE_TRACKING = "Sort Type Tracking";
        public static final String SEARCH_TRACKING = "Search Tracking";
        public static final String SETTINGS_TRACKING = "Settings Tracking";
    }

    public static class Action {
        public static final String READ = "Read";
        public static final String ADD_BOOKMARK = "Add Bookmark";
        public static final String REMOVE_BOOKMARK = "Remove Bookmark";
        public static final String READ_UNTIL_FINISH = "Read Until Finish";
        public static final String SHARE = "Share";
        public static final String SEARCH = "Search";
        public static final String SORT = "Sort";
        public static final String PUSH_NOTIFICATION = "Push Notification";
        public static final String CLEAR_APP_CACHE = "Clear App Cache";
        public static final String OPEN_FROM_DEEP_LINK = "Open Form Deep Link";
    }

    public static class Label {
        public static final String SORT_BY_UPDATED = "Sort By Updated";
        public static final String SORT_BY_PUBLISHED = "Sort By Published";
        public static final String ENABLE = "Enable";
        public static final String DISABLE = "Disable";
    }

    public static class Page {
        public static final String MAIN_PAGE = "Main Page";
        public static final String READ_POST = "Post Reader Page";
        public static final String READ_BOOKMARK = "Bookmark Reader Page";
        public static final String BOOKMARK_LIST = "Bookmark List Page";
        public static final String SEARCH = "Search Page";
        public static final String IMAGE_PREVIEW = "Image Preview Page";
        public static final String DEEP_LINK = "Deep Link Page";
        public static final String Settings = "Settings Page";
    }
}
