importScripts(
  "https://www.gstatic.com/firebasejs/12.19.0/firebase-app-compat.js"
);

importScripts(
  "https://www.gstatic.com/firebasejs/12.19.0/firebase-messaging-compat.js"
);

firebase.initializeApp({
  apiKey: "AIzaSyCzzH6dmObfrkcxQMj0FnRwCjoo6EvCQao",
  authDomain: "chatsapp-baac6.firebaseapp.com",
  databaseURL: "https://chatsapp-baac6-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "chatsapp-baac6",
  storageBucket: "chatsapp-baac6.firebasestorage.app",
  messagingSenderId: "948254375927",
  appId: "1:948254375927:web:153f9b664630a7c0bdf8f2"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log("ChatsApp background message received", payload.data?.chatId || "");

  // FCM notification payloads are displayed automatically
  // in the background, so don't display a duplicate here.
});

self.addEventListener("notificationclick", (event) => {
  event.notification.close();

  event.waitUntil(
    clients.openWindow("https://chatsapp-online.pages.dev/")
  );
});
