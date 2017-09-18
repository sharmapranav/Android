package comp5216.sydney.edu.au.mediaaccess;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * http://archive.oreilly.com/oreillyschool/courses/android2/CameraAdvanced.html
 */
public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {

    public static final String EXTRA_CAMERA_DATA = "camera_data";

    private static final String KEY_IS_CAPTURING = "is_capturing";

    private Camera mCamera;
    private ImageView mCameraImage;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private byte[] mCameraData;
    private boolean mIsCapturing;

    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };

    private OnClickListener mRecaptureImageButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
        }
    };

    private OnClickListener mDoneButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCameraData != null) {

                Bitmap bmp = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
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

//                Toast.makeText(this, "Picture saved!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        mCameraImage.setVisibility(View.INVISIBLE);

        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mCaptureImageButton = (Button) findViewById(R.id.capture);
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);

        final Button doneButton = (Button) findViewById(R.id.save);
        doneButton.setOnClickListener(mDoneButtonClickListener);

        mIsCapturing = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                if (mIsCapturing) {
                    mCamera.startPreview();
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

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCameraData = data;
        setupImageDisplay();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
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

    private void captureImage() {
        mCamera.takePicture(null, null, this);
    }

    private void setupImageCapture() {
        mCameraImage.setVisibility(View.INVISIBLE);
        mCameraPreview.setVisibility(View.VISIBLE);
        mCamera.startPreview();
        mCaptureImageButton.setText("Capture");
        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        mCameraImage.setImageBitmap(bitmap);
        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        mCameraImage.setVisibility(View.VISIBLE);
//        mCaptureImageButton.setText("recapture_image");
        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
    }
    public void onQuit(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}






//
//package comp5216.sydney.edu.au.mediaaccess;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.hardware.Camera;
//import android.hardware.Camera.PictureCallback;
//import android.os.Bundle;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//public class CameraActivity extends Activity implements PictureCallback, SurfaceHolder.Callback {
//
//    public static final String EXTRA_CAMERA_DATA = "camera_data";
//
//    private static final String KEY_IS_CAPTURING = "is_capturing";
//
//    private Camera mCamera;
//    private ImageView mCameraImage;
//    private SurfaceView mCameraPreview;
//    private Button mCaptureImageButton;
//    private byte[] mCameraData;
//    private boolean mIsCapturing;
//
//    private OnClickListener mCaptureImageButtonClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            captureImage();
//        }
//    };
//
//    private OnClickListener mRecaptureImageButtonClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            setupImageCapture();
//        }
//    };
//
//    private OnClickListener mDoneButtonClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (mCameraData != null) {
//                Intent intent = new Intent();
//                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
//                setResult(RESULT_OK, intent);
//            } else {
//                setResult(RESULT_CANCELED);
//            }
//            finish();
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_camera);
//
//        mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
//        mCameraImage.setVisibility(View.INVISIBLE);
//
//        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
//        final SurfaceHolder surfaceHolder = mCameraPreview.getHolder();
//        surfaceHolder.addCallback(this);
//        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//
//        mCaptureImageButton = (Button) findViewById(R.id.capture);
//        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
//
//        final Button doneButton = (Button) findViewById(R.id.save);
//        doneButton.setOnClickListener(mDoneButtonClickListener);
//
//        mIsCapturing = true;
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
//        if (mCameraData != null) {
//            setupImageDisplay();
//        } else {
//            setupImageCapture();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (mCamera == null) {
//            try {
//                mCamera = Camera.open();
//                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
//                if (mIsCapturing) {
//                    mCamera.startPreview();
//                }
//            } catch (Exception e) {
//                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mCamera != null) {
//            mCamera.release();
//            mCamera = null;
//        }
//    }
//
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//        mCameraData = data;
//        setupImageDisplay();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        if (mCamera != null) {
//            try {
//                mCamera.setPreviewDisplay(holder);
//                if (mIsCapturing) {
//                    mCamera.startPreview();
//                }
//            } catch (IOException e) {
//                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//    }
//
//    private void captureImage() {
//        mCamera.takePicture(null, null, this);
//    }
//
//    private void setupImageCapture() {
//        mCameraImage.setVisibility(View.INVISIBLE);
//        mCameraPreview.setVisibility(View.VISIBLE);
//        mCamera.startPreview();
//        mCaptureImageButton.setText("Capture");
//        mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
//    }
//
//    private void setupImageDisplay() {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
//        mCameraImage.setImageBitmap(bitmap);
//        mCamera.stopPreview();
//        mCameraPreview.setVisibility(View.INVISIBLE);
//        mCameraImage.setVisibility(View.VISIBLE);
//        mCaptureImageButton.setText("Capture");
//        mCaptureImageButton.setOnClickListener(mRecaptureImageButtonClickListener);
//    }
//
//    public void onQuit(View view) {
//        setResult(RESULT_CANCELED);
//        finish();
//    }
//}