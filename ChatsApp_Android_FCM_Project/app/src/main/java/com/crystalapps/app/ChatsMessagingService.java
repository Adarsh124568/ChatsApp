package com.crystalapps.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.app.Notification;

public class ChatsMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL = "chats_messages";
    @Override public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        getSharedPreferences("fcm", MODE_PRIVATE).edit().putString("token", token).apply();
        // TODO: send token to authenticated backend and bind it to Firebase Auth UID.
    }
    @Override public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        // Intentionally generic: do not trust a remote `locked` flag or expose private text.
        // Server must enforce authorization; until then we only display a generic alert.
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) nm.createNotificationChannel(new NotificationChannel(CHANNEL, "ChatsApp messages", NotificationManager.IMPORTANCE_HIGH));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder builder = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(this, CHANNEL) : new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_email)
            .setContentTitle("ChatsApp")
            .setContentText("You have a new message")
            .setAutoCancel(true).setContentIntent(pi);
        nm.notify((int)(System.currentTimeMillis() % Integer.MAX_VALUE), builder.build());
    }
}
