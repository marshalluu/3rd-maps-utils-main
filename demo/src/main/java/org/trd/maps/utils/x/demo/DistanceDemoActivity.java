/*
 * Copyright 2013 Google Inc.
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

import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.Polyline;
import com.huawei.hms.maps.model.PolylineOptions;

import org.trd.maps.SphericalUtil;

import java.util.Arrays;

public class DistanceDemoActivity extends BaseDemoActivity implements HuaweiMap.OnMarkerDragListener {
    private TextView mTextView;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Polyline mPolyline;

    @Override
    protected int getLayoutId() {
        return R.layout.distance_demo;
    }

    @Override
    protected void startDemo(boolean isRestore) {
        mTextView = findViewById(R.id.textView);

        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8256, 151.2395), 10));
        }
        getMap().setOnMarkerDragListener(this);

        mMarkerA = getMap().addMarker(new MarkerOptions().position(new LatLng(-33.9046, 151.155)).draggable(true));
        mMarkerB = getMap().addMarker(new MarkerOptions().position(new LatLng(-33.8291, 151.248)).draggable(true));
        mPolyline = getMap().addPolyline(new PolylineOptions().geodesic(true));

        Toast.makeText(this, "Drag the markers!", Toast.LENGTH_LONG).show();
        showDistance();
    }

    private void showDistance() {
        double distance = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerB.getPosition());
        mTextView.setText("The markers are " + formatNumber(distance) + " apart.");
    }

    private void updatePolyline() {
        mPolyline.setPoints(Arrays.asList(mMarkerA.getPosition(), mMarkerB.getPosition()));
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        showDistance();
        updatePolyline();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        showDistance();
        updatePolyline();
    }
}
