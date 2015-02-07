package net.aayush.skooterapp.data;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import net.aayush.skooterapp.AppController;
import net.aayush.skooterapp.BaseActivity;
import net.aayush.skooterapp.R;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String LOG_TAG = Post.class.getSimpleName();

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

    private String mTimestamp;

    public Post(int id, String channel, String content, int commentsCount, int favoriteCount, int upvotes, int downvotes, boolean ifUserVoted, boolean userVote, boolean userSkoot, boolean userFavorited, boolean userCommented, String timestamp, String imageUrl) {
        mId = id;
        mChannel = channel;
        mContent = content;
        mCommentsCount = commentsCount;
        mFavoriteCount = favoriteCount;
        mUserFavorited = userFavorited;
        mUserCommented = userCommented;
        mUpvotes = upvotes;
        mDownvotes = downvotes;
        mIfUserVoted = ifUserVoted;
        mUserVote = userVote;
        mUserSkoot = userSkoot;
        mTimestamp = timestamp;
        mImageUrl = imageUrl;
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
                mUserVote = true;
                mIfUserVoted = true;
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.vote_skoot), params);

        params = new HashMap<String, String>();
        params.put("user_id", Integer.toString(BaseActivity.userId));
        params.put("vote", "false");
        params.put("location_id", Integer.toString(BaseActivity.locationId));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "Downvote: " + response.toString());
                mUserVote = false;
                mIfUserVoted = true;
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

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "downvote_post");
    }

    public int getCommentsCount() {
        return mCommentsCount;
    }

    public void favoritePost() {
        setUserFavorited(true);
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.favorite_post), params);

        params = new HashMap<String, String>();
        params.put("user_id", Integer.toString(BaseActivity.userId));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "Follow: " + response.toString());
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

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "favorite_post");
    }

    public void unFavoritePost() {
        setUserFavorited(false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("post_id", Integer.toString(getId()));

        String url = BaseActivity.substituteString(AppController.getInstance().getResources().getString(R.string.unfavorite_post), params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "UnFollow: " + response.toString());
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

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "unfavorite_post");
    }
}
