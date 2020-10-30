/*
 * Copyright 2019 Google Inc.
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

package org.trd.maps.utils.x.demo;

import android.content.res.Resources;

import androidx.lifecycle.ViewModel;

import com.huawei.hms.maps.model.LatLng;

import org.trd.maps.clustering.algo.NonHierarchicalViewBasedAlgorithm;
import org.trd.maps.utils.x.demo.model.MyItem;
import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

public class ClusteringViewModel extends ViewModel {

    private NonHierarchicalViewBasedAlgorithm<MyItem> mAlgorithm = new NonHierarchicalViewBasedAlgorithm<>(0, 0);

    NonHierarchicalViewBasedAlgorithm<MyItem> getAlgorithm() {
        return mAlgorithm;
    }

    void readItems(Resources resources) throws JSONException {
        InputStream inputStream = resources.openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mAlgorithm.lock();
        try {
            for (int i = 0; i < 100; i++) {
                double offset = i / 60d;
                for (MyItem item : items) {
                    LatLng position = item.getPosition();
                    double lat = position.latitude + offset;
                    double lng = position.longitude + offset;
                    MyItem offsetItem = new MyItem(lat, lng);
                    mAlgorithm.addItem(offsetItem);
                }
            }
        } finally {
            mAlgorithm.unlock();
        }
    }
}
