package com.akexorcist.sleepingforless.util.content;

import android.graphics.Color;

import com.akexorcist.sleepingforless.view.post.constant.PostType;
import com.akexorcist.sleepingforless.view.post.model.BasePost;
import com.akexorcist.sleepingforless.view.post.model.CodePost;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;
import com.akexorcist.sleepingforless.view.post.model.PlainTextPost;
import com.akexorcist.sleepingforless.view.post.model.VideoPost;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class ContentUtility {
    private static ContentUtility utility;

    public static ContentUtility getInstance() {
        if (utility == null) {
            utility = new ContentUtility();
        }
        return utility;
    }

    public List<String> wrapContent(String content) {
        String[] t = moveCodeCloseTagToNewline(replaceNewlineWithBR(removeNewlineFromHeader(removeSpaceCode(removeDivTag(content))))).split("<br .*/>");
        return removeBlankLine(wrapCode(wrapTextColor(removeUnusedATagLine(wrapLinkTag(wrapImageTag(removeBlankLine(removeUnusedCodeLine(t))))))));
    }

    private String removeDivTag(String text) {
        return text.replaceAll("[<](/)?div[^>]*[>]", "");
    }

    private String removeSpaceCode(String text) {
        return text.replaceAll("&nbsp; ", " ").replaceAll("&nbsp;", " ");
    }

    private String removeNewlineFromHeader(String text) {
        return text.replaceAll("(<h\\d).*?>\\n?\\n?\\r?(.+)(</h\\d>)", "$1>$2$3<br />");
    }

    private String replaceNewlineWithBR(String text) {
        return text.replace("\\n", "<br />");
    }

    private String moveCodeCloseTagToNewline(String text) {
        return text.replaceAll("(.)(</code></pre>)", "$1<br />$2");
    }

    private List<String> removeUnusedCodeLine(String[] texts) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : texts) {
            wrapTextList.add(text.replaceAll("<<code class=\\\\\".+\\\\\">$", "")
                    .replaceAll("^</code>$", "")
                    .replaceAll("<b>", "")
                    .replaceAll("</b>", ""));
        }
        return wrapTextList;
    }

    private List<String> removeUnusedATagLine(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("<a name='more'></a>", ""));
        }
        return wrapTextList;
    }

    private List<String> removeBlankLine(List<String> textList) {
        ArrayList<String> newTextList = new ArrayList<>();
        for (String text : textList) {
            if (!text.trim().isEmpty()) {
                newTextList.add(text.trim());
            }
        }
        return newTextList;
    }

    private List<String> wrapImageTag(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("<a.+?href\\s*=\\s*\"(.+?)\".*><img.+?src\\s*=\\s*\"(.+?)\".*/></a>", "<a:$1><img:$2>"));
        }
        return wrapTextList;
    }

    private List<String> wrapLinkTag(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("<a.+href=\\\\?[\\'\"]?([^\\'\" >]+)\".+\\\">(.+)</a>", "<a:$1>$2</a>"));
        }
        return wrapTextList;
    }

    private List<String> wrapTextColor(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("<span\\b[^>]*color: (#[a-z0-9]{3,8})\\b[^>]*>(.*?)</span>", "<color:$1>$2</color>"));
        }
        return wrapTextList;
    }

    private List<String> wrapCode(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("<pre.+<code.+language-(.+)\".*>", "<code:$1>")
                    .replaceAll("</code></pre>", ""));
        }
        return wrapTextList;
    }

    public boolean isPlainText(String text) {
        return !isCode(text) && !isImage(text) && !isHeaderText(text);
    }

    public boolean isVideo(String text) {
        return isMatcher(text, "<iframe.+>");
    }

    public boolean isHeaderText(String text) {
        return isMatcher(text, "<h\\d>.+</h\\d>");
    }

    public boolean isCode(String text) {
        return isMatcher(text, "<code:.+>");
    }

    public boolean isImage(String text) {
        return isMatcher(text, "<a:.+><img:.+>");
    }

    public boolean isContainLink(String text) {
        return isMatcher(text, "<a:.+>");
    }

    public boolean isContainColor(String text) {
        return isMatcher(text, "<color:.+>.+</color>");
    }

    private boolean isMatcher(String text, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public List<BasePost> convertPost(String rawContent) {
        List<BasePost> listPost = new ArrayList<>();
        List<String> textList = ContentUtility.getInstance().wrapContent(rawContent);
        for (String text : textList) {
            if (ContentUtility.getInstance().isCode(text)) {
                listPost.add(ContentUtility.getInstance().convertCodePost(text).setType(PostType.CODE));
            } else if (ContentUtility.getInstance().isImage(text)) {
                listPost.add(ContentUtility.getInstance().convertImagePost(text).setType(PostType.IMAGE));
            } else if (ContentUtility.getInstance().isHeaderText(text)) {
                listPost.add(ContentUtility.getInstance().convertHeaderPost(text).setType(PostType.HEADER));
            } else if (ContentUtility.getInstance().isVideo(text)) {
                listPost.add(ContentUtility.getInstance().convertVideoPost(text).setType(PostType.VIDEO));
            } else {
                listPost.add(ContentUtility.getInstance().convertPlainText("    " + text).setType(PostType.PLAIN_TEXT));
            }
        }
        return listPost;
    }

    public ImagePost convertImagePost(String imageContent) {
        Matcher matcher = getMatcher(imageContent, "<a:(.+)><img:(.+)>");
        if (matcher.find()) {
            return new ImagePost(matcher.group(1), matcher.group(2));
        }
        return null;
    }

    public PlainTextPost convertPlainText(String plainText) {
        List<PlainTextPost.Highlight> highlightList = convertPlainTextHighLight(plainText);
        List<PlainTextPost.Link> linkList = convertPlainTextLink(plainText);
        plainText = plainText.replaceAll("<color:#[a-z0-9]{3,8}>(.*?)</color>", "$1");
        plainText = plainText.replaceAll("<a:.+?>(.*?)</a>", "$1");
        return new PlainTextPost(plainText, highlightList, linkList);
    }

    private List<PlainTextPost.Highlight> convertPlainTextHighLight(String plainText) {
        int codeCount = 0;
        List<PlainTextPost.Highlight> highlightList = new ArrayList<>();
        Matcher matcher = getMatcher(plainText, "(<color:(#[a-z0-9]{3,8})>)(.*?)(</color>)");
        while (matcher.find()) {
            int openCodeSize = matcher.group(1).length();
            int closeCodeSize = matcher.group(4).length();
            int start = matcher.start() - codeCount;
            int end = matcher.end() - (codeCount + openCodeSize + closeCodeSize);
            codeCount += openCodeSize + closeCodeSize;
            int color = Color.parseColor(matcher.group(2));
            PlainTextPost.Highlight highlight = new PlainTextPost.Highlight(start, end, color);
            highlightList.add(highlight);
        }
        return highlightList;
    }

    private List<PlainTextPost.Link> convertPlainTextLink(String plainText) {
        int codeCount = 0;
        List<PlainTextPost.Link> linkList = new ArrayList<>();
        Matcher matcher = getMatcher(plainText, "(<a:(.+?)>)(.*?)(</a>)");
        while (matcher.find()) {
            int openCodeSize = matcher.group(1).length();
            int closeCodeSize = matcher.group(4).length();
            int start = matcher.start() - codeCount;
            int end = matcher.end() - (codeCount + openCodeSize + closeCodeSize);
            codeCount += openCodeSize + closeCodeSize;
            String url = matcher.group(2);
            PlainTextPost.Link link = new PlainTextPost.Link(start, end, url);
            linkList.add(link);
        }
        return linkList;
    }

    public VideoPost convertVideoPost(String videoContent) {
        Matcher matcher = getMatcher(videoContent, "<iframe.*?src=\"//www.youtube.com/embed/(.*?)\".*");
        if (matcher.find()) {
            String url = "https://www.youtube.com/watch?v=" + matcher.group(1).replaceAll("\\?.*", "");
            return new VideoPost(url).setVideoType(VideoPost.TYPE_YOUTUBE);
        }
        matcher = getMatcher(videoContent, "<iframe.*?src=\".*?player.vimeo.com/video/(.*?)\".*");
        if (matcher.find()) {
            String url = "https://vimeo.com/" + matcher.group(1).replaceAll("\\?.*", "");
            return new VideoPost(url).setVideoType(VideoPost.TYPE_VIMEO);
        }
        matcher = getMatcher(videoContent, "<iframe.*?src=\"(.*?)\"");
        if (matcher.find()) {
            String url = matcher.group(1);
            if (url.startsWith("//")) {
                url = "http:" + url;
            }
            return new VideoPost(url).setVideoType(VideoPost.TYPE_OTHER);
        }
        return null;
    }

    public HeaderPost convertHeaderPost(String headerContent) {
        Matcher matcher = getMatcher(headerContent, "<h(\\d)>(.+)</h\\d>");
        if (matcher.find()) {
            return new HeaderPost(Integer.parseInt(matcher.group(1)), matcher.group(2));
        }
        return null;
    }

    public CodePost convertCodePost(String headerContent) {
        Matcher matcher = getMatcher(headerContent, "<code:(java|markup)>");
        if (matcher.find()) {
            return new CodePost(headerContent.replaceAll("<code:(java|markup)>", ""), matcher.group(1));
        }
        return null;
    }

    public Matcher getMatcher(String text, String regExPattern) {
        Pattern pattern = Pattern.compile(regExPattern);
        return pattern.matcher(text);
    }

    public String removeLabelFromTitle(String title) {
        return title.replaceAll("^\\[.*?\\]", "").trim();
    }
}
