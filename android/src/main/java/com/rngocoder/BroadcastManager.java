package com.rngocoder;

import android.content.Context;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WOWZCameraView;
import com.wowza.gocoder.sdk.api.errors.WOWZStreamingError;
import com.wowza.gocoder.sdk.api.logging.WOWZLog;


import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback;


public class BroadcastManager {
    public static WOWZBroadcastConfig initBroadcast(Context localContext, String hostAddress, String applicationName, String broadcastName, String sdkLicenseKey, String username, String password,int sizePreset, String videoOrientation, WOWZCameraView cameraView, WOWZAudioDevice audioDevice){
        WowzaGoCoder.init(localContext, sdkLicenseKey);
        WOWZBroadcastConfig broadcastConfig = new WOWZBroadcastConfig();
        broadcastConfig.setOrientationBehavior(getOrientationBehavior(videoOrientation));
        broadcastConfig.setVideoFramerate(12);

        WOWZMediaConfig mediaConfig = getSizePresetWithInt(sizePreset);
        broadcastConfig.setVideoSourceConfig(mediaConfig);

        broadcastConfig.setVideoBroadcaster(cameraView);
        broadcastConfig.setAudioBroadcaster(audioDevice);
        broadcastConfig.setAudioBitRate(22400);

        broadcastConfig.setHostAddress(hostAddress);
        broadcastConfig.setUsername(username);
        broadcastConfig.setPassword(password);
        broadcastConfig.setApplicationName(applicationName);
        broadcastConfig.setStreamName(broadcastName);

        return broadcastConfig;
    }

    public static void startBroadcast(WOWZBroadcast broadcast, WOWZBroadcastConfig broadcastConfig, WOWZStatusCallback callback) {
        if (!broadcast.getStatus().isRunning()) {
            // Validate the current broadcast config
            WOWZStreamingError configValidationError = broadcastConfig.validateForBroadcast();
            if (configValidationError != null) {
                WOWZLog.error(configValidationError);
            } else {
                // Start the live stream
                broadcast.startBroadcast(broadcastConfig, callback);
            }
        }
    }
    public static void stopBroadcast(WOWZBroadcast broadcast, WOWZStatusCallback callback){
        if (broadcast.getStatus().isRunning()) {
            // Stop the live stream
            broadcast.endBroadcast(callback);
        }
    }
    public static void invertCamera(WOWZCameraView cameraView) {
        cameraView.switchCamera();
    }

    public static void turnFlash(WOWZCameraView cameraView, boolean on) {
        cameraView.getCamera().setTorchOn(on);

    }

    public static void mute(WOWZAudioDevice audioDevice, boolean muted){
        audioDevice.setMuted(muted);
    }

    public static void changeStreamName(WOWZBroadcastConfig broadcastConfig, String broadcastName){
        broadcastConfig.setStreamName(broadcastName);
    }

    private static WOWZMediaConfig getSizePresetWithInt(int sizePreset){
        switch (sizePreset){
            case 0: //FRAME_SIZE_176x144
                return WOWZMediaConfig.FRAME_SIZE_176x144;
            case 1: //FRAME_SIZE_320x240
                return WOWZMediaConfig.FRAME_SIZE_320x240;
            case 2: //FRAME_SIZE_352x288
                return WOWZMediaConfig.FRAME_SIZE_352x288;
            case 3: //FRAME_SIZE_640x480
                return WOWZMediaConfig.FRAME_SIZE_640x480;
            case 4: //FRAME_SIZE_960x540
                return WOWZMediaConfig.FRAME_SIZE_960x540;
            case 5: //FRAME_SIZE_1280x720
                return WOWZMediaConfig.FRAME_SIZE_1280x720;
            case 6: //FRAME_SIZE_1440x1080
                return WOWZMediaConfig.FRAME_SIZE_1440x1080;
            case 7: //FRAME_SIZE_1920x1080
                return WOWZMediaConfig.FRAME_SIZE_1920x1080;
            case 8: //FRAME_SIZE_3840x2160
                return  WOWZMediaConfig.FRAME_SIZE_3840x2160;
            default:
                return WOWZMediaConfig.FRAME_SIZE_640x480;
        }
    }

    private static int getOrientationBehavior(String orientation) {
//        switch (orientation) {
//            case "landscape":
//                return WOWZMediaConfig.ALWAYS_LANDSCAPE;
//            case "portrait":
//                return WOWZMediaConfig.ALWAYS_PORTRAIT;
//            default:
                return WOWZMediaConfig.SAME_AS_SOURCE;
//        }
    }
}
