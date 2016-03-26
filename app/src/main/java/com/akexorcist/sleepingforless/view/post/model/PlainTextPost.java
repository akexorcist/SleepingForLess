package com.akexorcist.sleepingforless.view.post.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Akexorcist on 3/13/2016 AD.
 */

@Parcel(parcelsIndex = false)
public class PlainTextPost extends BasePost {
    String text;
    List<Highlight> highlightList;
    List<Link> linkList;

    public PlainTextPost() {
    }

    public PlainTextPost(String text, List<Highlight> highlightList, List<Link> linkList) {
        setRaw(text);
        this.text = text;
        this.highlightList = highlightList;
        this.linkList = linkList;
    }

    public String getText() {
        return text;
    }

    public List<Link> getLinkList() {
        return linkList;
    }

    public boolean isLinkAvailable() {
        return linkList != null && !linkList.isEmpty();
    }

    public List<Highlight> getHighlightList() {
        return highlightList;
    }

    public boolean isHighlightAvailable() {
        return linkList != null && !highlightList.isEmpty();
    }

    @Parcel(parcelsIndex = false)
    public static class Highlight {
        int start;
        int end;
        int color;

        public Highlight() {
        }

        public Highlight(int start, int end, int color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getColor() {
            return color;
        }
    }

    @Parcel(parcelsIndex = false)
    public static class Link {
        int start;
        int end;
        String url;

        public Link() {
        }

        public Link(int start, int end, String url) {
            this.start = start;
            this.end = end;
            this.url = url;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getUrl() {
            return url;
        }
    }
}
