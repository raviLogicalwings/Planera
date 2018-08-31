package com.planera.mis.planera2.activities.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Filter;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class SearchCompactDialog extends SimpleSearchDialogCompat {


    public SearchCompactDialog(Context context, String title, String searchHint, @Nullable Filter filter, ArrayList items, SearchResultListener searchResultListener) {
        super(context, title, searchHint, filter, items, searchResultListener);
    }


    public void onDialogSelectItem(){

    }
}
