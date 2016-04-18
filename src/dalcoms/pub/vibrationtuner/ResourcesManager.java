package dalcoms.pub.vibrationtuner;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;
import android.graphics.Point;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings( "deprecation" )
public class ResourcesManager {
	private static final ResourcesManager instance = new ResourcesManager();

	protected Engine engine;
	protected MainActivity activity;
	protected Camera camera;
	protected VertexBufferObjectManager vbom;

	protected Vibrator pVibrator;
//	static android.hardware.Camera mHardwareCamera = null;
//	private boolean mIsCameraFlashAvailable;
//	Parameters mHardwareCameraParameter;

	protected float resizeFactor = 1f;
	private final float cameraWidthRef = 1080f;

	// =======================================
	// Graphic : Atlas
	private BuildableBitmapTextureAtlas atlasNearest;
	private final Point sizeAtlasNearest = new Point( 1280, 1968 );

	private BuildableBitmapTextureAtlas atlasBilinear;
	private final Point sizeAtlasBilinear = new Point( 1280, 1968 );

	private BuildableBitmapTextureAtlas atlasNearestPremultiplyAlpha;
	private final Point sizeAtlasNearestPremultiplyAlpha = new Point( 1280, 1968 );

	private BuildableBitmapTextureAtlas atlasBilinearPremultiplyAlpha;
	private final Point sizeAtlasBilineaPremultiplyAlphar = new Point( 1280, 1968 );
	// Regions
	public ITiledTextureRegion regionMarketShareStar;
	public ITiledTextureRegion regionOnOffIcon;
	public ITiledTextureRegion regionLightOnEffect;

	// Sound
	private static final int FONT_SIZE_DEFAULT = 64;
	private static final int FONT_SIZE_BUTTON = 48;
	private Font fontDefault;
	private Font fontButton;

	private Sound soundIntro;

	// =======================================

	public static ResourcesManager getInstance( ) {
		return instance;
	}

	public static void prepare( Engine e, MainActivity ma, Camera c,
			VertexBufferObjectManager vertexBufferObjectManager ) {
		getInstance().engine = e;
		getInstance().activity = ma;
		getInstance().camera = c;
		getInstance().vbom = vertexBufferObjectManager;
		getInstance().resizeFactor = c.getWidth()
				/ getInstance().cameraWidthRef;
		getInstance().setVibrator();

	}

	public void safeToastMessageShow( String pToastMsg, int pToastLength ) {
		final String toastMsg = pToastMsg;
		final int toastLength = pToastLength;
		activity.runOnUiThread( new Runnable() {
			@Override
			public void run( ) {
				Toast.makeText( activity.getApplicationContext(),
						toastMsg,
						toastLength ).show();
			}
		} );
	}


	private void setVibrator( ) {
		this.pVibrator = ( Vibrator ) this.activity
				.getSystemService( Context.VIBRATOR_SERVICE );
	}

	public Vibrator getVibrator( ) {
		return this.pVibrator;
	}

	public Engine getEngine( ) {
		return engine;
	}

	public MainActivity getActivity( ) {
		return activity;
	}

	public Camera getCamera( ) {
		return camera;
	}

	public VertexBufferObjectManager getVbom( ) {
		return vbom;
	}

	public float getResizeFactor( ) {
		return this.resizeFactor;
	}

	public int applyResizeFactor( int pSize ) {
		return ( int ) ( pSize * getResizeFactor() );
	}

	public float applyResizeFactor( float pSize ) {
		return pSize * getResizeFactor();
	}

	private void setAssetBasePath( ) {
		int cameraWidth;
		setSvgAssetBasePath( "svg/" );

		cameraWidth = ( int ) camera.getWidth();
		switch ( cameraWidth ) {
			case 480 :
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx480/" );
				break;
			case 720 :
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx720/" );
				break;
			case 1080 :
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx1080/" );
				break;
			default :
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath( "gfx720/" );
				Log.v( "assetPathErro", "SomethingWrong~" );
				break;
		}
	}

	private void setSvgAssetBasePath( String pAssetBasePath ) {
		SVGBitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath( pAssetBasePath );
	}

	public void loadResources( ) {
		loadFonts();
		loadGraphicResources();
		loadSounds();
	}

