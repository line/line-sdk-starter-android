package jp.line.android.sdk.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import jp.line.android.sdk.LineSdkContextManager;
import jp.line.android.sdk.api.ApiClient;
import jp.line.android.sdk.api.ApiRequestFuture;
import jp.line.android.sdk.api.ApiRequestFutureListener;
import jp.line.android.sdk.login.LineAuthManager;
import jp.line.android.sdk.login.LineLoginFuture;
import jp.line.android.sdk.login.LineLoginFutureListener;
import jp.line.android.sdk.model.Profile;

/**
 * A simple activity to display usage of the LINE SDK. The LOGIN! button
 * executes the login flow and the TRY API button retrieves the user's profile
 * and displays their name and profile picture. The MID and access token are
 * stored in {@link SharedPreferences} and could be sent to a server to log
 * a LINE user into a developer's own server.
 */
public class HomeActivity extends Activity {

	private static final Logger logger = LoggerFactory.getLogger(HomeActivity.class);

	@InjectView(R.id.profile_image)
	ImageView profileImage;

	@InjectView(R.id.profile_name)
	TextView profileName;

	private Handler mainThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.inject(this);
		mainThread = new Handler(Looper.myLooper());
	}

	@OnClick(R.id.login_button)
	public void login() {
		LineAuthManager authManager = LineSdkContextManager.getSdkContext().getAuthManager();
		authManager.login(this).addFutureListener(
				new LineLoginFutureListener() {
					@Override
					public void loginComplete(LineLoginFuture future) {
						switch (future.getProgress()) {
						case SUCCESS:
							SharedPreferences settings = getSharedPreferences("lineauth", MODE_PRIVATE);
							// Store credentials to, for example, send in a webview activity to a server.
							// Developers should not have to worry about these values if they are only calling
							// APIs from the Android application.
							settings.edit()
									.putString("mid", future.getAccessToken().mid)
									.putString("accesstoken", future.getAccessToken().accessToken)
									.putString("refreshtoken", future.getAccessToken().refreshToken)
									.putLong("expire", future.getAccessToken().expire)
									.apply();
							displayCroutonOnMainThread("Logged in!", Style.INFO);
							logger.info("Login success!");
							break;
						case CANCELED:
							displayCroutonOnMainThread("Don't cancel login!", Style.ALERT);
							logger.info("Login canceled!");
							break;
						default:
							displayCroutonOnMainThread("An error occurred. Please try again.", Style.ALERT);
							logger.info("Login error!");
							break;
						}
					}
				});
	}

	@OnClick(R.id.tryapi_button)
	public void tryApi() {
		Activity activity = this;
		ApiClient apiClient = LineSdkContextManager.getSdkContext().getApiClient();
		apiClient.getMyProfile(null).addListener(new ApiRequestFutureListener<Profile>() {
			@Override
			public void requestComplete(ApiRequestFuture<Profile> future) {
				updateProfileInfoOnMainThread(
						future.getResponseObject().displayName, future.getResponseObject().pictureUrl);
			}
		});
	}

	private void displayCroutonOnMainThread(final String text, final Style style) {
		final Activity activity = this;
		mainThread.post(new Runnable() {
			@Override
			public void run() {
				Crouton.makeText(activity, text, style).show();
			}
		});
	}

	private void updateProfileInfoOnMainThread(final String username, final String imageUrl) {
		final Activity activity = this;
		mainThread.post(new Runnable() {
			@Override
			public void run() {
				profileName.setText(username);
				Picasso.with(activity).load(imageUrl).into(profileImage);
			}
		});
	}
}
