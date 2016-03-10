package com.akexorcist.sleepingforless.network.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class Post {
    String kind;
    String id;
    String published;
    String updated;
    String etag;
    String url;
    String selfLink;
    String title;
    String content;
    Blog blog;
    Author author;
    Reply replies;
    List<String> labels;

    public Post() {
    }

    public String getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public String getPublished() {
        return published;
    }

    public String getUpdated() {
        return updated;
    }

    public String getEtag() {
        return etag;
    }

    public String getUrl() {
        return url;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Blog getBlog() {
        return blog;
    }

    public Author getAuthor() {
        return author;
    }

    public Reply getReplies() {
        return replies;
    }

    public List<String> getLabels() {
        return labels;
    }

    @Parcel(parcelsIndex = false)
    public static class Blog {
        String id;

        public Blog() {
        }

        public String getId() {
            return id;
        }
    }

    @Parcel(parcelsIndex = false)
    public static class Author {
        String id;
        String displayName;
        String url;

        public Author() {
        }

        public String getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getUrl() {
            return url;
        }

        @Parcel(parcelsIndex = false)
        public static class Image {
            String url;

            public Image() {
            }

            public String getUrl() {
                return url;
            }
        }
    }

    @Parcel(parcelsIndex = false)
    public static class Reply {
        String totalItems;
        String selfLink;

        public Reply() {
        }

        public String getTotalItems() {
            return totalItems;
        }

        public String getSelfLink() {
            return selfLink;
        }
    }
}