	private void loadSounds( ) {
		SoundFactory.setAssetBasePath( "sfx/" );

		try {
			soundIntro = SoundFactory.createSoundFromAsset(
					this.engine.getSoundManager(), this.activity, "intro.ogg" );
			soundIntro.setLooping( false );

		} catch ( IllegalStateException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	public Sound getSoundIntro( ) {
		return soundIntro;
	}

	private void loadGraphicResources( ) {
		setAssetBasePath();
		loadGraphicResourcesNearest();
		loadGraphicResourcesBilinear();
		loadGraphicResourcesNearestPremultiplyAlpha();
		loadGraphicResourcesBilinearPremultiplyAlpha();
	}

	private void loadGraphicResourcesBilinearPremultiplyAlpha( ) {
		atlasBilinearPremultiplyAlpha = new BuildableBitmapTextureAtlas(
				this.activity.getTextureManager(),
				this.applyResizeFactor( sizeAtlasBilineaPremultiplyAlphar.x ),
				this.applyResizeFactor( sizeAtlasBilineaPremultiplyAlphar.y ),
				BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA );

		//		regionGround = SVGBitmapTextureAtlasTextureRegionFactory
		//				.createFromAsset( this.atlasBilinearPremultiplyAlpha,
		//						this.activity, "ground_1080x300.svg",
		//						this.applyResizeFactor( 1080 ),
		//						this.applyResizeFactor( 300 ) );

		try {
			atlasBilinearPremultiplyAlpha
					.build( new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, applyResizeFactor( 5 ) ) );
			atlasBilinearPremultiplyAlpha.load();
		} catch ( TextureAtlasBuilderException e ) {
			e.printStackTrace();
		}
	}

	private void loadGraphicResourcesNearestPremultiplyAlpha( ) {
		atlasNearestPremultiplyAlpha = new BuildableBitmapTextureAtlas(
				this.activity.getTextureManager(),
				this.applyResizeFactor( sizeAtlasNearestPremultiplyAlpha.x ),
				this.applyResizeFactor( sizeAtlasNearestPremultiplyAlpha.y ),
				BitmapTextureFormat.RGBA_8888,
				TextureOptions.NEAREST_PREMULTIPLYALPHA );

		regionOnOffIcon = SVGBitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset( this.atlasNearestPremultiplyAlpha, this.activity,
						"onofficon_244x104_2x1.svg",
						this.applyResizeFactor( 244 ),
						this.applyResizeFactor( 104 ), 2, 1 );

		regionLightOnEffect = SVGBitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset( this.atlasNearestPremultiplyAlpha, this.activity,
						"lightoneffect660x393_2x1.svg",
						this.applyResizeFactor( 660 ),
						this.applyResizeFactor( 394 ), 2, 1 );

		try {
			atlasNearestPremultiplyAlpha
					.build( new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, applyResizeFactor( 5 ) ) );
			atlasNearestPremultiplyAlpha.load();
		} catch ( TextureAtlasBuilderException e ) {
			e.printStackTrace();
		}

	}

	private void loadGraphicResourcesBilinear( ) {
		atlasBilinear = new BuildableBitmapTextureAtlas(
				this.activity.getTextureManager(),
				this.applyResizeFactor( sizeAtlasBilinear.x ),
				this.applyResizeFactor( sizeAtlasBilinear.y ),
				BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR );

		//		regionFinger = SVGBitmapTextureAtlasTextureRegionFactory
		//				.createFromAsset( this.atlasBilinear, this.activity,
		//						"finger_139x126.svg", this.applyResizeFactor( 139 ),
		//						this.applyResizeFactor( 126 ) );

		try {
			atlasBilinear
					.build( new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, applyResizeFactor( 5 ) ) );
			atlasBilinear.load();
		} catch ( TextureAtlasBuilderException e ) {
			e.printStackTrace();
		}

	}

	private void loadGraphicResourcesNearest( ) {// Default texture option
		atlasNearest = new BuildableBitmapTextureAtlas(
				this.activity.getTextureManager(),
				this.applyResizeFactor( sizeAtlasNearest.x ),
				this.applyResizeFactor( sizeAtlasNearest.y ),
				BitmapTextureFormat.RGBA_8888, TextureOptions.NEAREST );

		regionMarketShareStar = SVGBitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset( this.atlasNearest, this.activity,
						"market_share_star_btns_320x480.svg",
						this.applyResizeFactor( 320 ),
						this.applyResizeFactor( 480 ), 2, 3 );

		try {
			atlasNearest
					.build( new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, applyResizeFactor( 5 ) ) );
			atlasNearest.load();
		} catch ( TextureAtlasBuilderException e ) {
			e.printStackTrace();
		}

	}

	private void loadFonts( ) {
		FontFactory.setAssetBasePath( "fonts/" );

		final ITexture tFontTextureDefault = new BitmapTextureAtlas(
				activity.getTextureManager(),
				( int ) applyResizeFactor( camera.getWidth() ),
				( int ) applyResizeFactor( 512f ), TextureOptions.BILINEAR );

		final ITexture tFontTextureButton = new BitmapTextureAtlas(
				activity.getTextureManager(),
				( int ) applyResizeFactor( camera.getWidth() ),
				( int ) applyResizeFactor( 512f ), TextureOptions.BILINEAR );

		this.fontDefault = FontFactory.createFromAsset(
				activity.getFontManager(), tFontTextureDefault,
				activity.getAssets(), "UbuntuB.ttf", applyResizeFactor( FONT_SIZE_DEFAULT ), true,
				AppColor.getInstance().WHITE.getABGRPackedInt() );
		this.fontDefault.load();

		this.fontButton = FontFactory.createFromAsset(
				activity.getFontManager(), tFontTextureButton,
				activity.getAssets(), "UbuntuB.ttf", applyResizeFactor( FONT_SIZE_BUTTON ), true,
				AppColor.getInstance().WHITE.getABGRPackedInt() );
		this.fontButton.load();

	}

	public Font getFontDefault( ) {
		return this.fontDefault;
	}

	public Font getFontButton( ) {
		return this.fontButton;
	}

}
