package dalcoms.pub.vibrationtuner;

import lib.dalcoms.andengineheesanglib.utils.HsMath;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

public class RectangleSeekBar extends Rectangle {
	Font mTextFont;
	VertexBufferObjectManager vbom;
	//	Rectangle mTouchEffectRect;

	Text mTextTitle;
	HsMath hsMath;

	ResourcesManager resourceManager;

	Rectangle mRectSeekBar;
	Rectangle mRectActiveBar;
	Rectangle mRectSwitch;
	Color mColorSeekBar;
	Color mColorActiveBarEnabled;
	Color mColorActiveBarDisabled;
	Color mColorSwitchEnabled;
	Color mColorSwitchDisabled;
	float mSeekPosition = 0f;
	boolean mState = false;
	String mTitle = "";
	boolean isTouchEnabled = false;

	public RectangleSeekBar( float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager, Font pTextFont,
			String pTitle,
			Color pColorSeekBar,
			Color pColorActiveBarEnabled,
			Color pColorActiveBarDisabled,
			Color pColorSwitchEnabled,
			Color pColorSwitchDisabled,
			float pInitSeekPosition,
			boolean pInitState ) {
		super( pX, pY, pWidth, pHeight, pVertexBufferObjectManager );

		this.setAlpha( 0f );
		mTextFont = pTextFont;
		mColorSeekBar = pColorSeekBar;
		mColorActiveBarDisabled = pColorActiveBarDisabled;
		mColorActiveBarEnabled = pColorActiveBarEnabled;
		mColorSwitchDisabled = pColorSwitchDisabled;
		mColorSwitchEnabled = pColorSwitchEnabled;
		mSeekPosition = pInitSeekPosition;
		mState = pInitState;
		mTitle = pTitle;

		hsMath = new HsMath();

		resourceManager = ResourcesManager.getInstance();
		resourceManager.getEngine().runOnUpdateThread( new Runnable() {

			@Override
			public void run( ) {
				attachInnerComponents();
			}
		} );
	}

	public boolean isEnabled( ) {
		return this.mState;
	}

	public void setEnable( boolean pEn ) {
		this.mState = pEn;
		setColorState( isEnabled() );
	}

	private void setColorState( boolean pDisEn ) {//false=disabled, true=enabled
		mRectActiveBar.setColor( pDisEn ? mColorActiveBarEnabled : mColorActiveBarDisabled );
		mRectSwitch.setColor( pDisEn ? mColorSwitchEnabled : mColorSwitchDisabled );
	}

	private float getSeekPosition( float pPositionRatio ) {
		float result = 0;

		final float pSwitchWidth = this.getHeight();
		final float pSeekLength = this.getWidth() - pSwitchWidth;
		final float pOffset = pSwitchWidth / 2f;

		result = pSeekLength * pPositionRatio + pOffset;

		return result;
	}

	private void attachInnerComponents( ) {
		attachSeekBar();
		attachAtiveBar();
		attachSwitch();
		attachTitleText();

		setColorState( isEnabled() );
	}

	private void attachSeekBar( ) {
		final float pY = hsMath.getAlignCenterFloat( this.getHeight() / 4f, this.getHeight() );
		mRectSeekBar = new Rectangle( 0f, pY, this.getWidth(), this.getHeight() / 4f, vbom );
		attachChild( mRectSeekBar );
		mRectSeekBar.setColor( mColorSeekBar );
	}

	private void attachAtiveBar( ) {
		mRectActiveBar = new Rectangle( 0, 0, getSeekPosition( mSeekPosition ), mRectSeekBar.getHeight(),
				vbom );
		mRectSeekBar.attachChild( mRectActiveBar );
	}

	private void attachSwitch( ) {//include knob
		final float pWidth = getHeight();
		mRectSwitch = new Rectangle( getSeekPosition( mSeekPosition ) - getHeight() / 2f, 0,
				pWidth, pWidth, vbom );
		attachChild( mRectSwitch );
		Rectangle pRectInner = new Rectangle( hsMath.getAlignCenterFloat( pWidth / 4f, pWidth ),
				hsMath.getAlignCenterFloat( pWidth / 4f, pWidth ),
				pWidth / 4f, pWidth / 4f, vbom );
		pRectInner.setColor( 1f, 1f, 1f, 0.7f );
		mRectSwitch.attachChild( pRectInner );

	}

	private void attachTitleText( ) {
		mTextTitle = new Text( 0, 0, mTextFont, mTitle, vbom );
		final float pX = getWidth() - mTextTitle.getWidth();
		final float pY = -1 * mTextTitle.getHeight();
		mTextTitle.setPosition( pX, pY );
		mTextTitle.setColor( mColorSeekBar );
		attachChild( mTextTitle );
	}

	public void rePositionKey( float pPositionRatio ) {

		setKeyPosition( getSeekPosition( pPositionRatio ) );
	}

	private void setKeyPosition( float pCenterX ) {
		float pKeyWidthHalf = mRectSwitch.getWidth() / 2f;
		float pX = 0;
		if ( pCenterX < pKeyWidthHalf ) {
			pX = 0f;
		} else if ( pCenterX > getWidth() - pKeyWidthHalf ) {
			pX = getWidth() - mRectSwitch.getWidth();
		} else {
			pX = pCenterX - pKeyWidthHalf;
		}
		mRectSwitch.setX( pX );

		mRectActiveBar.setWidth( pX );
	}

	public float getSeekRatio( ) {
		float result = 0;
		result = mRectSwitch.getX() / ( getWidth() - mRectSwitch.getWidth() );
		return result;
	}

	@Override
	public boolean onAreaTouched( TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY ) {
		if ( isEnabled() ) {
			if ( pSceneTouchEvent.isActionDown() ) {
				isTouchEnabled = true;
//				resourceManager.getVibrator().vibrate( 30 );
				setKeyPosition( pTouchAreaLocalX );
				onSeekChanged();
			} else {
				if ( pSceneTouchEvent.isActionMove() ) {
					if ( isTouchEnabled ) {
						setKeyPosition( pTouchAreaLocalX );
						onSeekChanged();
					}
				} else if ( pSceneTouchEvent.isActionUp() ) {
					if ( isTouchEnabled ) {
						isTouchEnabled = false;
						onSeekChanged();
					}
				}
			}
		} else {
			isTouchEnabled = false;
		}

		return super.onAreaTouched( pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY );
	}

	public void onSeekChanged( ) {
		Log.v( "seek", String.valueOf( getSeekRatio() ) );
	}

}
