package com.akexorcist.sleepingforless.ui;

import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.database.BookmarkManager;
import com.akexorcist.sleepingforless.network.blogger.model.Post;
import com.akexorcist.sleepingforless.view.bookmark.BookmarkActivity;
import com.akexorcist.sleepingforless.view.offline.OfflinePostActivity;
import com.robotium.solo.Solo;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Akexorcist on 4/17/2016 AD.
 */
public class BookmarkActivityTest extends ActivityInstrumentationTestCase2 {
    private static Class targetActivity = BookmarkActivity.class;
    private static String ACTIVITY_NAME = targetActivity.getName();

    private Solo solo;

    private static Class<?> launcherActivityClass;

    static {
        try {
            launcherActivityClass = Class.forName(targetActivity.getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public BookmarkActivityTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getActivity().getCacheDir().getPath());
        MockitoAnnotations.initMocks(this);
        BookmarkManager.getInstance().clearBookmark();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
        BookmarkManager.getInstance().clearBookmark();
    }

    public void testAddBookmark() {
        solo.waitForActivity(ACTIVITY_NAME, 1000);
        int bookmarkAddedCount = 5;
        final BookmarkActivity bookmarkActivity = (BookmarkActivity) getActivity();
        RecyclerView rvBookmark = (RecyclerView) solo.getView(R.id.rv_bookmark_list);
        for (int i = 0; i < bookmarkAddedCount; i++) {
            Post post = mockPost(i);
            BookmarkManager.getInstance().addBookmark(post);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bookmarkActivity.callDatabase();
                }
            });
        }
        solo.sleep(100);
        int bookmarkCount = rvBookmark.getAdapter().getItemCount();
        assertEquals(bookmarkAddedCount, bookmarkCount);
    }

    public void testRemoveAllDatabaseWithCancel() {
        solo.waitForActivity(ACTIVITY_NAME, 1000);
        int bookmarkAddedCount = 5;
        final BookmarkActivity bookmarkActivity = (BookmarkActivity) getActivity();
        RecyclerView rvBookmark = (RecyclerView) solo.getView(R.id.rv_bookmark_list);
        View fabMenu = solo.getView(R.id.fab_menu);
        View btnRemoveAll = solo.getView(R.id.btn_menu_remove_all);
        View tvEmptyBookmark = solo.getView(R.id.tv_bookmark_not_found);
        for (int i = 0; i < bookmarkAddedCount; i++) {
            Post post = mockPost(i);
            BookmarkManager.getInstance().addBookmark(post);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bookmarkActivity.callDatabase();
            }
        });
        solo.sleep(100);
        solo.clickOnView(fabMenu);
        solo.clickOnView(btnRemoveAll);
        solo.waitForText(getActivity().getString(R.string.remove_confirm_no), 1, 2000);
        solo.clickOnText(getActivity().getString(R.string.remove_confirm_no));
        solo.sleep(100);
        int bookmarkCount = rvBookmark.getAdapter().getItemCount();
        assertEquals(bookmarkAddedCount, bookmarkCount);
        assertEquals(View.GONE, tvEmptyBookmark.getVisibility());
    }

    public void testRemoveAllDatabaseWithConfirm() {
        solo.waitForActivity(ACTIVITY_NAME, 1000);
        int bookmarkAddedCount = 5;
        final BookmarkActivity bookmarkActivity = (BookmarkActivity) getActivity();
        RecyclerView rvBookmark = (RecyclerView) solo.getView(R.id.rv_bookmark_list);
        View fabMenu = solo.getView(R.id.fab_menu);
        View btnRemoveAll = solo.getView(R.id.btn_menu_remove_all);
        View tvEmptyBookmark = solo.getView(R.id.tv_bookmark_not_found);
        for (int i = 0; i < bookmarkAddedCount; i++) {
            Post post = mockPost(i);
            BookmarkManager.getInstance().addBookmark(post);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bookmarkActivity.callDatabase();
            }
        });
        solo.sleep(100);
        solo.clickOnView(fabMenu);
        solo.clickOnView(btnRemoveAll);
        solo.waitForText(getActivity().getString(R.string.remove_confirm_yes), 1, 2000);
        solo.clickOnText(getActivity().getString(R.string.remove_confirm_yes));
        solo.sleep(100);
        int bookmarkCount = rvBookmark.getAdapter().getItemCount();
        assertEquals(0, bookmarkCount);
        assertEquals(View.VISIBLE, tvEmptyBookmark.getVisibility());
    }

    public void testClickOnBookmark() {
        solo.waitForActivity(ACTIVITY_NAME, 1000);
        final BookmarkActivity bookmarkActivity = (BookmarkActivity) getActivity();
        Post post = mockPost(0);
        BookmarkManager.getInstance().addBookmark(post);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bookmarkActivity.callDatabase();
            }
        });
        solo.sleep(100);
        solo.clickInRecyclerView(0);
        solo.waitForActivity(OfflinePostActivity.class, 1000);
        solo.sleep(200);
        assertEquals(OfflinePostActivity.class.getSimpleName(), solo.getCurrentActivity().getClass().getSimpleName());
    }

    public Post mockPost(int index) {
        Post.Author author = Mockito.mock(Post.Author.class);
        Mockito.when(author.getDisplayName()).thenReturn("Test Author Display Name " + index);
        Mockito.when(author.getId()).thenReturn("Test Author ID " + index);
        Mockito.when(author.getUrl()).thenReturn("Test Author URL " + index);
        Post.Blog blog = Mockito.mock(Post.Blog.class);
        Mockito.when(blog.getId()).thenReturn("Test Blog ID " + index);
        List<String> labels = Arrays.asList("Test Label 1", "Test Label 2");
        Post.Reply reply = Mockito.mock(Post.Reply.class);
        Mockito.when(reply.getSelfLink()).thenReturn("Test Self Link " + index);
        Mockito.when(reply.getTotalItems()).thenReturn("Test Total Item " + index);
        Post post = Mockito.mock(Post.class);
        Mockito.when(post.getTitle()).thenReturn("Test Title " + index);
        Mockito.when(post.getId()).thenReturn("Test ID " + index);
        Mockito.when(post.getContent()).thenReturn("Test Content " + index);
        Mockito.when(post.getEtag()).thenReturn("Test ETag " + index);
        Mockito.when(post.getKind()).thenReturn("Test Kind " + index);
        Mockito.when(post.getSelfLink()).thenReturn("Test Self Link " + index);
        Mockito.when(post.getPublished()).thenReturn("Test Published Date " + index);
        Mockito.when(post.getUpdated()).thenReturn("Test Updated Date " + index);
        Mockito.when(post.getUrl()).thenReturn("Test URL " + index);
        Mockito.when(post.getReplies()).thenReturn(reply);
        Mockito.when(post.getLabels()).thenReturn(labels);
        Mockito.when(post.getAuthor()).thenReturn(author);
        Mockito.when(post.getBlog()).thenReturn(blog);
        return post;
    }


    // TODO For Instrumentation Test
//    public void setup() {
//        File tempFolder = new File(getActivity().getCacheDir().getAbsoluteFile() + "/mockRealm");
//        tempFolder.mkdir();
//        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
//        Realm.setDefaultConfiguration(config);
//    }

//    public void testAddBookmark() {
//        Post post = mockPost();
//        BookmarkManager.getInstance().addBookmark(post);
//        int bookmarkCount = BookmarkManager.getInstance().getBookmarkList().size();
//        assertEquals(1, bookmarkCount);
//    }

//    public void testRemoveBookmark() {
//        Post post = mockPost();
//        BookmarkManager.getInstance().addBookmark(post);
//        int bookmarkCount = BookmarkManager.getInstance().getBookmarkList().size();
//        assertEquals(1, bookmarkCount);
//        BookmarkManager.getInstance().removeBookmark(post.getId());
//        bookmarkCount = BookmarkManager.getInstance().getBookmarkList().size();
//        assertEquals(0, bookmarkCount);
//    }

}
