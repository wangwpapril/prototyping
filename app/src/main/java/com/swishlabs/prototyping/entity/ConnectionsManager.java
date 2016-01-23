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
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.swishlabs.prototyping.net.IResponse;
import com.swishlabs.prototyping.services.GPSTracker;
import com.swishlabs.prototyping.util.Enums;
import com.swishlabs.prototyping.util.GsonUtil;
import com.swishlabs.prototyping.util.SharedPreferenceUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for loading data from the various sources. Instantiating classes are responsible for
 * providing the {code onDataLoaded} method to do something with the data.
 */
public abstract class ConnectionsManager<T> extends BaseDataManager
        implements DataLoadingSubject {

    private LatLng mCurrLocation;
    private GPSTracker gpsTracker;
    private List<Profile> mListProfile;
    private String sessionId;

    public ConnectionsManager(Context context) {
        super(context);
        gpsTracker = new GPSTracker(context);

        if(gpsTracker.canGetLocation()) {

            mCurrLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

        }else {
//            gpsTracker.showSettingsAlert();
            mCurrLocation = new LatLng(0.0, 0.0);
        }


        sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(),"153");

        mListProfile = new ArrayList<>();
//        this.filterAdapter = filterAdapter;
//        loadingCount = new AtomicInteger(0);
//        setupPageIndexes();
    }

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }


    public void loadData()
    {
//        GPSTracker gpsTracker = ((BaseFragmentActivity) getActivity()).getGpsTracker();
//        LatLng mCurrLocation;
//
//        if(gpsTracker.canGetLocation()) {
//
//            mCurrLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
//
//        }else {
////            gpsTracker.showSettingsAlert();
//            mCurrLocation = new LatLng(0.0, 0.0);
//        }

        loadingCount.incrementAndGet();

        mListProfile.clear();

        mWebApi.getConnections(sessionId, getOffset(), new IResponse<List<Profile>>() {

            @Override
            public void onSucceed(List<Profile> result) {
                if(result != null && !result.isEmpty()){
                    addOffset(result.size());
                    setSize(result.size());

                    if (result.size() < maxRecord)
                        noMoreData = true;

                    mListProfile = result;
                    onDataLoaded(mListProfile);
                }

            }

            @Override
            public void onFailed(String code, String errMsg) {

                loadingCount.decrementAndGet();
            }

            @Override
            public List<Profile> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<Profile>> type = new TypeToken<List<Profile>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<Profile>();

            }
        });

    }

    private void getTradeOpp()
    {
        if (mListProfile == null || mListProfile.isEmpty())
            return;

        setSize(mListProfile.size());
        for(Profile profile:mListProfile){
            final String userId = String.valueOf(profile.getSessionId());
            mWebApi.getTradeOpp(sessionId, userId, new IResponse<List<Service>>() {
                @Override
                public void onSucceed(List<Service> result) {
                     if (result.size() > 0){
                        for(int i=0;i<mListProfile.size();i++){
                            if (String.valueOf(mListProfile.get(i).getSessionId()).equals(userId)){
                                mListProfile.get(i).setOppNum(result.size());
                                break;
                            }
                        }
                     }

                    setSize(getSize()-1);
                    if (getSize()==0){
                        onDataLoaded(mListProfile);
                        loadingCount.decrementAndGet();
                    }

                }

                @Override
                public void onFailed(String code, String errMsg) {
                    setSize(getSize()-1);
                    if (getSize()==0){
                        onDataLoaded(mListProfile);
                        loadingCount.decrementAndGet();
                    }

                }

                @Override
                public List<Service> asObject(String rspStr) throws JSONException {
                    if (!TextUtils.isEmpty(rspStr)) {
                        TypeToken<List<Service>> type = new TypeToken<List<Service>>() {
                        };
                        return GsonUtil.jsonToList(type.getType(), rspStr);
                    }
                    return new ArrayList<Service>();

                }
            });

        }
    }


//    private void setupPageIndexes() {
//        List<Source> dateSources = filterAdapter.getFilters();
//        pageIndexes = new HashMap<>(dateSources.size());
//        for (Source source : dateSources) {
//            pageIndexes.put(source.key, 0);
//        }
//    }
//
//    private int getNextPageIndex(String dataSource) {
//        int nextPage = 1; // default to one – i.e. for newly added sources
//        if (pageIndexes.containsKey(dataSource)) {
//            nextPage = pageIndexes.get(dataSource) + 1;
//        }
//        pageIndexes.put(dataSource, nextPage);
//        return nextPage;
//    }
//
//    private boolean sourceIsEnabled(String key) {
//        return pageIndexes.get(key) != 0;
//    }
//

}
