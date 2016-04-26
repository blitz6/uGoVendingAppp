package com.ugosmoothie.ugovendingapp.WebServer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.EventTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michelle on 3/19/2016.
 */
public class AsyncServer {
    private static AsyncServer ourInstance = new AsyncServer();

    public static AsyncServer getInstance() {
        return ourInstance;
    }

    ArrayList<WebSocket> webSockets;
    AsyncHttpServer asyncHttpServer;
    AsyncHttpServer.WebSocketRequestCallback cb;

    private AsyncServer() {
        webSockets = new ArrayList<>();
        asyncHttpServer = new AsyncHttpServer();
        cb = new AsyncHttpServer.WebSocketRequestCallback() {
          @Override
          public void onConnected(final WebSocket webSocket, AsyncHttpServerRequest serverRequest) {
            webSockets.add(webSocket);
              webSocket.send("Connected to Server");
              webSocket.setClosedCallback(new CompletedCallback() {
                  @Override
                  public void onCompleted(Exception ex) {
                      try {
                          if (ex != null) {
                              Log.e("WebSocket", "error");
                          }
                      } finally {
                          webSockets.remove(webSocket);
                      }
                  }
              });
              webSocket.setStringCallback(new WebSocket.StringCallback() {
                  @Override
                  public void onStringAvailable(String s) {
                    requestParser(s);
                  }
              });;
          }
        };

        asyncHttpServer.websocket("/", cb);
        asyncHttpServer.listen(5858);

    }

    public void SendMessage(JSONObject payload) {
        for (WebSocket socket : webSockets) {
            socket.send(payload.toString());
        }
    };

    HashMap<String, ArrayList<Context>> listeners = new HashMap<>();

    public void registerListener(Context context, String action) {
        if (listeners.containsKey(action)) {
            listeners.get(action).add(context);
        } else {
            listeners.put(action, new ArrayList<Context>());
            listeners.get(action).add(context);
        }
    }

    private void requestParser(String s) {
        try {
            JSONObject msg = new JSONObject(s);
            if (msg.getString("eventType").equals(EventTypes.SmoothieEvent.Complete.getSmoothieEvent())) {
                Purchase purchase = Purchase.findById(Purchase.class, msg.getLong("orderId"));
                if (purchase != null) {
                    purchase.Completed();
                    if (listeners.containsKey("complete")) {
                        for (Context ctx: listeners.get("complete")
                             ) {
                            Intent intent = new Intent("complete");
                            intent.putExtra("orderId", purchase.getId());
                            ctx.sendBroadcast(intent);
                        }
                    }
                }
            }
        } catch (JSONException jsonEx) {
            jsonEx.printStackTrace();
        }
    };
}
