package com.oneflow.analytics.customwidgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oneflow.analytics.utils.OFHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OFCustomeWebView extends WebView {

    String tag = this.getClass().getName();
    Context context = null;
    private boolean isLoading = false;
    private static boolean mError = false;
    private static boolean isBack = false;
    int counter = 0;
    String appCachePath;

    private WebChromeClient.CustomViewCallback customViewCallback;
    public static View mCustomView;
    //static MyWebChromeClient mWebChrome = null;

    public OFCustomeWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;

        //this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        /*if (Build.VERSION.SDK_INT >= 11) {
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
           // this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            try {
                Method setLayerTypeMethod = this.getClass().getMethod(
                        "setLayerType",
                        new Class[]{int.class, WebView.class});
                setLayerTypeMethod.invoke(this, new Object[]{
                        LAYER_TYPE_HARDWARE, null});
            } catch (NoSuchMethodException e) {
                // Older OS, no HW acceleration anyway
            } catch (IllegalArgumentException e) {
                OFHelper.e(tag,e.getMessage());
            } catch (IllegalAccessException e) {
                OFHelper.e(tag,e.getMessage());
            } catch (InvocationTargetException e) {
                OFHelper.e(tag,e.getMessage());
            }
        }*/



        this.getSettings().setPluginState(WebSettings.PluginState.ON);
        //this.setWebChromeClient(new WebChromeClient());


        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //SharedPreferences shp = CustomeWebView.this.context.getSharedPreferences(context.getString(R.string.app_name), CustomeWebView.this.context.MODE_PRIVATE);
        //cookieManager.setCookie("https://myess.tsrdarashaw.com/",shp.getString("cookie", "0"));
        CookieSyncManager.getInstance().sync();
        SystemClock.sleep(500); // time in milliseconds

        OFHelper.v(tag, "1Flow at ABVwebView");




        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        this.getSettings().setJavaScriptEnabled(true);

        //this.getSettings().setPluginsEnabled(true);
        //this.getSettings().setPluginState(PluginState.ON);
        this.setWebChromeClient(new MyWebChromeClient());
        //this.addJavascriptInterface(new DemoJavaScriptInterface(), "jsinterface");
        //this.getSettings().setRenderPriority(RenderPriority.HIGH);
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setSupportZoom(false);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);


        this.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //this.getSettings().setAppCacheEnabled(false);
        //this.getSettings().setBlockNetworkImage(true);
        //this.getSettings().setBlockNetworkLoads(true);

