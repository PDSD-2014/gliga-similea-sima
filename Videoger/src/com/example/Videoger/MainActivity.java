package com.example.Videoger;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.Videoger.R;

@SuppressLint("JavascriptInterface")
public class MainActivity extends Activity {
	WebView web;

	private ValueCallback<Uri> mUploadMessage;  
	 private final static int FILECHOOSER_RESULTCODE=1;  
	 static final int REQUEST_VIDEO_CAPTURE = 2;

	 private void dispatchTakeVideoIntent() {
	     Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	     
	     File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
	     
	     Uri videoUri = Uri.fromFile(mediaFile);
	     takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
	     
	     if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
	         startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
	     }
	 }

	 @Override  
	 protected void onActivityResult(int requestCode, int resultCode,  
	                                    Intent intent) {  
	  if(requestCode==FILECHOOSER_RESULTCODE)  
	  {  
	   if (null == mUploadMessage) return;  
	            Uri result = intent == null || resultCode != RESULT_OK ? null  
	                    : intent.getData();  
	            mUploadMessage.onReceiveValue(result);  
	            mUploadMessage = null;  
	  }
	  
	  if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	        Uri result = intent.getData();
	        mUploadMessage.onReceiveValue(result);
	    }
	  }  

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    web = (WebView) findViewById(R.id.webview);
	   
	    web = new WebView(this);  
	    web.getSettings().setJavaScriptEnabled(true);
//	    web.loadUrl("http://192.168.0.103:3000");
	    web.loadUrl("http://hidden-hamlet-4327.herokuapp.com");
	    web.setWebViewClient(new myWebClient());
	    web.addJavascriptInterface(new FullScreenVideo(this), "Android");
	    web.setWebChromeClient(new WebChromeClient()  
	    {  
	           //The undocumented magic method override  
	           //Eclipse will swear at you if you try to put @Override here  
	        // For Android 3.0+
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

	            mUploadMessage = uploadMsg;  
	            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
	            i.addCategory(Intent.CATEGORY_OPENABLE);  
	            i.setType("image/*");  
	            MainActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

	           }

	        // For Android 3.0+
	           public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
	           mUploadMessage = uploadMsg;
	           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	           i.addCategory(Intent.CATEGORY_OPENABLE);
	           i.setType("*/*");
	           MainActivity.this.startActivityForResult(
	           Intent.createChooser(i, "File Browser"),
	           FILECHOOSER_RESULTCODE);
	           }

	        //For Android 4.1
	           public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
	               mUploadMessage = uploadMsg;  
//	               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
//	               i.addCategory(Intent.CATEGORY_OPENABLE);  
//	               i.setType("*/*");  
//	               MainActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), MainActivity.FILECHOOSER_RESULTCODE );
	               dispatchTakeVideoIntent();
//	               MainActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), MainActivity.REQUEST_VIDEO_CAPTURE );
	           }

	    });  


	    setContentView(web);  
	}

	public class myWebClient extends WebViewClient
	{
	    @Override
	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        // TODO Auto-generated method stub
	        super.onPageStarted(view, url, favicon);
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // TODO Auto-generated method stub
	    	if (Uri.parse(url).getHost().contains("amazon")) {
	    		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            startActivity(intent);
	    	} else {
	        view.loadUrl(url);
	    	}
	        return true;

	    }

	    @Override
	    public void onPageFinished(WebView view, String url) {
	        // TODO Auto-generated method stub
	        super.onPageFinished(view, url);

	        
	    }
	}

	//flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
	        web.goBack();
	        return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
}
