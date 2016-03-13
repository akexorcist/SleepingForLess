package com.akexorcist.sleepingforless.util;

import android.util.Log;

import com.akexorcist.sleepingforless.constant.TestConstant;
import com.akexorcist.sleepingforless.view.post.model.HeaderPost;
import com.akexorcist.sleepingforless.view.post.model.ImagePost;

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
        return text.replaceAll("(<h\\d>)\\n?\\n?\\r?(.+)(</h\\d>)", "$1$2$3<br />");
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
            Log.i("Check", text);
            wrapTextList.add(text.replaceAll("^<code class=\\\\\".+\\\\\">$", "")
                    .replaceAll("^</code>$", ""));
        }
        return wrapTextList;
    }

    private List<String> removeUnusedATagLine(List<String> textList) {
        ArrayList<String> wrapTextList = new ArrayList<>();
        for (String text : textList) {
            wrapTextList.add(text.replaceAll("^<a.*</a>", ""));
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
            wrapTextList.add(text.replaceAll("<span.+color:.*(#.+);\\\\?[\\'\"]?>(.+)</span>", "<color:$1>$2</color>"));
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

    public ImagePost convertImagePost(String imageContent) {
        Matcher matcher = getMatcher(imageContent, "<a:(.+)><img:(.+)>");
        if (matcher.find()) {
            return new ImagePost(matcher.group(1), matcher.group(2));
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

    public Matcher getMatcher(String text, String regExPattern) {
        Pattern pattern = Pattern.compile(regExPattern);
        return pattern.matcher(text);
    }

}
