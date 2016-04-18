package dalcoms.pub.vibrationtuner;

import dalcoms.pub.flashlight.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotiService extends IntentService {

	public NotiService( ) {

		super( "dalcoms.pub.flashlight.NotiService" );
	}

	@Override
	protected void onHandleIntent( Intent intent ) {

		long endTime = System.currentTimeMillis() + 3 *( 60*1000*60*24);//day

		while ( System.currentTimeMillis() < endTime ) {
			synchronized ( this ) {
				try {
					wait( endTime - System.currentTimeMillis() );
				} catch ( Exception e ) {
				}
			}
		}
		notiSelfAdNotification( intent.getStringExtra( DalcomsAppList.EXTRA_APP_PACKAGE ),
				intent.getStringExtra( DalcomsAppList.EXTRA_APP_TITLE ),
				intent.getStringExtra( DalcomsAppList.EXTRA_APP_DESCRIPTION ) );
	}

	private void notiSelfAdNotification( String pkgName, String notiTitle, String notiContentText ) {
		//		String appId = "dalcoms.pub.jumpyx";
		Intent resultIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=" + pkgName ) );

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( getApplicationContext() )
				.setSmallIcon( R.drawable.ic_noti )
				.setContentTitle( notiTitle )
				.setContentText( notiContentText )
				.setCategory( NotificationCompat.CATEGORY_PROMO )
				.setAutoCancel( true )
				.setColor( 0xffff6600 )
				.setVibrate( new long[]{100,100} );

		TaskStackBuilder stackBuilder = TaskStackBuilder.create( getApplicationContext() );
		//		stackBuilder.addParentStack( MainActivity.class );
		stackBuilder.addNextIntent( resultIntent );

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0,
				PendingIntent.FLAG_UPDATE_CURRENT );

		mBuilder.setContentIntent( resultPendingIntent );

		NotificationManager mNotificationManager = ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
		mNotificationManager.notify( 0, mBuilder.build() );
	}
}
