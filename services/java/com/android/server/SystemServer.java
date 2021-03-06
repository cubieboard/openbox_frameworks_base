/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server;

import android.accounts.AccountManagerService;
import android.app.ActivityManagerNative;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.res.Configuration;
import android.media.AudioService;
import android.net.wifi.p2p.WifiP2pService;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.server.BluetoothA2dpService;
import android.server.BluetoothService;
import android.server.search.SearchManagerService;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.view.WindowManager;
import android.os.DynamicPManager;
import android.media.AudioSystem;

import com.android.internal.app.ShutdownThread;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.SamplingProfilerIntegration;
import com.android.server.accessibility.AccessibilityManagerService;
import com.android.server.am.ActivityManagerService;
import com.android.server.net.NetworkPolicyManagerService;
import com.android.server.net.NetworkStatsService;
import com.android.server.pm.PackageManagerService;
import com.android.server.usb.UsbService;
import com.android.server.wm.WindowManagerService;
import com.android.server.WifiDisplayManagerService;
import com.android.server.googleremote.GoogleRemoteService;

import dalvik.system.VMRuntime;
import dalvik.system.Zygote;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;


/* add by Gary. start {{----------------------------------- */
/* 2011-11-18 */
/* init audio output channel */
import android.view.DisplayManager;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.DispList;
import com.android.internal.allwinner.config.ProductConfig;
/* add by Gary. end   -----------------------------------}} */
class ServerThread extends Thread {
    private static final String TAG = "SystemServer";
    private static final String ENCRYPTING_STATE = "trigger_restart_min_framework";
    private static final String ENCRYPTED_STATE = "1";
    /* add by Gary. start {{----------------------------------- */
    /* 2012-5-9 */
    /* support the YPbPr */
    private final boolean mDeviceHasYpbpr = false;
    /* add by Gary. end   -----------------------------------}} */

    ContentResolver mContentResolver;

    void reportWtf(String msg, Throwable e) {
        Slog.w(TAG, "***********************************************");
        Log.wtf(TAG, "BOOT FAILURE " + msg, e);
    }

