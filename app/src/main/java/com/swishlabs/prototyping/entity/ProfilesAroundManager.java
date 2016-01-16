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
import com.swishlabs.prototyping.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for loading data from the various sources. Instantiating classes are responsible for
 * providing the {code onDataLoaded} method to do something with the data.
 */
public abstract class ProfilesAroundManager<T> extends BaseDataManager
        implements DataLoadingSubject {

//    private final FilterAdapter filterAdapter;
//    private AtomicInteger loadingCount;
//    private Map<String, Integer> pageIndexes;

    private LatLng mCurrLocation;
    private GPSTracker gpsTracker;
    private List<Profile> mListProfile;

    public ProfilesAroundManager(Context context) {
        super(context);
        gpsTracker = new GPSTracker(context);

        if(gpsTracker.canGetLocation()) {

            mCurrLocation = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

        }else {
//            gpsTracker.showSettingsAlert();
            mCurrLocation = new LatLng(0.0, 0.0);
        }


        mListProfile = new ArrayList<>();
//        this.filterAdapter = filterAdapter;
//        loadingCount = new AtomicInteger(0);
//        setupPageIndexes();
    }

//    public void loadAllDataSources() {
//        for (Source filter : filterAdapter.getFilters()) {
//            loadSource(filter);
//        }
//    }

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }

