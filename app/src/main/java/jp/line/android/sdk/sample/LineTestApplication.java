package jp.line.android.sdk.sample;

import android.app.Application;

import jp.line.android.sdk.LineSdkContextManager;

/**
 * The Application instance. The Line SDK should be initialized here to allow
 * it to function correctly throughout the lifetime of the app, including
 * configuration changes, etc.
 */
public class LineTestApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		LineSdkContextManager.initialize(this);
	}
}
