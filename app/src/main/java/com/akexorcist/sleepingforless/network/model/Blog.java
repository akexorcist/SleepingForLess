package com.akexorcist.sleepingforless.network.model;

import org.parceler.Parcel;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class Blog {
    String kind;
    String id;
    String name;
    String description;
    String published;
    String updated;
    String url;
    String selfLink;
    Post posts;
    Page pages;
    Locale locale;

    public Blog() {
    }

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() {
        return updated;
    }

    public String getUrl() {
        return url;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public Post getPosts() {
        return posts;
    }

    public Page getPages() {
        return pages;
    }

    public Locale getLocale() {
        return locale;
    }

    @Parcel(parcelsIndex = false)
    public static class Post {
        int totalItems;
        String selfLink;

        public Post() {
        }

        public int getTotalItems() {
            return totalItems;
        }

        public String getSelfLink() {
            return selfLink;
        }
    }

    @Parcel(parcelsIndex = false)
    public static class Page {
        int totalItems;
        String selfLink;

        public Page() {
        }

        public int getTotalItems() {
            return totalItems;
        }

        public String getSelfLink() {
            return selfLink;
        }
    }

    @Parcel(parcelsIndex = false)
    public static class Locale {
        String language;
        String country;
        String variant;

        public Locale() {
        }

        public String getLanguage() {
            return language;
        }

        public String getCountry() {
            return country;
        }

        public String getVariant() {
            return variant;
        }
    }
}
