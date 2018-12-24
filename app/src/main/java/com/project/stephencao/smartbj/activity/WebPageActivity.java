package com.project.stephencao.smartbj.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.utils.ConstantValues;

public class WebPageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton mBackImageButton, mShareImageButton, mFontImageButton;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private WebSettings.TextSize mPreviousTextSize;
    private WebSettings mWebViewSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        initViews();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(ConstantValues.WEB_PAGE_URL);
        mWebView.loadUrl(url);
        mWebViewSettings = mWebView.getSettings();
        mWebViewSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url); // forbid jumping to the system browser
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) { //check whether can go back or not
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private void initViews() {
        mWebView = findViewById(R.id.wv_web_page_content);
        mProgressBar = findViewById(R.id.pb_web_page_progress);
        mProgressBar.setMax(100);
        mBackImageButton = findViewById(R.id.ibtn_web_page_back);
        mShareImageButton = findViewById(R.id.ibtn_web_page_share);
        mFontImageButton = findViewById(R.id.ibtn_web_page_font);
        mBackImageButton.setOnClickListener(this);
        mShareImageButton.setOnClickListener(this);
        mFontImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_web_page_back: {
                finish();
                break;
            }
            case R.id.ibtn_web_page_share: {
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.ibtn_web_page_font: {
                showSettingFontDialog();
                break;
            }
        }
    }

    private void showSettingFontDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.view__web_page_set_font_dialog, null);
        alertDialog.setView(view);
        RadioGroup radioGroup = view.findViewById(R.id.rg_web_page_font_size_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                mPreviousTextSize = mWebViewSettings.getTextSize();
                switch (checkedId) {
                    case R.id.rbtn_web_page_font_sl: {
                        mWebViewSettings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    }
                    case R.id.rbtn_web_page_font_l: {
                        mWebViewSettings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    }
                    case R.id.rbtn_web_page_font_n: {
                        mWebViewSettings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    }
                    case R.id.rbtn_web_page_font_s: {
                        mWebViewSettings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    }
                    case R.id.rbtn_web_page_font_ss: {
                        mWebViewSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    }
                }
            }
        });
        Button cancelButton = view.findViewById(R.id.btn_web_page_font_size_cancel);
        Button confirmButton = view.findViewById(R.id.btn_web_page_font_size_confirm);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebViewSettings.setTextSize(mPreviousTextSize);
                alertDialog.dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WebPageActivity.this, "success", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
