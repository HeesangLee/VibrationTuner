package dalcoms.pub.vibrationtuner.scene;

import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import dalcoms.pub.vibrationtuner.R;
import dalcoms.pub.vibrationtuner.ResourcesManager;

public class SceneManager {
//	private final String TAG = this.getClass().getSimpleName();
	private static final SceneManager instance = new SceneManager();

	private boolean flagResultInterstitialAdOn = false;

	private BaseScene currentScene;

	private BaseScene sceneHome;

	private SceneType currentSceneType;

	final int POP_AD_REPLAY = 15;

	public static SceneManager getInstance( ) {
		return instance;
	}

	public void setScene( BaseScene pScene ) {
		ResourcesManager.getInstance().getEngine().setScene( pScene );
		this.currentScene = pScene;
		this.currentSceneType = pScene.getSceneType();
	}

	public void setScene( SceneType pSceneType ) {
		switch ( pSceneType ) {

			case SCENE_HOME :
				setScene( sceneHome );
				break;
			default :
				setScene( sceneHome );
//				Log.e( TAG, "Scene creation is done with unexpected routine" );
				break;
		}
	}

	public BaseScene getCurrentScene( ) {
		return this.currentScene;
	}

	public SceneType getCurrentSceneType( ) {
		return this.currentSceneType;
	}

	public void createSceneHome( OnCreateSceneCallback pOnCreateSceneCallback ) {
		this.sceneHome = new SceneHome();
		this.currentScene = this.sceneHome;
		this.currentSceneType = this.sceneHome.getSceneType();

		pOnCreateSceneCallback.onCreateSceneFinished( this.currentScene );
	}

	public void createSceneHome( ) {
		this.sceneHome = new SceneHome();
		this.clearScene( this.currentSceneType );
		this.setScene( this.sceneHome );
	}

	private void clearScene( SceneType pSceneType ) {
		switch ( pSceneType ) {
			case SCENE_HOME :
				this.disposeSceneHome();
				break;
			default :
//				Log.v( "Dispose Scene Error", "Some Scene selection is not correct" );
				break;
		}
	}

	private void disposeSceneHome( ) {
		this.sceneHome.disposeScene();
		this.sceneHome = null;
	}

	private void displayAdmobInterstitialAd( InterstitialAd pAd ) {
		if ( pAd.isLoaded() ) {
			pAd.show();
		}
	}

	public void popAdmobInterstitialAd( ) {
		final InterstitialAd adMobInterstitialAd = new InterstitialAd( ResourcesManager.getInstance()
				.getActivity() );
		adMobInterstitialAd.setAdUnitId( ResourcesManager.getInstance().getActivity()
				.getString( R.string.admob_interstitial_id ) );
		final AdRequest adRequest = new AdRequest.Builder().build();
		ResourcesManager.getInstance().getActivity().runOnUiThread( new Runnable() {

			@Override
			public void run( ) {
				adMobInterstitialAd.loadAd( adRequest );
			}
		} );

		adMobInterstitialAd.setAdListener( new AdListener() {
			public void onAdLoaded( ) {
				displayAdmobInterstitialAd( adMobInterstitialAd );
			}
		} );
	}

	public boolean isResultInterstitialAdOn( ) {
		return flagResultInterstitialAdOn;
	}
}
