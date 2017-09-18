package comp5216.sydney.edu.au.mediaaccess;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public final String APP_TAG = "MobileComputingTutorial";
    public String photoFileName = "photo.jpg";

    // request codes
    private static final int OPEN_PHOTO = 100;
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 101;

    MarshMallowPermission marshMallowPermission;
    GridView photoGrid;
    ArrayList<Bitmap> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marshMallowPermission = new MarshMallowPermission(this);

        if (!marshMallowPermission.checkPermissionForReadfiles()) {
            marshMallowPermission.requestPermissionForReadfiles();
        }

        photos = new ArrayList();
        photoGrid = (GridView) findViewById(R.id.gridview);
        photoGrid.setAdapter(new ImageAdapter(this, photos));
        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String photoPath = getPhotoFileName(i);

                Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                intent.putExtra("photoPath", photoPath);

                startActivityForResult(intent, OPEN_PHOTO);
            }
        });

        loadPhotos();

        System.out.println("package name: " + getPackageName());
    }

    public void loadPhotos() {

        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory().getPath(),
                "/images/"
        );

        if (mediaStorageDir.listFiles() == null) {
            return;
        }

        photos.clear();

        for (File file : mediaStorageDir.listFiles()) {
            Bitmap photo = BitmapFactory.decodeFile(file.getPath());
            photos.add(photo);
        }
    }

    public String getPhotoFileName(int index) {
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory().getPath(),
                "/images/"
        );

        File photoFile = mediaStorageDir.listFiles()[index];
        return photoFile.getPath();
    }

    // Returns the Uri for a photo/media stored on disk given the fileName
    public Uri getFileUri(String fileName, int type) {
        String typestr = "/images/";

        // Get safe storage directory for photos
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory().getPath(),
                typestr + fileName
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.getParentFile().exists() && !mediaStorageDir.getParentFile().mkdirs()) {
            Log.d(APP_TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        Uri photoURI = null;
        if (Build.VERSION.SDK_INT >= 24) {
            photoURI = FileProvider.getUriForFile(
                    this.getApplicationContext(),
                    "comp5216.sydney.edu.au.mediaaccess.fileProvider",
                    mediaStorageDir);
        } else {
            photoURI = Uri.fromFile(mediaStorageDir);
        }

        return photoURI;
    }

    public void onTakePhotoClick(View v) {
        // Check permissions
        if (!marshMallowPermission.checkPermissionForCamera()
                || !marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForCamera();
        } else {
            // create Intent to take a picture and return control to the calling application
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // set file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            photoFileName = "IMG_" + timeStamp + ".jpg";
            Uri file_uri = getFileUri(photoFileName, 0);
            System.out.println(file_uri);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);

            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            File mediaStorageDir = new File(
                    Environment.getExternalStorageDirectory().getPath(),
                    "/images/"
            );
            String path = mediaStorageDir.getAbsolutePath() + "/" + photoFileName;
            intent.putExtra("photoPath", path);
            startActivityForResult(intent, MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_PERMISSIONS_REQUEST_OPEN_CAMERA) {
            if (resultCode == RESULT_OK) {
                loadPhotos();
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == OPEN_PHOTO) {
            if (resultCode == RESULT_OK) {
                loadPhotos();
            }
        }
    }
}
