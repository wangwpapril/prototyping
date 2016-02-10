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

import com.google.gson.reflect.TypeToken;
import com.swishlabs.prototyping.net.IResponse;
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
public abstract class ConnectionsManager extends BaseDataManager
        implements DataLoadingSubject {

    private List<Profile> mListProfile;
    private String sessionId;

    public ConnectionsManager(Context context) {
        super(context);

        sessionId = SharedPreferenceUtil.getString(Enums.PreferenceKeys.sessionId.toString(),"153");

        mListProfile = new ArrayList<>();
    }

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }


    public void loadData()
    {

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

}
