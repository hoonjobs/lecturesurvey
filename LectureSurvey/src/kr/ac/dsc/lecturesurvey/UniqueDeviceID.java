package kr.ac.dsc.lecturesurvey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

@SuppressLint("DefaultLocale")
public class UniqueDeviceID {
	
	@SuppressLint("DefaultLocale")
	public static String getUniqueDeviceID(Context context)
	{
		//1. The IMEI: only for Android devices with Phone use:

		TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String m_szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE

		//This requires adding a permission in AndroidManifest.xml, and users will be notified upon installing your software: android.permission.READ_PHONE_STATE. The IMEI is unique for your phone and it looks like this: 359881030314356 (unless you have a pre-production device with an invalid IMEI like 0000000000000).

		////////////////////////////////////////////
		////////////////////////////////////////////
		
		//2. Pseudo-Unique ID, that works on all Android devices
		//Some devices don't have a phone (eg. Tablets) or for some reason you don't want to include the READ_PHONE_STATE permission. You can still read details like ROM Version, Manufacturer name, CPU type, and other hardware details, that will be well suited if you want to use the ID for a serial key check, or other general purposes. The ID computed in this way won't be unique: it is possible to find two devices with the same ID (based on the same hardware and rom image) but the chances in real world applications are negligible. For this purpose you can use the Build class:
		
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI
				Build.BOARD.length()%10+ Build.BRAND.length()%10 +
				Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
				Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
				Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
				Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
				Build.TAGS.length()%10 + Build.TYPE.length()%10 +
				Build.USER.length()%10 ; //13 digits
		
		//Most of the Build members are strings, what we're doing here is to take their length and transform it via modulo in a digit. We have 13 such digits and we are adding two more in front (35) to have the same size ID like the IMEI (15 digits). There are other possibilities here are well, just have a look at these strings.
		//Returns something like: 355715565309247 . No special permission are required, making this approach very convenient.
		
		////////////////////////////////////////////
		////////////////////////////////////////////

		//3. The Android ID , considered unreliable because it can sometimes be null. The documentation states that it "can change upon factory reset". This string can also be altered on a rooted phone.
		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

		//Returns: 9774d56d682e549c . No special permissions required.

		////////////////////////////////////////////
		////////////////////////////////////////////

		//4. The WLAN MAC Address string, is another unique identifier that you can use as a device id. Before you read it, you will need to make sure that your project has the android.permission.ACCESS_WIFI_STATE permission or the WLAN MAC Address will come up as null.
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

		//Returns: 00:11:22:33:44:55 (not a real address since this is a custom ROM , as you can see the MAC address can easily be faked). WLAN doesn't have to be on, to read this value.
		
		////////////////////////////////////////////
		////////////////////////////////////////////
		
		
		//5. The BT MAC Address string, available on Android devices with Bluetooth, can be read if your project has the android.permission.BLUETOOTH permission.
		BluetoothAdapter m_BluetoothAdapter	= null; // Local Bluetooth adapter
    	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	String m_szBTMAC = "";
    	if(m_BluetoothAdapter != null) m_BluetoothAdapter.getAddress();

		//Returns: 43:25:78:50:93:38 . BT doesn't have to be on, to read it.

		////////////////////////////////////////////
		////////////////////////////////////////////

//	   Combined Device ID
//		Above, you have here 5 ways of reading a device unique identifier. Some of them might fail and return null, or you won't be able to use them because of the special permissions or because the hardware is missing (phone, bluetooth, wlan).
//		Nevertheless on all platforms you will find at least one that works. So a very good idea is to mix these strings, and generate a unique result out of their sum. To mix the strings you can simply concatenate them, and the result can be used to compute a md5 hash:

    	String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID+ m_szWLANMAC + m_szBTMAC;
    	// compute md5
    	MessageDigest m = null;
    	try {
    		m = MessageDigest.getInstance("MD5");
    	} catch (NoSuchAlgorithmException e) {
    		e.printStackTrace();
    	}
    	m.update(m_szLongID.getBytes(),0,m_szLongID.length());
    	// get md5 bytes
    	byte p_md5Data[] = m.digest();
    	// create a hex string
    	String m_szUniqueID = new String();
    	for (int i=0;i
    			<p_md5Data.length;i++) {
    		int b =  (0xFF & p_md5Data[i]);
    		// if it is a single digit, make sure it have 0 in front (proper padding)
    		if (b <= 0xF) m_szUniqueID+="0";
    		// add number to string
    		m_szUniqueID+=Integer.toHexString(b);
    	}
    	// hex string to uppercase
    	m_szUniqueID = m_szUniqueID.toUpperCase(Locale.getDefault());
		 

//		The result has 32 hex digits and it looks like this:
//		9DDDF85AFF0A87974CE4541BD94D5F55
    	
    	return m_szUniqueID;
	}
}
