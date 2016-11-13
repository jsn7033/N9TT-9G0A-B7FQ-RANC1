package com.callback;

/**
 * Created by Sunny on 11/13/2016.
 */

public class RefreshHomeFragmentCallback {

    static RefreshHomeFragmentCallback mRefreshHomeFragmentCallback;
    RefreshHomeFragmentCl mRefreshHomeFragment;

    public interface RefreshHomeFragmentCl {
        void refreshFragment();
    }

    public static RefreshHomeFragmentCallback getInstance() {
        if (mRefreshHomeFragmentCallback == null) {
            mRefreshHomeFragmentCallback = new RefreshHomeFragmentCallback();
        }
        return mRefreshHomeFragmentCallback;
    }

    public void setListener(RefreshHomeFragmentCl mRefreshHomeFragment) {
        this.mRefreshHomeFragment = mRefreshHomeFragment;
    }

    public void notiyy() {
        if (mRefreshHomeFragment != null) {
            mRefreshHomeFragment.refreshFragment();
        }
    }


}
