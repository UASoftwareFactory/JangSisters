package kr.sofac.jangsisters.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.sofac.jangsisters.R;
import kr.sofac.jangsisters.models.Constants;

/**
 * Created by Sasha on 20.12.2017.
 */

public class ShopFragment extends BaseFragment {

    //TODO подумать как быть с onBackPressed

    //TODO перенести иконки в тулбар

    @BindView(R.id.shop_webview) WebView webView;

    private List<String> pages;
    private int currentPage = -1;
    private boolean fromNavigation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, view);
        pages = new ArrayList<>();
        webView.loadUrl(Constants.SHOP_URL);
        webView.setWebViewClient(new ShopWebViewClient());
        return view;
    }

    public void backClick(){
        if(currentPage != 0) {
            fromNavigation = true;
            currentPage--;
            webView.loadUrl(pages.get(currentPage));
        }
    }

    public void forwardClick(){
        if(currentPage != pages.size() - 1) {
            fromNavigation = true;
            currentPage++;
            webView.loadUrl(pages.get(currentPage));
        }
    }

    public void refreshClick(){
        fromNavigation = true;
        webView.loadUrl(pages.get(currentPage));
    }

    public void homeClick() {
        fromNavigation = true;
        webView.loadUrl(Constants.SHOP_URL);
    }

    private class ShopWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(view.getTitle());
            if(!fromNavigation) {
                pages.add(url);
                currentPage++;
            }
            fromNavigation = false;
            super.onPageFinished(view, url);
        }

    }

}
