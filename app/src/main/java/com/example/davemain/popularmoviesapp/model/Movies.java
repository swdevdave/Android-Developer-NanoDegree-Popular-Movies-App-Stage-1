package com.example.davemain.popularmoviesapp.model;

public class Movies {

    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mUserRating;
    private String mThumbnailImgUrl;
    private String mPosterUrl;

    public Movies() {
    }


// --Commented out by Inspection START (6/7/2018 9:38 PM):
//    public Movies(String mTitle, String mOverview, String mReleaseDate, String mUserRating, String mThumbnailImgUrl, String mPosterUrl) {
//        this.mTitle = mTitle;
//        this.mOverview = mOverview;
//        this.mReleaseDate = mReleaseDate;
//        this.mUserRating = mUserRating;
//        this.mThumbnailImgUrl = mThumbnailImgUrl;
//        this.mPosterUrl = mPosterUrl;
//    }
// --Commented out by Inspection STOP (6/7/2018 9:38 PM)

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public void setmUserRating(String mUserRating) {
        this.mUserRating = mUserRating;
    }

    public String getmThumbnailImgUrl() {
        return mThumbnailImgUrl;
    }

    public void setmThumbnailImgUrl(String mThumbnailImgUrl) {
        this.mThumbnailImgUrl = mThumbnailImgUrl;
    }

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    public void setmPosterUrl(String mPosterUrl) {
        this.mPosterUrl = mPosterUrl;
    }


}
