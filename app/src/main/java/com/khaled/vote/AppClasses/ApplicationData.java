package com.khaled.vote.AppClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationData {

    private static final String FILE_NAME = "VOTEAPPFile";

    private static final String VOTE_STATUS = "vote_status";
    private static final String USER_VOTE_STATUS_PRESIDENT = "vote_president";
    private static final String USER_VOTE_STATUS_VICE = "vote_vice";
    private static final String SIGN_LOGIN = "sign_login";


    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Context mContext;

    @SuppressLint("CommitPrefEdits")
    public ApplicationData(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    public void saveVoteStatus(boolean saveVoteStatus) {
        mEditor.putBoolean(VOTE_STATUS, saveVoteStatus);
        mEditor.apply();
    }

    public void savePresStatus(boolean savePresStatus) {
        mEditor.putBoolean(USER_VOTE_STATUS_PRESIDENT, savePresStatus);
        mEditor.apply();
    }

    public void saveViceStatus(boolean saveViceStatus) {
        mEditor.putBoolean(USER_VOTE_STATUS_VICE, saveViceStatus);
        mEditor.apply();
    }

    public void saveSignLogin(boolean saveSignLogin) {
        mEditor.putBoolean(SIGN_LOGIN, saveSignLogin);
        mEditor.apply();
    }

    public boolean getSaveLogin() {
        return mSharedPreferences.getBoolean(SIGN_LOGIN, false);
    }

    public boolean getVoteState() {
        return mSharedPreferences.getBoolean(VOTE_STATUS, false);
    }

    public boolean getPresState() {
        return mSharedPreferences.getBoolean(USER_VOTE_STATUS_PRESIDENT, false);
    }

    public boolean getViceState() {
        return mSharedPreferences.getBoolean(USER_VOTE_STATUS_VICE, false);
    }


}