//        this.getSettings().setDatabaseEnabled(true);
//        appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        // OFHelper.v(tag, "1Flow Caching url at ab[" + appCachePath + "]");
//        this.getSettings().setAppCachePath(appCachePath);
//        this.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        //this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //this.getSettings().setAppCacheEnabled(true);

        this.getSettings().setAllowFileAccess(false);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setLoadsImagesAutomatically(true);

        /*String databasePath = this.getContext().getDir("databases", Context.MODE_PRIVATE).getPath();
        this.getSettings().setDatabasePath(databasePath);*/
        this.setWebViewClient(new ABWebViewClient());





        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //FrameLayout fl=(FrameLayout)inflater.inflate(R.layout.custom_screen,null);

    }

    public OFCustomeWebView(Context context) {
        super(context);
        this.context = context;

        if (Build.VERSION.SDK_INT >= 11) {
            this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            try {
                Method setLayerTypeMethod = this.getClass().getMethod(
                        "setLayerType",
                        new Class[]{int.class, WebView.class});
                setLayerTypeMethod.invoke(this, new Object[]{
                        LAYER_TYPE_HARDWARE, null});
            } catch (NoSuchMethodException e) {
                // Older OS, no HW acceleration anyway
            } catch (IllegalArgumentException e) {
                OFHelper.e(tag,e.getMessage());
            } catch (IllegalAccessException e) {
                OFHelper.e(tag,e.getMessage());
            } catch (InvocationTargetException e) {
                OFHelper.e(tag,e.getMessage());
            }
        }

        CookieSyncManager cookieSyncManager = CookieSyncManager
                .createInstance(this.context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //cookieManager.setCookie("https://myess.tsrdarashaw.com/",shp.getString("cookie", "0"));
        CookieSyncManager.getInstance().sync();
        SystemClock.sleep(500); // time in milliseconds

        OFHelper.v(tag, "1Flow at ABVwebView");

        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setJavaScriptEnabled(true);
        //this.getSettings().setPluginsEnabled(true);
        this.getSettings().setPluginState(PluginState.ON);
        this.setWebChromeClient(new MyWebChromeClient());
        //this.addJavascriptInterface(new DemoJavaScriptInterface(), "jsinterface");
        this.getSettings().setRenderPriority(RenderPriority.HIGH);
        this.getSettings().setBuiltInZoomControls(false);
        this.getSettings().setSupportZoom(false);

        this.getSettings().setDatabaseEnabled(true);
        appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        this.getSettings().setAppCachePath(appCachePath);
        this.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        //this.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.getSettings().setAppCacheEnabled(true);

        this.getSettings().setAllowFileAccess(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setLoadsImagesAutomatically(true);

        /*String databasePath = this.getContext().getDir("databases", Context.MODE_PRIVATE).getPath();
        this.getSettings().setDatabasePath(databasePath);*/

        this.setWebViewClient(new ABWebViewClient());


    }

    private class ABWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


            OFHelper.e(tag, "1Flow onReceivedError error isBack[" + isBack + "][" + failingUrl + "]ABWebView.this.getSettings().getCacheMode()[" + OFCustomeWebView.this.getSettings().getCacheMode() + "][" + errorCode + "][" + description + "]");
            /*
             * Create Timeout Exception
             */
            //view.stopLoading();


            //	if(OFHelper.isNetworkAvailable(ABWebView.this.context)){
            if (errorCode == ERROR_HOST_LOOKUP) {
                mError = true;
                if (!OFHelper.isConnected(context)) {
                    //MainActivity.noConnectionAlert("Internet connection is not available. Please check.");
                    OFHelper.v(this.getClass().getName(), "1Flow Network issue");
                }

            }
            if (errorCode == ERROR_CONNECT) {
                view.stopLoading();
                if (failingUrl.endsWith(".com/")) {
                    AlertDialog ald = new AlertDialog.Builder(context)
                            .create();
                    ald.setMessage("Internet connection is not available. Please check.");
                    ald.setButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                            ((Activity) OFCustomeWebView.this.context).finish();

                        }
                    });

                    ald.show();
                } else {
                    OFHelper.v(this.getClass().getName(), "1Flow Network issue");
                    ///view.reload();
                }
            }
            // TODO Auto-generated method stub
            if (errorCode == ERROR_TIMEOUT) {
                view.stopLoading();
                OFHelper.e(this.getClass().getName(), "1Flow Network issue");
            }

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            // TODO Auto-generated method stub
            if (mError) {
                OFHelper.v(this.getClass().getName(), "Network issue");
                view.stopLoading();
            }
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            // TODO Auto-generated method stub
            handler.proceed("hbx", "hbx");

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
            if (mError) {
                view.stopLoading();
                OFHelper.v(this.getClass().getName(), "Network issue");
            }
			/*if(!OFHelper.isNetworkAvailable(ABWebView.this.context)){
				view.stopLoading();
				MainActivity.noConnectionAlert("Internet connection is not available. Please check.");
			}*/
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                            String mimetype, long contentlength) {
                    // TODO Auto-generated method stub

                    final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri source = Uri.parse(url);

                    // Make a new request pointing to the mp3 url
                    DownloadManager.Request request = new DownloadManager.Request(source);
                    manager.enqueue(request);
                }
            });
            if (url.contains("subscribe") || url.contains("emailArticlePrompt") || url.contains("m.facebook.com") || url.contains("plus.google.com") || url.contains("www.linkedin.com") || url.contains("twitter.com") || url.contains("license.icopyright.net")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                OFCustomeWebView.this.context.startActivity(browserIntent);
            }
			/*else if(url.endsWith(".pdf")){
					final DownloadManager manager = (DownloadManager) ABWebView.this.context.getSystemService(Context.DOWNLOAD_SERVICE);
					Uri source = Uri.parse(url);

					// Make a new request pointing to the mp3 url
					DownloadManager.Request request = new DownloadManager.Request(source);
					// Use the same file name for the destination
					File destinationFile = new File (OFHelper.getPDFLocation(), source.getLastPathSegment());
					request.setDestinationUri(Uri.fromFile(destinationFile));
					// Add it to the manager
					manager.enqueue(request);
					view.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
				}*/
            else {
                view.loadUrl(url);
            }
			/*} else {


				MainActivity.noConnectionAlert("Internet connection is not available. Please check.");
			}*/
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            counter++;
        }
    }

    public boolean inCustomView() {
        return (mCustomView != null);
    }

    /*public static void hideCustomView() {

        mWebChrome.onHideCustomView();
    }*/

    CountDownTimer cdt = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            //MainActivity.hidePageLoader(false);
        }
    };

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    class MyWebChromeClient extends WebChromeClient {

        //private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>=100){
                view.setVisibility(VISIBLE);
            }
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            // TODO Auto-generated method stub
            //super.getVisitedHistory(callback);

        }

        ;

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            onShowCustomView(view, callback);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;


            customViewCallback = callback;
        }

        @Override
        public View getVideoLoadingProgressView() {

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(OFCustomeWebView.this.context);

            }
            return mVideoProgressView;
        }

        @Override
        public void onHideCustomView() {
            //To change body of overridden methods use File | Settings | File Templates.
            if (mCustomView == null)
                return;


            customViewCallback.onCustomViewHidden();

            mCustomView = null;
        }
    }

    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
            // also you can call javascript functions here.
        }
    }

    public boolean getLoadingStatus() {
        return isLoading;
    }

    public void updateIsBack(boolean status) {
        isBack = status;
    }

    @Override
    public void goBack() {
        // TODO Auto-generated method stub
        super.goBack();

        isBack = true;
    }
}
