package com.kt.securcetokenstorage.device;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class AnomalyDetector {
    public boolean isDebuggerAttached() {
        return android.os.Debug.isDebuggerConnected();
    }

    public boolean isEmulator() {
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String fingerprint = Build.FINGERPRINT;
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        String product = Build.PRODUCT;

        return brand.startsWith("generic") || device.startsWith("generic") ||
                fingerprint.contains("generic") || model.contains("Emulator") ||
                manufacturer.contains("Genymotion") || product.contains("sdk");
    }

    public boolean isDeviceRooted() {
        return checkRootFiles() || checkRootCommands() || checkSuBinary();
    }

    private boolean checkRootFiles() {
        String[] rootPaths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
        };
        for (String path : rootPaths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSuBinary() {
        String suBinaryPath = "/system/xbin/su";
        return new File(suBinaryPath).exists();
    }

    private boolean checkRootCommands() {
        String[] commands = {"which su", "which busybox"};
        for (String command : commands) {
            try {
                Process process = Runtime.getRuntime().exec(command);
                if (process != null) {
                    return true;
                }
            } catch (Exception e) {
                Log.e("RootChecker", "Error executing command: " + command, e);
            }
        }
        return false;
    }

    public boolean isSignatureValid(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            // Replace with your original app signature
            String originalSignature = "YOUR_ORIGINAL_SIGNATURE";
            for (Signature signature : signatures) {
                if (signature.toCharsString().equals(originalSignature)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAPKModified(Context context, String knownChecksum) {
        String apkPath = context.getPackageCodePath();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(apkPath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            fis.close();
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return knownChecksum.equals(hexString.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return accessibilityEnabled == 1;
    }

    public boolean isDeveloperModeEnabled(Context context) {
        return Settings.Secure.getInt(
                context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) != 0;
    }
}
