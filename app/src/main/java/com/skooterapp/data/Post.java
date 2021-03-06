package com.skooterapp.data;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.skooterapp.AppController;
import com.skooterapp.BaseActivity;
import com.skooterapp.R;
import com.skooterapp.SkooterJsonObjectRequest;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String LOG_TAG = Post.class.getSimpleName();

    protected static final String SKOOT_ID = "id";
    protected static final String SKOOT_POST = "content";
    protected static final String SKOOT_HANDLE = "channel";
    protected static final String SKOOT_UPVOTES = "upvotes";
    protected static final String SKOOT_DOWNVOTES = "downvotes";
    protected static final String SKOOT_IF_USER_VOTED = "user_voted";
    protected static final String SKOOT_USER_VOTE = "user_vote";
    protected static final String SKOOT_USER_SCOOT = "user_skoot";
    protected static final String SKOOT_CREATED_AT = "created_at";
    protected static final String SKOOT_COMMENTS_COUNT = "comments_count";
    protected static final String SKOOT_FAVORITE_COUNT = "favorites_count";
    protected static final String SKOOT_USER_FAVORITED = "user_favorited";
    protected static final String SKOOT_USER_COMMENTED = "user_commented";
    protected static final String SKOOT_IMAGE_URL = "zone_image";
    protected static final String SKOOT_IMAGE_PRESENT = "image_present";
    protected static final String SKOOT_SMALL_IMAGE_URL = "small_image_url";
    protected static final String SKOOT_LARGE_IMAGE_URL = "large_image_url";
    protected static final String SKOOT_IMAGE_RESOLUTION = "image_resolution";
    protected static final String SKOOT_IMAGE_WIDTH = "width";
    protected static final String SKOOT_IMAGE_HEIGHT = "height";

    private int mId;
    private String mChannel;
    private String mContent;
    private int mUpvotes;
    private int mDownvotes;
    private int mCommentsCount;
    private int mFavoriteCount;
    private boolean mIfUserVoted;
    private boolean mUserVote;
    private boolean mUserFavorited;
    private boolean mUserCommented;
    private boolean mUserSkoot;
    private String mImageUrl;
    private boolean mImagePresent;
    private String mSmallImageUrl;
    private String mLargeImageUrl;
    private int mWidth;
    private int mHeight;
    private String mTimestamp;

    public boolean isImagePresent() {
        return mImagePresent;
    }

    public void setImagePresent(boolean imagePresent) {
        mImagePresent = imagePresent;
    }

    public String getSmallImageUrl() {
        return mSmallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        mSmallImageUrl = smallImageUrl;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        mLargeImageUrl = largeImageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public int getId() {
        return mId;
    }

    public String getChannel() {
        return mChannel;
    }

    public String getContent() {
        return mContent;
    }

    public int getUpvotes() {
        return mUpvotes;
    }

    public int getDownvotes() {
        return mDownvotes;
    }

    public boolean isIfUserVoted() {
        return mIfUserVoted;
    }

    public boolean isUserVote() {
        return mUserVote;
    }

    public boolean isUserSkoot() {
        return mUserSkoot;
    }

    public String getTimestamp() {
        return BaseActivity.getTimeAgo(mTimestamp);
    }

    public boolean isUserCommented() {
        return mUserCommented;
    }

    public void setUserCommented(boolean userCommented) {
        mUserCommented = userCommented;
    }

    public boolean isUserFavorited() {
        return mUserFavorited;
    }

    public void setUserFavorited(boolean userFavorited) {
        mUserFavorited = userFavorited;
    }

    public int getFavoriteCount() {
        return mFavoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        mFavoriteCount = favoriteCount;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public Post(int id, String channel, String content, int upvotes, int downvotes, int commentsCount, int favoriteCount, boolean ifUserVoted, boolean userVote, boolean userFavorited, boolean userCommented, boolean userSkoot, String imageUrl, boolean imagePresent, String smallImageUrl, String largeImageUrl, int width, int height, String timestamp) {
        mId = id;
        mChannel = channel;
        mContent = content;
        mUpvotes = upvotes;
        mDownvotes = downvotes;
        mCommentsCount = commentsCount;
        mFavoriteCount = favoriteCount;
        mIfUserVoted = ifUserVoted;
        mUserVote = userVote;
        mUserFavorited = userFavorited;
        mUserCommented = userCommented;
        mUserSkoot = userSkoot;
        mImageUrl = imageUrl;
        mImagePresent = imagePresent;
        mSmallImageUrl = smallImageUrl;
        mLargeImageUrl = largeImageUrl;
        mWidth = width;
        mHeight = height;
        mTimestamp = timestamp;
    }

    @Override
    public String toString() {
        return mContent + "\n" +
                (mUpvotes - mDownvotes) + "\n" +
                mTimestamp;
    }

    public int getVoteCount() {
        return mUpvotes - mDownvotes;
    }

    public void upvotePost() {
        if(mIfUserVoted) {
            if(!mUserVote) {
                //Upvotes + 2
                mUpvotes += 1;
                mDownvotes -= 1;
            } else {
                return;
            }
        } else {
            //Upvotes + 1
            mUpvotes += 1;
        }
        mUserVote = true;
        mIfUserVoted = true;
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.vote_skoot), params);

        params = new HashMap<String, String>();
        params.put("user_id", Integer.toString(BaseActivity.userId));
        params.put("vote", "true");
        params.put("location_id", Integer.toString(BaseActivity.locationId));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "Upvote: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.put("user_id", Integer.toString(BaseActivity.userId));
                headers.put("access_token", BaseActivity.accessToken);

                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "upvote_post");
    }

    public void downvotePost() {
        if(mIfUserVoted) {
            if(mUserVote) {
                //Downvote - 2
                mUpvotes -= 1;
                mDownvotes += 1;
            } else {
                return;
            }
        } else {
            //Downvote - 1
            mDownvotes += 1;
        }
        mUserVote = false;
        mIfUserVoted = true;
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.vote_skoot), params);

        params = new HashMap<String, String>();
        params.put("user_id", Integer.toString(BaseActivity.userId));
        params.put("vote", "false");
        params.put("location_id", Integer.toString(BaseActivity.locationId));

        SkooterJsonObjectRequest jsonObjectRequest = new SkooterJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "Downvote: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "downvote_post");
    }

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        mCommentsCount = commentsCount;
    }

    public void favoritePost() {
        setUserFavorited(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.favorite_post), params);

        params = new HashMap<String, String>();
        params.put("user_id", Integer.toString(BaseActivity.userId));

        SkooterJsonObjectRequest jsonObjectRequest = new SkooterJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "Follow: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "favorite_post");
        mFavoriteCount += 1;
    }

    public void unFavoritePost() {
        setUserFavorited(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.unfavorite_post), params);

        SkooterJsonObjectRequest jsonObjectRequest = new SkooterJsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "UnFollow: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "unfavorite_post");
        mFavoriteCount -= 1;
    }

    public static Post parsePostFromJSONObject(JSONObject response) {
        try {
            int id = response.getInt(SKOOT_ID);
            String post = response.getString(SKOOT_POST);

            String channel = "";
            if (!response.isNull(SKOOT_HANDLE)) {
                channel = "@" + response.getString(SKOOT_HANDLE);
            }

            int upvotes = response.getInt(SKOOT_UPVOTES);
            int commentsCount = response.getInt(SKOOT_COMMENTS_COUNT);
            int downvotes = response.getInt(SKOOT_DOWNVOTES);
            boolean skoot_if_user_voted = response.getBoolean(SKOOT_IF_USER_VOTED);
            boolean user_vote = response.getBoolean(SKOOT_USER_VOTE);
            boolean user_skoot = response.getBoolean(SKOOT_USER_SCOOT);
            boolean user_favorited = response.getBoolean(SKOOT_USER_FAVORITED);
            boolean user_commented = response.getBoolean(SKOOT_USER_COMMENTED);
            int favoriteCount = response.getInt(SKOOT_FAVORITE_COUNT);
            String created_at = response.getString(SKOOT_CREATED_AT);
            String image_url = response.getString(SKOOT_IMAGE_URL);
            boolean isImagePresent = response.getBoolean(SKOOT_IMAGE_PRESENT);
            String small_image_url = response.getString(SKOOT_SMALL_IMAGE_URL);
            String large_image_url = response.getString(SKOOT_LARGE_IMAGE_URL);
            JSONObject image_resolution = response.getJSONObject(SKOOT_IMAGE_RESOLUTION);
            int width = image_resolution.getInt(SKOOT_IMAGE_WIDTH);
            int height = image_resolution.getInt(SKOOT_IMAGE_HEIGHT);

            return new Post(id, channel, post, upvotes, downvotes, commentsCount, favoriteCount,  skoot_if_user_voted, user_vote, user_favorited, user_commented, user_skoot,  image_url, isImagePresent, small_image_url, large_image_url, width, height, created_at);
        } catch (Exception e) {
            return null;
        }
    }

    public static int findPostPositionInListById(List<Post> postList, int postId) {
        int i = 0;
        for(Post post: postList) {
            if(post.getId() == postId)
            {
                return i;
            }
            i++;
        }
        return -1;
    }
}
