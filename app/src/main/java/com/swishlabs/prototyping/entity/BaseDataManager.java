/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.swishlabs.prototyping.entity;

import android.content.Context;

import com.swishlabs.prototyping.net.WebApi;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base class for loading data.
 */
public abstract class BaseDataManager <T>  {
    public static final String PROFILES_AROUND = "PROFILES_AROUND";
    public static final int maxRecord = 20;

    protected boolean noMoreData;
    protected AtomicInteger loadingCount;
    protected int offset;
    protected WebApi mWebApi;
    protected int size;

    private UserProfilePrefs userProfilePrefs;
//    private DesignerNewsService designerNewsApi;
//    private DribbblePrefs dribbblePrefs;
//    private DribbbleService dribbbleApi;
//    private ProductHuntService productHuntApi;

    public BaseDataManager(Context context) {
        // setup the API access objects
        userProfilePrefs = UserProfilePrefs.get(context);

        initialize();
//        createDesignerNewsApi();
        mWebApi = WebApi.getInstance(context);
//        dribbblePrefs = DribbblePrefs.get(context);
//        createDribbbleApi();
//        createProductHuntApi();
    }

    public abstract void onDataLoaded(List<T> data);

//    protected static void setPage(List<? extends PlaidItem> items, int page) {
//        for (PlaidItem item : items) {
//            item.page = page;
//        }
//    }

    protected void addOffset(int offset) {

        this.offset += offset;
    }

    protected int getOffset() {
        return this.offset;
    }

    public boolean getMoreData() {
        return noMoreData;
    }

    public void initialize() {
        offset = 0;
        noMoreData = false;
        loadingCount = new AtomicInteger(0);
        size = 0;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

//    @Override
//    public void onDesignerNewsLogin() {
//        createDesignerNewsApi(); // capture the auth token
//    }
//
//    @Override
//    public void onDesignerNewsLogout() {
//        createDesignerNewsApi(); // clear the auth token
//    }

}