    @Override
    public void run() {
        EventLog.writeEvent(EventLogTags.BOOT_PROGRESS_SYSTEM_RUN,
            SystemClock.uptimeMillis());

        Looper.prepare();

        android.os.Process.setThreadPriority(
                android.os.Process.THREAD_PRIORITY_FOREGROUND);

        BinderInternal.disableBackgroundScheduling(true);
        android.os.Process.setCanSelfBackground(false);

        // Check whether we failed to shut down last time we tried.
        {
            final String shutdownAction = SystemProperties.get(
                    ShutdownThread.SHUTDOWN_ACTION_PROPERTY, "");
            if (shutdownAction != null && shutdownAction.length() > 0) {
                boolean reboot = (shutdownAction.charAt(0) == '1');

                final String reason;
                if (shutdownAction.length() > 1) {
                    reason = shutdownAction.substring(1, shutdownAction.length());
                } else {
                    reason = null;
                }

                ShutdownThread.rebootOrShutdown(reboot, reason);
            }
        }

        String factoryTestStr = SystemProperties.get("ro.factorytest");
        int factoryTest = "".equals(factoryTestStr) ? SystemServer.FACTORY_TEST_OFF
                : Integer.parseInt(factoryTestStr);

        LightsService lights = null;
        PowerManagerService power = null;
        DynamicPManagerService dpm = null;
        BatteryService battery = null;
        AlarmManagerService alarm = null;
        NetworkManagementService networkManagement = null;
        NetworkStatsService networkStats = null;
        NetworkPolicyManagerService networkPolicy = null;
        ConnectivityService connectivity = null;
        WifiP2pService wifiP2p = null;
        WifiService wifi = null;
        EthernetService ethernet = null;	/*  EthernetService (add by shuge@allwinnertech.com)  */
        IPackageManager pm = null;
        Context context = null;
        WindowManagerService wm = null;
        BluetoothService bluetooth = null;
        BluetoothA2dpService bluetoothA2dp = null;
        DockObserver dock = null;
        UsbService usb = null;
        UiModeManagerService uiMode = null;
        RecognitionManagerService recognition = null;
        ThrottleService throttle = null;
        NetworkTimeUpdateService networkTimeUpdater = null;

        // Critical services...
        try {
            Slog.i(TAG, "Entropy Service");
            ServiceManager.addService("entropy", new EntropyService());

            Slog.i(TAG, "Power Manager");
            power = new PowerManagerService();
            ServiceManager.addService(Context.POWER_SERVICE, power);

            Slog.i(TAG, "Activity Manager");
            context = ActivityManagerService.main(factoryTest);

            Slog.i(TAG, "Telephony Registry");
            ServiceManager.addService("telephony.registry", new TelephonyRegistry(context));

            AttributeCache.init(context);

            Slog.i(TAG, "Package Manager");
            // Only run "core" apps if we're encrypting the device.
            String cryptState = SystemProperties.get("vold.decrypt");
            boolean onlyCore = false;
            if (ENCRYPTING_STATE.equals(cryptState)) {
                Slog.w(TAG, "Detected encryption in progress - only parsing core apps");
                onlyCore = true;
            } else if (ENCRYPTED_STATE.equals(cryptState)) {
                Slog.w(TAG, "Device encrypted - only parsing core apps");
                onlyCore = true;
            }

            pm = PackageManagerService.main(context,
                    factoryTest != SystemServer.FACTORY_TEST_OFF,
                    onlyCore);
            boolean firstBoot = false;
            try {
                firstBoot = pm.isFirstBoot();
            } catch (RemoteException e) {
            }

            ActivityManagerService.setSystemProcess();

            mContentResolver = context.getContentResolver();

            // The AccountManager must come before the ContentService
            try {
                Slog.i(TAG, "Account Manager");
                ServiceManager.addService(Context.ACCOUNT_SERVICE,
                        new AccountManagerService(context));
            } catch (Throwable e) {
                Slog.e(TAG, "Failure starting Account Manager", e);
            }

            Slog.i(TAG, "Content Manager");
            ContentService.main(context,
                    factoryTest == SystemServer.FACTORY_TEST_LOW_LEVEL);

            Slog.i(TAG, "System Content Providers");
            ActivityManagerService.installSystemProviders();

            Slog.i(TAG, "Lights Service");
            lights = new LightsService(context);

            Slog.i(TAG, "Battery Service");
            battery = new BatteryService(context, lights);
            ServiceManager.addService("battery", battery);

			Slog.i(TAG, "DynamicPManager");
			dpm = new DynamicPManagerService(context);
			ServiceManager.addService(DynamicPManager.DPM_SERVICE, dpm);

            Slog.i(TAG, "Vibrator Service");
            ServiceManager.addService("vibrator", new VibratorService(context));

            // only initialize the power service after we have started the
            // lights service, content providers and the battery service.
            power.init(context, lights, ActivityManagerService.self(), battery);

            Slog.i(TAG, "Alarm Manager");
            alarm = new AlarmManagerService(context);
            ServiceManager.addService(Context.ALARM_SERVICE, alarm);

            Slog.i(TAG, "Init Watchdog");
            Watchdog.getInstance().init(context, battery, power, alarm,
                    ActivityManagerService.self());

            Slog.i(TAG, "Window Manager");
            wm = WindowManagerService.main(context, power,
                    factoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL,
                    !firstBoot);
            ServiceManager.addService(Context.WINDOW_SERVICE, wm);

			if(SystemProperties.get("ro.display.switch").equals("1"))
			{
				Slog.i(TAG, "Display Manager");
				DisplayManagerService display = new DisplayManagerService(context,power);
	            ServiceManager.addService(Context.DISPLAY_SERVICE, display);
			}
			if(true) //SystemProperties.get("ro.wifidisplay.switch").equals("1")
			{
				Slog.i(TAG, "Display Manager");
				WifiDisplayManagerService wifidisplay = new WifiDisplayManagerService(context,power);
	            ServiceManager.addService(Context.WIFIDISPLAY_SERVICE, wifidisplay);
			}

            ActivityManagerService.self().setWindowManager(wm);

            // Skip Bluetooth if we have an emulator kernel
            // TODO: Use a more reliable check to see if this product should
            // support Bluetooth - see bug 988521
            if (SystemProperties.get("ro.kernel.qemu").equals("1")) {
                Slog.i(TAG, "No Bluetooh Service (emulator)");
            } else if (factoryTest == SystemServer.FACTORY_TEST_LOW_LEVEL) {
                Slog.i(TAG, "No Bluetooth Service (factory test)");
            } else {
                Slog.i(TAG, "Bluetooth Service");
                bluetooth = new BluetoothService(context);
                ServiceManager.addService(BluetoothAdapter.BLUETOOTH_SERVICE, bluetooth);
                bluetooth.initAfterRegistration();
                bluetoothA2dp = new BluetoothA2dpService(context, bluetooth);
                ServiceManager.addService(BluetoothA2dpService.BLUETOOTH_A2DP_SERVICE,
                                          bluetoothA2dp);
                bluetooth.initAfterA2dpRegistration();

                int airplaneModeOn = Settings.System.getInt(mContentResolver,
                        Settings.System.AIRPLANE_MODE_ON, 0);
                int bluetoothOn = Settings.Secure.getInt(mContentResolver,
                    Settings.Secure.BLUETOOTH_ON, 0);
                if (airplaneModeOn == 0 && bluetoothOn != 0) {
                    bluetooth.enable();
                }
            }

        } catch (RuntimeException e) {
            Slog.e("System", "******************************************");
            Slog.e("System", "************ Failure starting core service", e);
        }

        DevicePolicyManagerService devicePolicy = null;
        StatusBarManagerService statusBar = null;
        InputMethodManagerService imm = null;
        AppWidgetService appWidget = null;
        NotificationManagerService notification = null;
        WallpaperManagerService wallpaper = null;
        LocationManagerService location = null;
        CountryDetectorService countryDetector = null;
        TextServicesManagerService tsms = null;

        // Bring up services needed for UI.
        if (factoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
            try {
                Slog.i(TAG, "Input Method Service");
                imm = new InputMethodManagerService(context);
                ServiceManager.addService(Context.INPUT_METHOD_SERVICE, imm);
            } catch (Throwable e) {
                reportWtf("starting Input Manager Service", e);
            }

            try {
                Slog.i(TAG, "Accessibility Manager");
                ServiceManager.addService(Context.ACCESSIBILITY_SERVICE,
                        new AccessibilityManagerService(context));
            } catch (Throwable e) {
                reportWtf("starting Accessibility Manager", e);
            }
        }

        try {
            wm.displayReady();
        } catch (Throwable e) {
            reportWtf("making display ready", e);
        }

        try {
            pm.performBootDexOpt();
        } catch (Throwable e) {
            reportWtf("performing boot dexopt", e);
        }

        try {
            ActivityManagerNative.getDefault().showBootMessage(
                    context.getResources().getText(
                            com.android.internal.R.string.android_upgrading_starting_apps),
                            false);
        } catch (RemoteException e) {
        }

        if (factoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
            try {
                Slog.i(TAG, "Device Policy");
                devicePolicy = new DevicePolicyManagerService(context);
                ServiceManager.addService(Context.DEVICE_POLICY_SERVICE, devicePolicy);
            } catch (Throwable e) {
                reportWtf("starting DevicePolicyService", e);
            }

            try {
                Slog.i(TAG, "Status Bar");
                statusBar = new StatusBarManagerService(context, wm);
                ServiceManager.addService(Context.STATUS_BAR_SERVICE, statusBar);
            } catch (Throwable e) {
                reportWtf("starting StatusBarManagerService", e);
            }

            try {
                Slog.i(TAG, "Clipboard Service");
                ServiceManager.addService(Context.CLIPBOARD_SERVICE,
                        new ClipboardService(context));
            } catch (Throwable e) {
                reportWtf("starting Clipboard Service", e);
            }

            try {
                Slog.i(TAG, "NetworkManagement Service");
                networkManagement = NetworkManagementService.create(context);
                ServiceManager.addService(Context.NETWORKMANAGEMENT_SERVICE, networkManagement);
            } catch (Throwable e) {
                reportWtf("starting NetworkManagement Service", e);
            }

            try {
                Slog.i(TAG, "Text Service Manager Service");
                tsms = new TextServicesManagerService(context);
                ServiceManager.addService(Context.TEXT_SERVICES_MANAGER_SERVICE, tsms);
            } catch (Throwable e) {
                reportWtf("starting Text Service Manager Service", e);
            }

            try {
                Slog.i(TAG, "NetworkStats Service");
                networkStats = new NetworkStatsService(context, networkManagement, alarm);
                ServiceManager.addService(Context.NETWORK_STATS_SERVICE, networkStats);
            } catch (Throwable e) {
                reportWtf("starting NetworkStats Service", e);
            }

            try {
                Slog.i(TAG, "NetworkPolicy Service");
                networkPolicy = new NetworkPolicyManagerService(
                        context, ActivityManagerService.self(), power,
                        networkStats, networkManagement);
                ServiceManager.addService(Context.NETWORK_POLICY_SERVICE, networkPolicy);
            } catch (Throwable e) {
                reportWtf("starting NetworkPolicy Service", e);
            }

           try {
                Slog.i(TAG, "Wi-Fi P2pService");
                wifiP2p = new WifiP2pService(context);
                ServiceManager.addService(Context.WIFI_P2P_SERVICE, wifiP2p);
            } catch (Throwable e) {
                reportWtf("starting Wi-Fi P2pService", e);
            }

           try {
                Slog.i(TAG, "Wi-Fi Service");
                wifi = new WifiService(context);
                ServiceManager.addService(Context.WIFI_SERVICE, wifi);
            } catch (Throwable e) {
                reportWtf("starting Wi-Fi Service", e);
            }

		   /* Begin (add by shuge@allwinnertech.com) */
           try {
                Slog.i(TAG, "Ethernet Service");
                ethernet = new EthernetService(context);
                ServiceManager.addService(Context.ETHERNET_SERVICE, ethernet);
            } catch (Throwable e) {
                reportWtf("starting Ethernet Service", e);
            }
		   /* End (add by shuge@allwinnertech.com) */

            try {
                Slog.i(TAG, "Connectivity Service");
                connectivity = new ConnectivityService(
                        context, networkManagement, networkStats, networkPolicy);
                ServiceManager.addService(Context.CONNECTIVITY_SERVICE, connectivity);
                networkStats.bindConnectivityManager(connectivity);
                networkPolicy.bindConnectivityManager(connectivity);
                wifi.checkAndStartWifi();
                wifiP2p.connectivityServiceReady();
            } catch (Throwable e) {
                reportWtf("starting Connectivity Service", e);
            }

            try {
                Slog.i(TAG, "Throttle Service");
                throttle = new ThrottleService(context);
                ServiceManager.addService(
                        Context.THROTTLE_SERVICE, throttle);
            } catch (Throwable e) {
                reportWtf("starting ThrottleService", e);
            }

            try {
                /*
                 * NotificationManagerService is dependant on MountService,
                 * (for media / usb notifications) so we must start MountService first.
                 */
                Slog.i(TAG, "Mount Service");
                ServiceManager.addService("mount", new MountService(context));
            } catch (Throwable e) {
                reportWtf("starting Mount Service", e);
            }

            try {
                Slog.i(TAG, "Notification Manager");
                notification = new NotificationManagerService(context, statusBar, lights);
                ServiceManager.addService(Context.NOTIFICATION_SERVICE, notification);
                networkPolicy.bindNotificationManager(notification);
            } catch (Throwable e) {
                reportWtf("starting Notification Manager", e);
            }

            try {
                Slog.i(TAG, "Device Storage Monitor");
                ServiceManager.addService(DeviceStorageMonitorService.SERVICE,
                        new DeviceStorageMonitorService(context));
            } catch (Throwable e) {
                reportWtf("starting DeviceStorageMonitor service", e);
            }

            try {
                Slog.i(TAG, "Location Manager");
                location = new LocationManagerService(context);
                ServiceManager.addService(Context.LOCATION_SERVICE, location);
            } catch (Throwable e) {
                reportWtf("starting Location Manager", e);
            }

            try {
                Slog.i(TAG, "Country Detector");
                countryDetector = new CountryDetectorService(context);
                ServiceManager.addService(Context.COUNTRY_DETECTOR, countryDetector);
            } catch (Throwable e) {
                reportWtf("starting Country Detector", e);
            }

            try {
                Slog.i(TAG, "Search Service");
                ServiceManager.addService(Context.SEARCH_SERVICE,
                        new SearchManagerService(context));
            } catch (Throwable e) {
                reportWtf("starting Search Service", e);
            }

            try {
                Slog.i(TAG, "DropBox Service");
                ServiceManager.addService(Context.DROPBOX_SERVICE,
                        new DropBoxManagerService(context, new File("/data/system/dropbox")));
            } catch (Throwable e) {
                reportWtf("starting DropBoxManagerService", e);
            }

            try {
                Slog.i(TAG, "Wallpaper Service");
                wallpaper = new WallpaperManagerService(context);
                ServiceManager.addService(Context.WALLPAPER_SERVICE, wallpaper);
            } catch (Throwable e) {
                reportWtf("starting Wallpaper Service", e);
            }

            try {
                Slog.i(TAG, "Audio Service");
                ServiceManager.addService(Context.AUDIO_SERVICE, new AudioService(context));
            } catch (Throwable e) {
                reportWtf("starting Audio Service", e);
            }

            try {
                Slog.i(TAG, "Dock Observer");
                // Listen for dock station changes
                dock = new DockObserver(context, power);
            } catch (Throwable e) {
                reportWtf("starting DockObserver", e);
            }

            try {
                Slog.i(TAG, "Wired Accessory Observer");
                // Listen for wired headset changes
                new WiredAccessoryObserver(context);
            } catch (Throwable e) {
                reportWtf("starting WiredAccessoryObserver", e);
            }

			try {
				Slog.i(TAG, "Audio device manager Observer");
				// Listen for wired headset changes
				new AudioDeviceManagerObserver(context);
			} catch (Throwable e) {
				reportWtf("starting AudioDeviceManagerObserver", e);
			}

            try {
                Slog.i(TAG, "USB Service");
                // Manage USB host and device support
                usb = new UsbService(context);
                ServiceManager.addService(Context.USB_SERVICE, usb);
            } catch (Throwable e) {
                reportWtf("starting UsbService", e);
            }

            try {
                Slog.i(TAG, "UI Mode Manager Service");
                // Listen for UI mode changes
                uiMode = new UiModeManagerService(context);
            } catch (Throwable e) {
                reportWtf("starting UiModeManagerService", e);
            }

            try {
                Slog.i(TAG, "Backup Service");
                ServiceManager.addService(Context.BACKUP_SERVICE,
                        new BackupManagerService(context));
            } catch (Throwable e) {
                Slog.e(TAG, "Failure starting Backup Service", e);
            }

            try {
                Slog.i(TAG, "AppWidget Service");
                appWidget = new AppWidgetService(context);
                ServiceManager.addService(Context.APPWIDGET_SERVICE, appWidget);
            } catch (Throwable e) {
                reportWtf("starting AppWidget Service", e);
            }

            try {
                Slog.i(TAG, "Recognition Service");
                recognition = new RecognitionManagerService(context);
            } catch (Throwable e) {
                reportWtf("starting Recognition Service", e);
            }

            try {
                Slog.i(TAG, "DiskStats Service");
                ServiceManager.addService("diskstats", new DiskStatsService(context));
            } catch (Throwable e) {
                reportWtf("starting DiskStats Service", e);
            }

            try {
                // need to add this service even if SamplingProfilerIntegration.isEnabled()
                // is false, because it is this service that detects system property change and
                // turns on SamplingProfilerIntegration. Plus, when sampling profiler doesn't work,
                // there is little overhead for running this service.
                Slog.i(TAG, "SamplingProfiler Service");
                ServiceManager.addService("samplingprofiler",
                            new SamplingProfilerService(context));
            } catch (Throwable e) {
                reportWtf("starting SamplingProfiler Service", e);
            }

            try {
                Slog.i(TAG, "NetworkTimeUpdateService");
                networkTimeUpdater = new NetworkTimeUpdateService(context);
            } catch (Throwable e) {
                reportWtf("starting NetworkTimeUpdate service", e);
            }
        }

        // Before things start rolling, be sure we have decided whether
        // we are in safe mode.
        final boolean safeMode = wm.detectSafeMode();
        if (safeMode) {
            ActivityManagerService.self().enterSafeMode();
            // Post the safe mode state in the Zygote class
            Zygote.systemInSafeMode = true;
            // Disable the JIT for the system_server process
            VMRuntime.getRuntime().disableJitCompilation();
        } else {
            // Enable the JIT for the system_server process
            VMRuntime.getRuntime().startJitCompilation();
        }

        // It is now time to start up the app processes...

        if (devicePolicy != null) {
            try {
                devicePolicy.systemReady();
            } catch (Throwable e) {
                reportWtf("making Device Policy Service ready", e);
            }
        }

        if (notification != null) {
            try {
                notification.systemReady();
            } catch (Throwable e) {
                reportWtf("making Notification Service ready", e);
            }
        }

        try {
            wm.systemReady();
        } catch (Throwable e) {
            reportWtf("making Window Manager Service ready", e);
        }

        if (safeMode) {
            ActivityManagerService.self().showSafeModeOverlay();
        }

        // Update the configuration for this context by hand, because we're going
        // to start using it before the config change done in wm.systemReady() will
        // propagate to it.
        Configuration config = wm.computeNewConfiguration();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager w = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        w.getDefaultDisplay().getMetrics(metrics);
        context.getResources().updateConfiguration(config, metrics);

        power.systemReady();
        try {
            pm.systemReady();
        } catch (Throwable e) {
            reportWtf("making Package Manager Service ready", e);
        }

        // These are needed to propagate to the runnable below.
        final Context contextF = context;
        final BatteryService batteryF = battery;
        final NetworkManagementService networkManagementF = networkManagement;
        final NetworkStatsService networkStatsF = networkStats;
        final NetworkPolicyManagerService networkPolicyF = networkPolicy;
        final ConnectivityService connectivityF = connectivity;
        final DockObserver dockF = dock;
        final UsbService usbF = usb;
        final ThrottleService throttleF = throttle;
        final UiModeManagerService uiModeF = uiMode;
        final AppWidgetService appWidgetF = appWidget;
        final WallpaperManagerService wallpaperF = wallpaper;
        final InputMethodManagerService immF = imm;
        final RecognitionManagerService recognitionF = recognition;
        final LocationManagerService locationF = location;
        final CountryDetectorService countryDetectorF = countryDetector;
        final NetworkTimeUpdateService networkTimeUpdaterF = networkTimeUpdater;
        final TextServicesManagerService textServiceManagerServiceF = tsms;
        final StatusBarManagerService statusBarF = statusBar;

        // We now tell the activity manager it is okay to run third party
        // code.  It will call back into us once it has gotten to the state
        // where third party code can really run (but before it has actually
        // started launching the initial applications), for us to complete our
        // initialization.
        ActivityManagerService.self().systemReady(new Runnable() {
            public void run() {
                Slog.i(TAG, "Making services ready");

                startSystemUi(contextF);
                try {
                    if (batteryF != null) batteryF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Battery Service ready", e);
                }
                try {
                    if (networkManagementF != null) networkManagementF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Network Managment Service ready", e);
                }
                try {
                    if (networkStatsF != null) networkStatsF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Network Stats Service ready", e);
                }
                try {
                    if (networkPolicyF != null) networkPolicyF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Network Policy Service ready", e);
                }
                try {
                    if (connectivityF != null) connectivityF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Connectivity Service ready", e);
                }
                try {
                    if (dockF != null) dockF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Dock Service ready", e);
                }
                try {
                    if (usbF != null) usbF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making USB Service ready", e);
                }
                try {
                    if (uiModeF != null) uiModeF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making UI Mode Service ready", e);
                }
                try {
                    if (recognitionF != null) recognitionF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Recognition Service ready", e);
                }
                Watchdog.getInstance().start();

                // It is now okay to let the various system services start their
                // third party code...

                try {
                    if (appWidgetF != null) appWidgetF.systemReady(safeMode);
                } catch (Throwable e) {
                    reportWtf("making App Widget Service ready", e);
                }
                try {
                    if (wallpaperF != null) wallpaperF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Wallpaper Service ready", e);
                }
                try {
                    if (immF != null) immF.systemReady(statusBarF);
                } catch (Throwable e) {
                    reportWtf("making Input Method Service ready", e);
                }
                try {
                    if (locationF != null) locationF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Location Service ready", e);
                }
                try {
                    if (countryDetectorF != null) countryDetectorF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Country Detector Service ready", e);
                }
                try {
                    if (throttleF != null) throttleF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Throttle Service ready", e);
                }
                try {
                    if (networkTimeUpdaterF != null) networkTimeUpdaterF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Network Time Service ready", e);
                }
                try {
                    if (textServiceManagerServiceF != null) textServiceManagerServiceF.systemReady();
                } catch (Throwable e) {
                    reportWtf("making Text Services Manager Service ready", e);
                }
            }
        });

        /* add by Gary. start {{----------------------------------- */
        /* 2012-05-30 */
        /* modify for a10s */
        String chipType = SystemProperties.get(ProductConfig.CHIP_TYPE);
        if(chipType == null){
            Log.w(TAG, "Fail in getting the value of property " + ProductConfig.CHIP_TYPE);
            chipType = ProductConfig.CHIP_TYPE_DEFAULT;
        }
        Log.d(TAG, "property " + ProductConfig.CHIP_TYPE + " is " + chipType);
        if(chipType.equals(ProductConfig.CHIP_TYPE_A10S)){
            DisplayManager displayManager = new DisplayManager();
            if(displayManager == null)
                Log.w(TAG, "Fail in creating DisplayManager.");
            DispList.DispFormat savedFormat = null;
            String savedFormatName = Settings.System.getString(context.getContentResolver(), Settings.System.DISPLY_OUTPUT_FORMAT);
            if(savedFormatName == null){
                Log.w(TAG, "Fail in getting saved display format.");
                savedFormat = DispList.HDMI_DEFAULT_FORMAT;
            }else{
                savedFormat = DispList.ItemName2Code(savedFormatName);
            }
            Log.d(TAG, "saved display = " + DispList.ItemCode2Name(savedFormat));
            DispList.DispFormat finalFormat = savedFormat;

            if(finalFormat.mOutputType == DisplayManager.DISPLAY_OUTPUT_TYPE_HDMI){
                boolean isSupport = (displayManager.isSupportHdmiMode(finalFormat.mFormat) != 0);
                if(!isSupport){
                    Log.d(TAG, "HDMI mode " + DispList.ItemCode2Name(finalFormat) + " is NOT supported by the TV.");
                    finalFormat = DispList.HDMI_DEFAULT_FORMAT;
                }
            }
            Log.d(TAG, "final format is " + DispList.ItemCode2Name(finalFormat));
            displayManager.setDisplayOutputType(0, finalFormat.mOutputType, finalFormat.mFormat);
            Settings.System.putString(context.getContentResolver(), Settings.System.DISPLY_OUTPUT_FORMAT,
                                      DispList.ItemCode2Name(finalFormat));

            /* init audio output channel */
            AudioManager audioManager = new AudioManager(context);
            if(audioManager == null){
                Log.e(TAG, "audioManager is null");
            }else {
            	/*modified by chenjd,chenjd@allwinnertech.com,20120711,start {{---------- */
            	String audioOutputChannels = Settings.System.getString(context.getContentResolver(),
					Settings.System.AUDIO_OUTPUT_CHANNEL);
				if(audioOutputChannels == null) {
					Log.d(TAG,"use default audio output channel:" + audioOutputChannels);
					audioOutputChannels = AudioManager.AUDIO_NAME_CODEC;
				}
				Log.d(TAG, "saved audio channel is " + audioOutputChannels);
				ArrayList<String> audioDevList = new ArrayList<String>();
				String[] audioList = audioOutputChannels.split(",");
				for(String audio:audioList){
					if(!"".equals(audio)){
						if(audio.contains("HDMI")){
							audioDevList.add(AudioManager.AUDIO_NAME_HDMI);
						}else if(audio.contains("CODEC")){
							audioDevList.add(AudioManager.AUDIO_NAME_CODEC);
						}else if(audio.contains("SPDIF")){
							audioDevList.add(AudioManager.AUDIO_NAME_SPDIF);
						}
					}
				}
				audioManager.setAudioDeviceActive(audioDevList,AudioManager.AUDIO_OUTPUT_ACTIVE);
				/* record the audio output channel */
				String st = null;
				for(int i = 0; i < audioDevList.size(); i++){
					if(st == null){
						st = audioDevList.get(i);
					}
					else{
						st = st + "," + audioDevList.get(i);
					}
				}
				Settings.System.putString(context.getContentResolver(), Settings.System.AUDIO_OUTPUT_CHANNEL,st);
				Log.d(TAG,"audio channels change to {" + st + "}");
				/*modified by chenjd,end --------------}} */

				/*
                String audioOutputChannelName = Settings.System.getString(context.getContentResolver(),
                                                                          Settings.System.AUDIO_OUTPUT_CHANNEL);
                Log.d(TAG, "saved audio channel is " + audioOutputChannelName);
                int audioOutputChannel = AudioManager.auidoOutputChannelName2Code(audioOutputChannelName);
                audioManager.switchAudioOutMode(audioOutputChannel);
                /* record the audio output channel */
				/*
                Settings.System.putString(context.getContentResolver(), Settings.System.AUDIO_OUTPUT_CHANNEL,
                                          AudioManager.auidoOutputChannelCode2Name(audioOutputChannel));
                                          */
            }
        } else {
            /* add by Gary. start {{----------------------------------- */
            /* 2011-12-10 */
            /* init display output */
            boolean ypbprIsConnected = false;
            boolean cvbsIsConnected = false;
            boolean hdmiIsConnected = false;
            DispList.DispFormat finalFormat = null;

//            DisplayManager displayManager = (DisplayManager)context.getSystemService(Context.DISPLAY_SERVICE);
            DisplayManager displayManager = new DisplayManager();
            if(displayManager == null)
                Log.w(TAG, "Fail in creating DisplayManager.");

            /* check cable's connection status */
            if(displayManager.getHdmiHotPlugStatus() != 0)
                hdmiIsConnected = true;

            int tvStatus = displayManager.getTvHotPlugStatus();
            Log.d(TAG, "tv connect status = " + tvStatus);
            if(tvStatus == DisplayManager.DISPLAY_TVDAC_CVBS)
                cvbsIsConnected = true;
            else if(tvStatus == DisplayManager.DISPLAY_TVDAC_YPBPR)
                ypbprIsConnected = true;
            else if(tvStatus == DisplayManager.DISPLAY_TVDAC_ALL){
                cvbsIsConnected = true;
                ypbprIsConnected = true;
            }
            Log.d(TAG, "HDMI connect status = " + hdmiIsConnected + ", av connnect status = "
                  + cvbsIsConnected + ", YPbPr connect status = " + ypbprIsConnected);

            DispList.DispFormat savedFormat = null;
            String savedFormatName = Settings.System.getString(context.getContentResolver(), Settings.System.DISPLY_OUTPUT_FORMAT);
            if(savedFormatName == null){
                Log.w(TAG, "Fail in getting saved display format.");
                savedFormat = DispList.HDMI_DEFAULT_FORMAT;
            }else{
                savedFormat = DispList.ItemName2Code(savedFormatName);
            }
            Log.d(TAG, "saved display = " + DispList.ItemCode2Name(savedFormat));
            int savedType = savedFormat.mOutputType;
            DispList.DispFormat curFormat = new DispList.DispFormat(displayManager.getDisplayOutputType(0),
                                                                    displayManager.getDisplayOutputFormat(0));
            Log.d(TAG, "current format is " + curFormat);

            int savedIntType = DispList.getAdvancedDisplayType(savedFormat);
            int finalIntType = DispList.ADVANCED_DISPLAY_TYPE_HDMI;
            if(hdmiIsConnected && DispList.isHDMI(curFormat)){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_HDMI;
                finalFormat = curFormat;
            }else if(cvbsIsConnected && DispList.isCVBS(curFormat)){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_CVBS;
                finalFormat = curFormat;
            }else if(mDeviceHasYpbpr && ypbprIsConnected && DispList.isYPbPr(curFormat)){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_YPBPR;
                finalFormat = curFormat;
            }else if(hdmiIsConnected){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_HDMI;
                finalFormat = DispList.HDMI_DEFAULT_FORMAT;
            }else if(cvbsIsConnected){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_CVBS;
                finalFormat = DispList.CVBS_DEFAULT_FORMAT;
            }else if(mDeviceHasYpbpr && ypbprIsConnected){
                finalIntType = DispList.ADVANCED_DISPLAY_TYPE_YPBPR;
                finalFormat = DispList.YPBPR_DEFAULT_FORMAT;
            }else {
                finalIntType = savedIntType;
                finalFormat = savedFormat;
            }

            if(finalIntType == savedIntType)
                finalFormat = savedFormat;

            if(finalFormat.mOutputType == DisplayManager.DISPLAY_OUTPUT_TYPE_HDMI){
                boolean isSupport = (displayManager.isSupportHdmiMode(finalFormat.mFormat) != 0);
                if(!isSupport){
                    Log.d(TAG, "HDMI mode " + DispList.ItemCode2Name(finalFormat) + " is NOT supported by the TV.");
                    finalFormat = DispList.HDMI_DEFAULT_FORMAT;
                }
            }
            Log.d(TAG, "final format is " + DispList.ItemCode2Name(finalFormat));
            if(    curFormat.mOutputType != finalFormat.mOutputType
                || curFormat.mFormat != finalFormat.mFormat ){
//                displayManager.setDisplayOutputType(0, finalFormat.mOutputType, finalFormat.mFormat);
      	        displayManager.setDisplayParameter(0, finalFormat.mOutputType, finalFormat.mFormat);
		        displayManager.setDisplayMode(DisplayManager.getDisplayModeForIC());
            }

            Settings.System.putString(context.getContentResolver(), Settings.System.DISPLY_OUTPUT_FORMAT,
                                      DispList.ItemCode2Name(finalFormat));

            /* init the display area */
            int ratio = Settings.System.getInt(context.getContentResolver(), Settings.System.DISPLAY_AREA_RATIO, 95);
            Log.d(TAG, "display area ratio = " + ratio);
            displayManager.setDisplayAreaPercent(0, ratio);
            /* add by Gary. end   -----------------------------------}} */

            /* add by Gary. start {{----------------------------------- */
            /* 2011-11-28 */
            /* init audio output channel */
            Log.d(TAG, "to init audio output");

    //        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            AudioManager audioManager = new AudioManager(context);
            if(audioManager == null){
                Log.e(TAG, "audioManager is null");
            }else {
				/* modified by chenjd,chenjd@allwinnertech.com,20120710, start {{--------
				 * init audio output mode,may be multi channels */
				String audioOutputChannelName = Settings.System.getString(context.getContentResolver(),
                                                                          Settings.System.AUDIO_OUTPUT_CHANNEL);
				if(audioOutputChannelName == null) {
					Log.d(TAG,"use default audio output channel:" + audioOutputChannelName);
					audioOutputChannelName = AudioManager.AUDIO_NAME_CODEC;
				}
				Log.d(TAG, "saved audio channel is " + audioOutputChannelName);
				ArrayList<String> audioOutputChannels = new ArrayList<String>();
				String[] audioList = audioOutputChannelName.split(",");
				for(String audio:audioList){
					if(!"".equals(audio)){
						audioOutputChannels.add(audio);
					}
				}
				Log.d(TAG, "active the saved audio channels " + audioOutputChannels);
				if(displayManager.getDisplayOutputType(0) == DisplayManager.DISPLAY_OUTPUT_TYPE_HDMI){
					if(!audioOutputChannels.contains(AudioManager.AUDIO_NAME_SPDIF) &&
						!audioOutputChannels.contains(AudioManager.AUDIO_NAME_HDMI)){
						audioOutputChannels.clear();
						audioOutputChannels.add(AudioManager.AUDIO_NAME_HDMI);
					}
				}else{
					if(!audioOutputChannels.contains(AudioManager.AUDIO_NAME_SPDIF) &&
						!audioOutputChannels.contains(AudioManager.AUDIO_NAME_CODEC)){
						audioOutputChannels.clear();
						audioOutputChannels.add(AudioManager.AUDIO_NAME_CODEC);

					}
				}
				audioManager.setAudioDeviceActive(audioOutputChannels,AudioManager.AUDIO_OUTPUT_ACTIVE);
				/* record the audio output channel */
				String st = null;
				for(int i = 0; i < audioOutputChannels.size(); i++){
					if(st == null){
						st = audioOutputChannels.get(i);
					}
					else{
						st = st + "," + audioOutputChannels.get(i);
					}
				}
				Settings.System.putString(context.getContentResolver(), Settings.System.AUDIO_OUTPUT_CHANNEL,st);
				Log.d(TAG,"audio channels change to {" + st + "}");
				/*
                String audioOutputChannelName = Settings.System.getString(context.getContentResolver(),
                                                                          Settings.System.AUDIO_OUTPUT_CHANNEL);
                Log.d(TAG, "saved audio channel is " + audioOutputChannelName);
                int audioOutputChannel = AudioManager.auidoOutputChannelName2Code(audioOutputChannelName);
                if(displayManager.getDisplayOutputType(0) == DisplayManager.DISPLAY_OUTPUT_TYPE_HDMI){
                    if(audioOutputChannel != AudioManager.AUDIO_OUT_CHANNEL_SPDIF)
                        audioOutputChannel = AudioManager.AUDIO_OUT_CHANNEL_HDMI;
                }else{
                    if(audioOutputChannel != AudioManager.AUDIO_OUT_CHANNEL_SPDIF)
                        audioOutputChannel = AudioManager.AUDIO_OUT_CHANNEL_CODEC;
                }
                audioManager.switchAudioOutMode(audioOutputChannel);
                */
                /* record the audio output channel */
				/*
                Settings.System.putString(context.getContentResolver(), Settings.System.AUDIO_OUTPUT_CHANNEL,
                                          AudioManager.auidoOutputChannelCode2Name(audioOutputChannel));
				*/
				/* modified by chenjd,chenjd@allwinnertech.com,20120710, end ------}} */
			}
            /* add by Gary. end   -----------------------------------}} */

            /* add by Gary. start {{----------------------------------- */
            /* 2011-12-11 */
            /* init the color parameters : brightness, contrast,and saturation */
            int value;
            value = Settings.System.getInt(context.getContentResolver(), Settings.System.COLOR_BRIGHTNESS, 50);
            Log.d(TAG, "color brightness = " + value);
            displayManager.setDisplayBright(0, value);
            value = Settings.System.getInt(context.getContentResolver(), Settings.System.COLOR_CONTRAST, 50);
            Log.d(TAG, "color contrast = " + value);
            displayManager.setDisplayContrast(0, value);
            value = Settings.System.getInt(context.getContentResolver(), Settings.System.COLOR_SATURATION, 50);
            Log.d(TAG, "color saturation = " + value);
            displayManager.setDisplaySaturation(0, value);
            /* add by Gary. end   -----------------------------------}} */
        }

        // For debug builds, log event loop stalls to dropbox for analysis.
        if (StrictMode.conditionallyEnableDebugLogging()) {
            Slog.i(TAG, "Enabled StrictMode for system server main thread.");
        }

		/* add by yangfuyang start the google tv remote server*/
			new GoogleRemoteService(context).startServer();

        Looper.loop();
        Slog.d(TAG, "System ServerThread is exiting!");
    }

