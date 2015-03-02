# android_log_view

###usage:

import src folder into src folder

import layout folder into res/layout folder

add LogView activity to your androidManifest

	<activity
            android:name="james.toolbox.jlogger.view.LogView"
            android:label="logView"
            android:screenOrientation="portrait" >
    </activity>

1. call init function 

	JLogger.init(activity, DBName, DBVersion)

	if you don't hava a DB in your application, just pass null for second parameter, pass 0 for third parameter

2. call Jlogger.info, debug, warn, error to log
3. if you want to see the log view, find a entry to trigger the logview, and use below code at the trigger point

            Intent i = new Intent(this, LogView.class);
            startActivity(i);