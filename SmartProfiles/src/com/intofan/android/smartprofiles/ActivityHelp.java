package com.intofan.android.smartprofiles;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ActivityHelp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		WebView webview = new WebView(this);
		webview.getSettings().setJavaScriptEnabled(true);
		setContentView(webview);
		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setProgress(progress * 100);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				UtilAlert.toast(getApplicationContext(), description, 1);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					if (url != null) {
						if (url.startsWith("market://")
								|| url.startsWith("http://")
								|| url.startsWith("https://")) {
							view.getContext().startActivity(
									new Intent(Intent.ACTION_VIEW, Uri
											.parse(url)));
							return true;
						} else if (url.startsWith("mailto:")) {
							final Intent emailIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							emailIntent.setType("plain/text");
							emailIntent.putExtra(
									android.content.Intent.EXTRA_EMAIL,
									new String[] { "intofansoft@gmail.com" });
							emailIntent.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									"Smarter Profiles Feedback");
							startActivity(Intent.createChooser(emailIntent,
									"Send E-mail.."));
							return true;
						}
						return false;
					}
					return false;
				} catch (Exception e) {
					UtilAlert.alert(activity, getString(R.string.error),
							e.toString());
				}
				return false;
			}
		});
		webview.loadUrl("http://intofan.com/smarterprofiles/help.html");
	}
}
