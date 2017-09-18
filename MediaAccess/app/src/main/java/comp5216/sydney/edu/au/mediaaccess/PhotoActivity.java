package comp5216.sydney.edu.au.mediaaccess;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoActivity extends Activity {

    String photoPath;
    Bitmap originalPhoto;
    Bitmap editedPhoto;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);

        photoPath = getIntent().getStringExtra("photoPath");
        originalPhoto = BitmapFactory.decodeFile(photoPath);
        editedPhoto = originalPhoto.copy(originalPhoto.getConfig(), true);

        imageView = findViewById(R.id.imageview);
        imageView.setImageBitmap(originalPhoto);
    }

    /**
     * https://gist.github.com/firzan/5848737
     *
     * @param view
     */
    public void onGrey(View view) {
        originalPhoto = null;
        originalPhoto = editedPhoto.copy(editedPhoto.getConfig(), true);

        int width = editedPhoto.getWidth();
        int height = editedPhoto.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // get pixel color
                int pixel = editedPhoto.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                } else {
                    gray = 0;
                }
                try {
                    // set new pixel color to output bitmap
                    editedPhoto.setPixel(x, y, Color.argb(A, gray, gray, gray));
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        imageView.setImageBitmap(editedPhoto);
    }

    public void onCrop(View view) {
        originalPhoto = null;
        originalPhoto = editedPhoto.copy(editedPhoto.getConfig(), true);

        EditText l = findViewById(R.id.left_crop);
        EditText t = findViewById(R.id.top_crop);
        EditText r = findViewById(R.id.right_crop);
        EditText b = findViewById(R.id.bottom_crop);

        int cropL = stringToInt(l.getText().toString());
        int cropT = stringToInt(t.getText().toString());
        int cropR = originalPhoto.getWidth() - stringToInt(r.getText().toString());
        int cropB = originalPhoto.getHeight() - stringToInt(b.getText().toString());

        try {
            editedPhoto = editedPhoto.createBitmap(editedPhoto, cropL, cropT, cropR - cropL, cropB - cropT);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        imageView.setImageBitmap(editedPhoto);

        l.setText("");
        t.setText("");
        r.setText("");
        b.setText("");
    }

    private int stringToInt(String text) {
        if (text != null && !text.isEmpty())
            return Integer.parseInt(text);
        return 0;
    }

    public void onUndo(View view) {
        editedPhoto = originalPhoto.copy(originalPhoto.getConfig(), true);
        imageView.setImageBitmap(editedPhoto);
    }

    public void onSave(View view) {
        File file = new File(photoPath);
        file.delete();

        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(photoPath);
            editedPhoto.compress(Bitmap.CompressFormat.JPEG, 100, fs);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                fs.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
