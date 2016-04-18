package dalcoms.pub.vibrationtuner;

import lib.dalcoms.andengineheesanglib.utils.HsMath;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class RectangleOnOffButton extends Rectangle {

	ITiledTextureRegion mITiledRegionOnOff;
	Font mTextFont;
	boolean mButtonOnOffStatus;
	TiledSprite mOnOffIconSprite;
	VertexBufferObjectManager vbom;
	Rectangle mTouchEffectRect;

	Text mTextOn;
	Text mTextOff;
	HsMath hsMath;

	public RectangleOnOffButton( float pX, float pY,
			float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,
			ITiledTextureRegion pITiledRegionOnOff,
			Font pTextFont,
			boolean pButtonOnOffStatus ) {

		super( pX, pY, pWidth, pHeight, pVertexBufferObjectManager );

		mITiledRegionOnOff = pITiledRegionOnOff;
		mTextFont = pTextFont;
		mButtonOnOffStatus = pButtonOnOffStatus;

		hsMath = new HsMath();

		ResourcesManager.getInstance().getEngine().runOnUpdateThread( new Runnable() {

			@Override
			public void run( ) {
				attachInnerComponents( mButtonOnOffStatus );
			}
		} );
	}

	public void setCenterPosition( float pCenterX, float pCenterY ) {
		final float pX = pCenterX - getCenterX();
		final float pY = pCenterY - getCenterY();
		this.setPosition( pX, pY );
	}

	public float getCenterX( ) {
		return this.getX() + this.getWidth() / 2f;
	}

	public float getCenterY( ) {
		return this.getY() + this.getHeight() / 2f;
	}

	private void attachInnerComponents( boolean pButtonStatus ) {
		attachTouchEffect();
		attachOnOffIcon( pButtonStatus );

		attachOnOffText( pButtonStatus );
	}

	private void attachTouchEffect( ) {
		final float pWidth = getWidth() * 0.8f;
		final float pHeight = getHeight();
		final float pY = 0;
		final float pX = ( getWidth() - pWidth ) / 2f;
		mTouchEffectRect = new Rectangle( pX, pY, pWidth, pHeight, vbom );
		mTouchEffectRect.setColor( 1f, 1f, 1f, 0.35f );
		mTouchEffectRect.setVisible( false );
		attachChild( mTouchEffectRect );
	}

	private void attachOnOffIcon( boolean pButtonStatus ) {
		final float pX = ( this.getWidth() - mITiledRegionOnOff.getWidth() ) / 2f;
		final float pY = ( this.getHeight() - mITiledRegionOnOff.getHeight() ) / 2f;
		mOnOffIconSprite = new TiledSprite( pX, pY, mITiledRegionOnOff, vbom );
		mOnOffIconSprite.setCurrentTileIndex( pButtonStatus ? 1 : 0 );
		attachChild( mOnOffIconSprite );
	}

	private void attachOnOffText( boolean pButtonStatus ) {
		float marginVertical = ResourcesManager.getInstance().applyResizeFactor( 30f );

		mTextOn = new Text( 0, 0, mTextFont, "ON", new TextOptions( HorizontalAlign.LEFT ), vbom );
		mTextOff = new Text( 0, 0, mTextFont, "OFF", new TextOptions( HorizontalAlign.LEFT ), vbom );

		mTextOn.setPosition( hsMath.getAlignCenterFloat( mTextOn.getWidth(), this.getWidth() ),
				marginVertical );
		mTextOff.setPosition( hsMath.getAlignCenterFloat( mTextOff.getWidth(), this.getWidth() ),
				this.getHeight() - ( marginVertical + mTextOff.getHeight() ) );

		attachChild( mTextOn );
		attachChild( mTextOff );

		setOnOffTextColor( pButtonStatus );
	}

	private void setOnOffTextColor( boolean pButtonStatus ) {
		AppColor thisAppColor = AppColor.getInstance();
		if ( pButtonStatus ) {
			mTextOn.setColor( thisAppColor.ONOFF_BUTTON_TEXTON );
			mTextOff.setColor( thisAppColor.ONOFF_BUTTON_TEXTOFF );
		}else{
			mTextOff.setColor( thisAppColor.ONOFF_BUTTON_TEXTON );
			mTextOn.setColor( thisAppColor.ONOFF_BUTTON_TEXTOFF );
		}
	}

	public boolean isButtonOn( ) {
		return this.mButtonOnOffStatus;
	}

	private void togleButtonOnOff( ) {
		mButtonOnOffStatus = !mButtonOnOffStatus;
	}

	@Override
	public boolean onAreaTouched( TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY ) {
		if ( pSceneTouchEvent.isActionDown() ) {
			mTouchEffectRect.setVisible( true );
			mTouchEffectRect.registerEntityModifier( new ScaleModifier( 0.2f, 0.2f, 1f ) {
				@Override
				protected void onModifierFinished( IEntity pItem ) {
					super.onModifierFinished( pItem );
					mTouchEffectRect.setVisible( false );
				}
			} );
		} else {
			if ( pSceneTouchEvent.isActionUp() ) {
				mTouchEffectRect.setVisible( false );
				isButtonToggled();
			}
		}

		return super.onAreaTouched( pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY );
	}

	public void isButtonToggled( ) {
		togleButtonOnOff();
		mOnOffIconSprite.setCurrentTileIndex( isButtonOn() ? 1 : 0 );
		setOnOffTextColor( isButtonOn() );
	}

}
