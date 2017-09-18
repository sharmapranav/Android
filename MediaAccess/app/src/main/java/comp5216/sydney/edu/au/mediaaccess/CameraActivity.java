package comp5216.sydney.edu.au.mediaaccess;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Reference http://archive.oreilly.com/oreillyschool/courses/android2/CameraAdvanced.html
 */
public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {

    private static final String KEY_IS_CAPTURING = "is_capturing";

    private Camera camera;
    private ImageView imageView;
    private SurfaceView surfaceView;
    private byte[] photoData;
    private boolean isCapturing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.camera_image_view);
        imageView.setVisibility(View.INVISIBLE);

        surfaceView = findViewById(R.id.preview_view);

        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        isCapturing = true;
    }

    public void onCapture(View v) {

        if (surfaceView.getVisibility() == View.VISIBLE) {
            captureImage();
        } else {
            setupImageCapture();
        }
    }

    private void captureImage() {
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        photoData = data;
        setupImageDisplay();
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
        imageView.setImageBitmap(bitmap);
        camera.stopPreview();
        surfaceView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    private void setupImageCapture() {
        imageView.setVisibility(View.INVISIBLE);
        surfaceView.setVisibility(View.VISIBLE);
        camera.startPreview();
    }

    public void onSave(View v) {
        if (photoData != null && imageView.getVisibility() == View.VISIBLE) {

            Bitmap bmp = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            String photoPath = getIntent().getStringExtra("photoPath");

            FileOutputStream fs = null;
            try {
                fs = new FileOutputStream(photoPath);
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fs);
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } finally {
                try {
                    fs.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            Toast.makeText(this, "Picture was saved!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nothing to save",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onQuit(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, isCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        isCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, photoData == null);
        if (photoData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (camera == null) {
            try {
                camera = Camera.open();
                camera.setPreviewDisplay(surfaceView.getHolder());
                if (isCapturing) {
                    camera.startPreview();
                }
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                if (isCapturing) {
                    camera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
