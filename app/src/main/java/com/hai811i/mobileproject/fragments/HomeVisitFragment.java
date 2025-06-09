package com.hai811i.mobileproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.hai811i.mobileproject.R;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HomeVisitFragment extends Fragment {
    private FusedLocationProviderClient fusedLocationClient;
    private MapView mapView;
    private Button btnGetLocation;
    private TextView tvAddress;
    private IGeoPoint selectedLocation;
    private IGeoPoint currentLocation;
    private MapController mapController;
    private MyLocationNewOverlay myLocationOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_visit, container, false);

        // Initialize osmdroid configuration
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Initialize views
        mapView = view.findViewById(R.id.map_view);
        btnGetLocation = view.findViewById(R.id.btn_get_location);
        tvAddress = view.findViewById(R.id.tv_address);

        // Setup map
        setupMap();

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Set up click listeners
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());

        // Handle map clicks
        mapView.getOverlays().add(new GestureDetectorOverlay(mapView, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mapView != null) {
                    Projection projection = mapView.getProjection();
                    selectedLocation = projection.fromPixels((int)e.getX(), (int)e.getY());
                    updateMapWithMarker();
                    getAddressFromLocation(selectedLocation);
                    return true;
                }
                return false;
            }
        }));

        return view;
    }

    private void setupMap() {
        // Add tile source (OpenStreetMap)
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        // Enable multi-touch controls
        mapView.setMultiTouchControls(true);

        // Add rotation gesture
        RotationGestureOverlay rotationGestureOverlay = new RotationGestureOverlay(mapView);
        rotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(rotationGestureOverlay);

        // Set initial zoom level
        mapController = (MapController) mapView.getController();
        mapController.setZoom(15.0);

        // Setup location overlay
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), mapView);
        myLocationOverlay.enableMyLocation();
        mapView.getOverlays().add(myLocationOverlay);
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        mapController.setCenter(currentLocation);
                        myLocationOverlay.setEnabled(true);
                        getAddressFromLocation(currentLocation);
                    } else {
                        Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAddressFromLocation(IGeoPoint geoPoint) {
        // Use Android's built-in Geocoder instead of Nominatim to avoid network requests
        android.location.Geocoder geocoder = new android.location.Geocoder(requireContext(), Locale.getDefault());
        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocation(
                        geoPoint.getLatitude(),
                        geoPoint.getLongitude(),
                        1);

                requireActivity().runOnUiThread(() -> {
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        StringBuilder addressText = new StringBuilder();
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            addressText.append(address.getAddressLine(i));
                            if (i < address.getMaxAddressLineIndex()) {
                                addressText.append(", ");
                            }
                        }
                        tvAddress.setText(addressText.toString());

                        if (currentLocation != null && selectedLocation != null) {
                            showRoute(currentLocation, selectedLocation);
                        }
                    } else {
                        tvAddress.setText("Address not found");
                    }
                });
            } catch (IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Could not get address", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void showRoute(IGeoPoint origin, IGeoPoint dest) {
        // Clear previous overlays (keep first 2 overlays - gestures and location)
        while (mapView.getOverlays().size() > 2) {
            mapView.getOverlays().remove(2);
        }

        // Add markers
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition((GeoPoint) origin);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Current Location");
        mapView.getOverlays().add(startMarker);

        Marker endMarker = new Marker(mapView);
        endMarker.setPosition((GeoPoint) dest);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setTitle("Destination");
        mapView.getOverlays().add(endMarker);

        // Draw a line between points
        Polyline line = new Polyline();
        line.setPoints(Arrays.asList((GeoPoint)origin, (GeoPoint)dest));
        line.setColor(Color.BLUE);
        line.setWidth(5f);
        mapView.getOverlays().add(line);

        mapView.invalidate();
    }

    private void updateMapWithMarker() {
        // Clear previous markers (keep first 2 overlays)
        while (mapView.getOverlays().size() > 2) {
            mapView.getOverlays().remove(2);
        }

        Marker marker = new Marker(mapView);
        marker.setPosition((GeoPoint) selectedLocation);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Selected Location");
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    // MapView lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }



    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Custom GestureDetectorOverlay class
    private static class GestureDetectorOverlay extends org.osmdroid.views.overlay.Overlay {
        private final GestureDetector gestureDetector;

        public GestureDetectorOverlay(MapView mapView, GestureDetector.SimpleOnGestureListener listener) {
            gestureDetector = new GestureDetector(mapView.getContext(), listener);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            return gestureDetector.onTouchEvent(event);
        }
    }
}