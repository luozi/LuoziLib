package com.luozi.lib.helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;

public class FragmentHelper {

	private FragmentManager mFragmentManager;
	private HashMap<String, FragmentInfo> mFragmentItems = new HashMap<String, FragmentInfo>();
	private FragmentInfo mShowingInfo;
	private int mFrameRes;

	public static class FragmentInfo {
		protected Context context;
		protected String tag;
		protected Class<?> cls;
		protected Bundle args;
		protected Fragment fragment;

		public FragmentInfo(Context context, String tag, Class<?> cls) {
			this(context, tag, cls, null);
		}

		public FragmentInfo(Context context, String tag, Class<?> cls, Bundle args) {
			this.context = context;
			this.tag = tag;
			this.cls = cls;
			this.args = args;
		}

		/**
		 * Get tag.
		 */
		public String getTag() {
			return tag;
		}
	}

	public FragmentHelper(FragmentManager fragmentManager, int frameRes) {
		this.mFragmentManager = fragmentManager;
		this.mFrameRes = frameRes;

		mFragmentItems.clear();
	}

	/**
	 * Add fragment info for map.
	 */
	public void addFragmentItem(FragmentInfo info) {
		mFragmentItems.put(info.getTag(), info);
	}

	/**
	 * Show fragment by tag.
	 */
	public void show(String tag) {
		show(mFragmentItems.get(tag));
	}

	/**
	 * Get fragment by tag.
	 */
	public Fragment get(String tag) {
		if (!mFragmentItems.containsKey(tag)) {
			return null;
		}
		return mFragmentItems.get(tag).fragment;
	}

	/**
	 * Show fragment by tag and set bundle.
	 */
	public void show(String tag, Bundle args) {
		FragmentInfo info = mFragmentItems.get(tag);
		info.args = args;
		show(info);
	}

	/**
	 * Judge the tag's fragment is showing.
	 */
	public boolean isShowing(String tag) {
		if (mShowingInfo.tag.equals(tag)) {
			return true;
		}
		return false;
	}

	/**
	 * Show fragment by info.
	 */
	public void show(FragmentInfo info) {

		final FragmentTransaction trans = mFragmentManager.beginTransaction()
				.disallowAddToBackStack();

		if (mShowingInfo == info) {
			// Fragment is showing, don't try
			// again
		} else {
			if (mShowingInfo != null) {
				// Detach showing fragment
				if (mShowingInfo.fragment != null) {
					trans.hide(mShowingInfo.fragment);
				}
			}
			mShowingInfo = info;
			if (mShowingInfo != null) {
				// Attach init fragment
				if (mShowingInfo.fragment == null) {
					mShowingInfo.fragment = Fragment.instantiate(mShowingInfo.context,
							mShowingInfo.cls.getName(), mShowingInfo.args);
					trans.add(mFrameRes, mShowingInfo.fragment, mShowingInfo.tag);
				} else {
					trans.show(mShowingInfo.fragment);
				}
			}
		}

		if (!trans.isEmpty()) {
			trans.commit();
		}
	}
}
