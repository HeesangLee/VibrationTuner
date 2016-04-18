package dalcoms.pub.vibrationtuner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dalcoms.pub.flashlight.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class DalcomsAppList {
	public static final String EXTRA_APP_PACKAGE = "dalcoms.pub.flashlight.appPackage";
	public static final String EXTRA_APP_TITLE = "dalcoms.pub.flashlight.appTitle";
	public static final String EXTRA_APP_DESCRIPTION = "dalcoms.pub.flashlight.appDescription";

	final ArrayList<String> mPakageListOfDalcomsApp = new ArrayList<String>() {
		{
			add( "dalcoms.pub.jumpyx" );
			add( "dalcoms.pub.circlecolormatch" );
			add( "dalcoms.pub.brainwavestudio" );
			add( "dalcoms.pub.naturesound" );
			add( "dalcoms.pub.lottonumgenall" );
			add( "dalcoms.pub.lottonum645only" );
			add( "dalcoms.pub.mathkids" );
			add( "dalcoms.pub.moodvibrator" );
			add( "hs.app.skinvibrator" );
			add( "hslee.android.unitconverter" );
		}
	};

	Map<String, String> mPackageTitleMap;
	Map<String, String> mPackageDescriptionMap;

	ArrayList<String> mUninstalledPakageList;
	ArrayList<String> mInstalledPakageList;
	Context mContext;
	
	public DalcomsAppList(){
		
	}

	public DalcomsAppList( Context pContext ) {
		mContext = pContext;

		putAppTitlesForNotification();
		putAppDescriptionForNotification();
		initDalcomsPackageList();
	}

	private void initDalcomsPackageList( ) {
		checkInstalledApps();
	}

	private void putAppTitlesForNotification( ) {
		this.mPackageTitleMap = new HashMap<String, String>() {
			{
				put( "dalcoms.pub.jumpyx", mContext.getString( R.string.noti_title_jumpyx ) );
				put( "dalcoms.pub.circlecolormatch",
						mContext.getString( R.string.noti_title_circlecolormatch ) );
				put( "dalcoms.pub.brainwavestudio", mContext.getString( R.string.noti_title_brainwavestudio ) );
				put( "dalcoms.pub.naturesound", mContext.getString( R.string.noti_title_naturesound ) );
				put( "dalcoms.pub.lottonumgenall", mContext.getString( R.string.noti_title_lottonumgenall ) );
				put( "dalcoms.pub.lottonum645only", mContext.getString( R.string.noti_title_lottonum645only ) );
				put( "dalcoms.pub.mathkids", mContext.getString( R.string.noti_title_mathkids ) );
				put( "dalcoms.pub.moodvibrator", mContext.getString( R.string.noti_title_moodvibrator ) );
				put( "hs.app.skinvibrator", mContext.getString( R.string.noti_title_skinvibrator ) );
				put( "hslee.android.unitconverter", mContext.getString( R.string.noti_title_unitconverter ) );
			}
		};
	}

	private void putAppDescriptionForNotification( ) {
		this.mPackageDescriptionMap = new HashMap<String, String>() {
			{
				put( "dalcoms.pub.jumpyx", mContext.getString( R.string.noti_desc_jumpyx ) );
				put( "dalcoms.pub.brainwavestudio", mContext.getString( R.string.noti_desc_brainwavestudio ) );
				put( "dalcoms.pub.naturesound", mContext.getString( R.string.noti_desc_naturesound ) );
				put( "dalcoms.pub.lottonumgenall", mContext.getString( R.string.noti_desc_lottonumgenall ) );
				put( "dalcoms.pub.circlecolormatch", mContext.getString( R.string.noti_desc_circlecolormatch ) );
				put( "dalcoms.pub.lottonum645only", mContext.getString( R.string.noti_desc_lottonum645only ) );
				put( "dalcoms.pub.mathkids", mContext.getString( R.string.noti_desc_mathkids ) );
				put( "dalcoms.pub.moodvibrator", mContext.getString( R.string.noti_desc_moodvibrator ) );
				put( "hs.app.skinvibrator", mContext.getString( R.string.noti_desc_skinvibrator ) );
				put( "hslee.android.unitconverter", mContext.getString( R.string.noti_desc_unitconverter ) );
			}
		};
	}

	private void checkInstalledApps( ) {
		mInstalledPakageList = new ArrayList<String>();
		mUninstalledPakageList = new ArrayList<String>();

		for ( String pkgName : mPakageListOfDalcomsApp ) {
			if ( isPackageInstalled( mContext, pkgName ) ) {
				mInstalledPakageList.add( pkgName );
			} else {
				mUninstalledPakageList.add( pkgName );
			}
		}
	}

	private boolean isPackageInstalled( Context pContext, String pPackageName ) {
		boolean result = false;
		try {
			pContext.getPackageManager().getApplicationInfo( pPackageName, 0 );
			result = true;
		} catch ( NameNotFoundException e ) {
			result = false;
		}
		return result;
	}

	public ArrayList<String> getDalcomsApps( ) {
		return mPakageListOfDalcomsApp;
	}

	public ArrayList<String> getInstalledApps( ) {
		return mInstalledPakageList;
	}

	public ArrayList<String> getUninstalledApps( ) {
		return mUninstalledPakageList;
	}

	public String getPackageTitle( String pPackageName ) {
		return this.mPackageTitleMap.get( pPackageName );
	}

	public String getPackageDescription( String pPackageName ) {
		return this.mPackageDescriptionMap.get( pPackageName );
	}

}
