package gotenna.lakshmi.testmapbox.view;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.InputStream;
import java.net.MalformedURLException;

import gotenna.lakshmi.testmapbox.R;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    protected Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);

            }
        });

    }


    static String loadGeoJsonFromAsset(Context context, String filename) {
        try {
            // Load GeoJSON file from local asset folder
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }



    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {


            mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {

               @Override
                public void onStyleLoaded(@NonNull Style style) {
                    initLayerIcons(style);
                   try {
                       addClusteredGeoJsonSource(style);
                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   }
                   Toast.makeText(MainActivity.this, R.string.zoom_map_in_and_out_instruction,
                            Toast.LENGTH_LONG).show();
                }
            });
    }

    private void initLayerIcons(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("single-quake-icon-id", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_pin_drop)));
        loadedMapStyle.addImage("quake-triangle-icon-id", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_accessibility)));
    }

    private void addClusteredGeoJsonSource(@NonNull Style loadedMapStyle) throws MalformedURLException {
        String geoJson = loadGeoJsonFromAsset(this, "gotenna.geojson");
        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        Log.v("geojson",geoJson);
        loadedMapStyle.addSource(
                 new GeoJsonSource("users",geoJson,
                        new GeoJsonOptions()
                                .withCluster(true)
                                .withClusterMaxZoom(60)
                                .withClusterRadius(50)
                )
                //new GeoJsonSource(geoJson)
        );

        loadedMapStyle.addLayer(new SymbolLayer("unclustered-points", "users").withProperties(iconImage("single-quake-icon-id")));

        int[] layers = new int[] {50,70};

        for (int i = 0; i < layers.length; i++) {
            //Add clusters' SymbolLayers images
            SymbolLayer symbolLayer = new SymbolLayer("cluster-" + i, "users");

            symbolLayer.setProperties(
                    iconImage("quake-triangle-icon-id")
            );
            //loadedMapStyle.addLayer(symbolLayer);


            Expression pointCount = toNumber(get("point_count"));

            // Add a filter to the cluster layer that hides the icons based on "point_count"
            symbolLayer.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i]))
                    ) : all(has("point_count"),
                            gt(pointCount, literal(layers[i])),
                            lt(pointCount, literal(layers[i - 1]))
                    )
            );
            loadedMapStyle.addLayer(symbolLayer);
        }

        //Add a SymbolLayer for the cluster data number point count
        loadedMapStyle.addLayer(new SymbolLayer("count", "users").withProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.BLACK),
                textIgnorePlacement(true),
                textOffset(new Float[] {0f, .5f}),
                textAllowOverlap(true)
        ));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

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

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
