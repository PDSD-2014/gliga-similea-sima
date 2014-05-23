package com.example.Videoger;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class FullScreenVideo {
	Context mContext;
	FullScreenVideo(Context c) {
        mContext = c;
    }
	@JavascriptInterface
	public void play(String video) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(video));
		mContext.startActivity(i);
	}
	@JavascriptInterface
	public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