    static final void startSystemUi(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.systemui",
                    "com.android.systemui.SystemUIService"));
        Slog.d(TAG, "Starting service: " + intent);
        context.startService(intent);
    }
}

public class SystemServer {
    private static final String TAG = "SystemServer";

    public static final int FACTORY_TEST_OFF = 0;
    public static final int FACTORY_TEST_LOW_LEVEL = 1;
    public static final int FACTORY_TEST_HIGH_LEVEL = 2;

    static Timer timer;
    static final long SNAPSHOT_INTERVAL = 60 * 60 * 1000; // 1hr

    // The earliest supported time.  We pick one day into 1970, to
    // give any timezone code room without going into negative time.
    private static final long EARLIEST_SUPPORTED_TIME = 86400 * 1000;

    /**
     * This method is called from Zygote to initialize the system. This will cause the native
     * services (SurfaceFlinger, AudioFlinger, etc..) to be started. After that it will call back
     * up into init2() to start the Android services.
     */
    native public static void init1(String[] args);

    public static void main(String[] args) {
        if (System.currentTimeMillis() < EARLIEST_SUPPORTED_TIME) {
            // If a device's clock is before 1970 (before 0), a lot of
            // APIs crash dealing with negative numbers, notably
            // java.io.File#setLastModified, so instead we fake it and
            // hope that time from cell towers or NTP fixes it
            // shortly.
            Slog.w(TAG, "System clock is before 1970; setting to 1970.");
            SystemClock.setCurrentTimeMillis(EARLIEST_SUPPORTED_TIME);
        }

        if (SamplingProfilerIntegration.isEnabled()) {
            SamplingProfilerIntegration.start();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SamplingProfilerIntegration.writeSnapshot("system_server", null);
                }
            }, SNAPSHOT_INTERVAL, SNAPSHOT_INTERVAL);
        }

        // Mmmmmm... more memory!
        dalvik.system.VMRuntime.getRuntime().clearGrowthLimit();

        // The system server has to run all of the time, so it needs to be
        // as efficient as possible with its memory usage.
        VMRuntime.getRuntime().setTargetHeapUtilization(0.8f);

        System.loadLibrary("android_servers");
        init1(args);
    }

    public static final void init2() {
        Slog.i(TAG, "Entered the Android system server!");
        Thread thr = new ServerThread();
        thr.setName("android.server.ServerThread");
        thr.start();
    }
}