//    @Override
//    public void onFiltersChanged(Source changedFilter){
//        if (changedFilter.active) {
//            loadSource(changedFilter);
//        } else {
//            // clear the page index for the source
//            pageIndexes.put(changedFilter.key, 0);
//        }
//    }
//
//    @Override
//    public void onFilterRemoved(Source removed) { } // no-op

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

        mWebApi.getProfiles("153", 5.0, mCurrLocation.longitude,mCurrLocation.latitude, getOffset(), new IResponse<List<ProfileAround>>() {

            @Override
            public void onSucceed(List<ProfileAround> result) {
                if(result != null && !result.isEmpty()){
                    addOffset(result.size());
                    setSize(result.size());

                    if (result.size() < maxRecord)
                        noMoreData = true;

                    for (ProfileAround profileId :result) {
                        mWebApi.getProfile(String.valueOf(profileId.getSessionId()), new IResponse<Profile>() {
                            @Override
                            public void onSucceed(Profile result) {

                                mListProfile.add(result);
                                if (mListProfile.size() == getSize()) {
                                    onDataLoaded(mListProfile);
                                    loadingCount.decrementAndGet();
                                }
                            }

                            @Override
                            public void onFailed(String code, String errMsg) {

                                setSize(getSize()-1);
                                if(getSize()==0 || mListProfile.size() == getSize()) {
                                    onDataLoaded(mListProfile);
                                    loadingCount.decrementAndGet();
                                }
                            }

                            @Override
                            public Profile asObject(String rspStr) throws JSONException {
                                try {
                                    JSONObject json = new JSONObject(rspStr);

                                    Profile profile = GsonUtil.jsonToObject(Profile.class, rspStr);

                                    return profile;
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailed(String code, String errMsg) {

                loadingCount.decrementAndGet();
            }

            @Override
            public List<ProfileAround> asObject(String rspStr) throws JSONException {

                if (!TextUtils.isEmpty(rspStr)) {
                    TypeToken<List<ProfileAround>> type = new TypeToken<List<ProfileAround>>() {
                    };
                    return GsonUtil.jsonToList(type.getType(), rspStr);
                }
                return new ArrayList<ProfileAround>();

            }
        });

    }

//    private void loadSource(Source source) {
//        if (source.active) {
//            loadingCount.incrementAndGet();
//            int page = getNextPageIndex(source.key);
//            switch (source.key) {
//                case SourceManager.SOURCE_DESIGNER_NEWS_POPULAR:
//                    loadDesignerNewsTopStories(page);
//                    break;
//                case SourceManager.SOURCE_DESIGNER_NEWS_RECENT:
//                    loadDesignerNewsRecent(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_POPULAR:
//                    loadDribbblePopular(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_FOLLOWING:
//                    loadDribbbleFollowing(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_USER_LIKES:
//                    loadDribbbleUserLikes(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_USER_SHOTS:
//                    loadDribbbleUserShots(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_RECENT:
//                    loadDribbbleRecent(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_DEBUTS:
//                    loadDribbbleDebuts(page);
//                    break;
//                case SourceManager.SOURCE_DRIBBBLE_ANIMATED:
//                    loadDribbbleAnimated(page);
//                    break;
//                case SourceManager.SOURCE_PRODUCT_HUNT:
//                    loadProductHunt(page);
//                    break;
//                default:
//                    if (source instanceof Source.DribbbleSearchSource) {
//                        loadDribbbleSearch((Source.DribbbleSearchSource) source, page);
//                    } else if (source instanceof Source.DesignerNewsSearchSource) {
//                        loadDesignerNewsSearch((Source.DesignerNewsSearchSource) source, page);
//                    }
//                    break;
//            }
//        }
//    }

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
//    private void loadDesignerNewsTopStories(final int page) {
//        getDesignerNewsApi().getTopStories(page, new Callback<StoriesResponse>() {
//            @Override
//            public void success(StoriesResponse storiesResponse, Response response) {
//                if (storiesResponse != null
//                        && sourceIsEnabled(SourceManager.SOURCE_DESIGNER_NEWS_POPULAR)) {
//                    setPage(storiesResponse.stories, page);
//                    setDataSource(storiesResponse.stories,
//                            SourceManager.SOURCE_DESIGNER_NEWS_POPULAR);
//                    onDataLoaded(storiesResponse.stories);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }

//    private void loadDesignerNewsRecent(final int page) {
//        getDesignerNewsApi().getRecentStories(page, new Callback<StoriesResponse>() {
//            @Override
//            public void success(StoriesResponse storiesResponse, Response response) {
//                if (storiesResponse != null
//                        && sourceIsEnabled(SourceManager.SOURCE_DESIGNER_NEWS_RECENT)) {
//                    setPage(storiesResponse.stories, page);
//                    setDataSource(storiesResponse.stories,
//                            SourceManager.SOURCE_DESIGNER_NEWS_RECENT);
//                    onDataLoaded(storiesResponse.stories);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }
//
//    private void loadDesignerNewsSearch(final Source.DesignerNewsSearchSource source,
//                                        final int page) {
//        getDesignerNewsApi().search(source.query, page, new Callback<StoriesResponse>() {
//            @Override
//            public void success(StoriesResponse storiesResponse, Response response) {
//                if (storiesResponse != null) {
//                    setPage(storiesResponse.stories, page);
//                    setDataSource(storiesResponse.stories, source.key);
//                    onDataLoaded(storiesResponse.stories);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }
//
//    private void loadDribbblePopular(final int page) {
//        getDribbbleApi().getPopular(page, DribbbleService.PER_PAGE_DEFAULT, new
//                Callback<List<Shot>>() {
//            @Override
//            public void success(List<Shot> shots, Response response) {
//                if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_POPULAR)) {
//                    setPage(shots, page);
//                    setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_POPULAR);
//                    onDataLoaded(shots);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }
//
//    private void loadDribbbleDebuts(final int page) {
//        getDribbbleApi().getDebuts(page, DribbbleService.PER_PAGE_DEFAULT, new
//                Callback<List<Shot>>() {
//            @Override
//            public void success(List<Shot> shots, Response response) {
//                if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_DEBUTS)) {
//                    setPage(shots, page);
//                    setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_DEBUTS);
//                    onDataLoaded(shots);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }
//
//    private void loadDribbbleAnimated(final int page) {
//        getDribbbleApi().getAnimated(page, DribbbleService.PER_PAGE_DEFAULT, new
//                Callback<List<Shot>>() {
//            @Override
//            public void success(List<Shot> shots, Response response) {
//                if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_ANIMATED)) {
//                    setPage(shots, page);
//                    setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_ANIMATED);
//                    onDataLoaded(shots);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }
//
//    private void loadDribbbleRecent(final int page) {
//        getDribbbleApi().getRecent(page, DribbbleService.PER_PAGE_DEFAULT, new
//                Callback<List<Shot>>() {
//            @Override
//            public void success(List<Shot> shots, Response response) {
//                if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_RECENT)) {
//                    setPage(shots, page);
//                    setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_RECENT);
//                    onDataLoaded(shots);
//                }
//                loadingCount.decrementAndGet();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                loadingCount.decrementAndGet();
//            }
//        });
//    }

//    private void loadDribbbleFollowing(final int page) {
//        if (getDribbblePrefs().isLoggedIn()) {
//            getDribbbleApi().getFollowing(page, DribbbleService.PER_PAGE_DEFAULT,
//                    new Callback<List<Shot>>() {
//                        @Override
//                        public void success(List<Shot> shots, Response response) {
//                            if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_FOLLOWING)) {
//                                setPage(shots, page);
//                                setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_FOLLOWING);
//                                onDataLoaded(shots);
//                            }
//                            loadingCount.decrementAndGet();
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            loadingCount.decrementAndGet();
//                        }
//                    });
//        } else {
//            loadingCount.decrementAndGet();
//        }
//    }
//
//    private void loadDribbbleUserLikes(final int page) {
//        if (getDribbblePrefs().isLoggedIn()) {
//            getDribbbleApi().getUserLikes(page, DribbbleService.PER_PAGE_DEFAULT,
//                    new Callback<List<Like>>() {
//                        @Override
//                        public void success(List<Like> likes, Response response) {
//                            if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_USER_LIKES)) {
//                                // API returns Likes but we just want the Shots
//                                List<Shot> likedShots = new ArrayList<>(likes.size());
//                                for (Like like : likes) {
//                                    likedShots.add(like.shot);
//                                }
//                                // these will be sorted like any other shot (popularity per page)
//                                // TODO figure out a more appropriate sorting strategy for likes
//                                setPage(likedShots, page);
//                                setDataSource(likedShots, SourceManager.SOURCE_DRIBBBLE_USER_LIKES);
//                                onDataLoaded(likedShots);
//                            }
//                            loadingCount.decrementAndGet();
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            loadingCount.decrementAndGet();
//                        }
//                    });
//        } else {
//            loadingCount.decrementAndGet();
//        }
//    }
//
//    private void loadDribbbleUserShots(final int page) {
//        if (getDribbblePrefs().isLoggedIn()) {
//            getDribbbleApi().getUserShots(page, DribbbleService.PER_PAGE_DEFAULT,
//                    new Callback<List<Shot>>() {
//                        @Override
//                        public void success(List<Shot> shots, Response response) {
//                            if (sourceIsEnabled(SourceManager.SOURCE_DRIBBBLE_USER_SHOTS)) {
//                                // this api call doesn't populate the shot user field but we need it
//                                User user = getDribbblePrefs().getUser();
//                                for (Shot shot : shots) {
//                                    shot.user = user;
//                                }
//
//                                setPage(shots, page);
//                                setDataSource(shots, SourceManager.SOURCE_DRIBBBLE_USER_SHOTS);
//                                onDataLoaded(shots);
//                            }
//                            loadingCount.decrementAndGet();
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            loadingCount.decrementAndGet();
//                        }
//                    });
//        } else {
//            loadingCount.decrementAndGet();
//        }
//    }
//
//
//    private void loadDribbbleSearch(final Source.DribbbleSearchSource source, final int page) {
//        new AsyncTask<Void, Void, List<Shot>>() {
//            @Override
//            protected List<Shot> doInBackground(Void... params) {
//                return DribbbleSearch.search(source.query, DribbbleSearch.SORT_RECENT, page);
//            }
//
//            @Override
//            protected void onPostExecute(List<Shot> shots) {
//                if (shots != null && shots.size() > 0 && sourceIsEnabled(source.key)) {
//                    setPage(shots, page);
//                    setDataSource(shots, source.key);
//                    onDataLoaded(shots);
//                }
//                loadingCount.decrementAndGet();
//            }
//        }.execute();
//    }
//

}
