package com.rachnisapp.notificationfilter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = "NotificationListener";
    private static final String SILENT_CHANNEL_ID = "SilentChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "NotificationListener service created");
        createSilentNotificationChannel();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();

        // Ignorar notificaciones generadas por esta app
        if (packageName.equals(getPackageName())) {
            Log.d(TAG, "Notification is from this app, ignoring");
            return;
        }

        Log.d(TAG, "Received notification from package: " + packageName);

        if (packageName.equals("com.whatsapp")) {
            Log.d(TAG, "Notification is from WhatsApp");

            if (sbn.getNotification().extras != null) {
                CharSequence notificationText = sbn.getNotification().extras.getCharSequence("android.text");
                Log.d(TAG, "Notification text: " + notificationText);

                if (notificationText != null && containsOnlyLink(notificationText.toString())) {
                    Log.d(TAG, "Notification contains only a link, silencing...");

                    // Cancelar notificación original inmediatamente
                    cancelOriginalNotification(sbn);

                    // Publicar una notificación silenciosa
                    publishSilentNotification(sbn, notificationText.toString());
                }
            }
        }
    }

    private boolean containsOnlyLink(String message) {
        return message.matches("^https://\\S+$");
    }

    private void cancelOriginalNotification(StatusBarNotification sbn) {
        try {
            cancelNotification(sbn.getKey());
            Log.d(TAG, "Notification canceled using key: " + sbn.getKey());
        } catch (Exception e) {
            Log.e(TAG, "Error canceling notification: " + e.getMessage(), e);
        }
    }

    private void publishSilentNotification(StatusBarNotification sbn, String notificationText) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        try {
            String senderName = sbn.getNotification().extras.getString("android.title", "Desconocido");
            Log.d(TAG, "Sender name: " + senderName);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder builder = new Notification.Builder(getApplicationContext(), SILENT_CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_notification_clear_all) // Ícono de sistema para pruebas
                        .setContentTitle(senderName)
                        .setContentText(notificationText)
                        .setAutoCancel(true);

                notificationManager.notify(sbn.getId(), builder.build());
                Log.d(TAG, "Silent notification reissued with ID: " + sbn.getId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error publishing silent notification: " + e.getMessage(), e);
        }
    }

    private void createSilentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    SILENT_CHANNEL_ID,
                    "Silent Notifications",
                    NotificationManager.IMPORTANCE_MIN // IMPORTANCE_MIN para notificaciones completamente silenciosas
            );

            channel.setDescription("Silent notification channel for filtered messages");
            channel.setSound(null, null); // Sin sonido
            channel.enableVibration(false); // Sin vibración
            channel.enableLights(false); // Sin luces LED

            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "Silent notification channel created");
        }
    }
}