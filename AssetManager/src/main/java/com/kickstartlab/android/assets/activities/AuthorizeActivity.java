package com.kickstartlab.android.assets.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.utils.DbDateUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AuthorizeActivity extends ActionBarActivity {

    SignaturePad mSignaturePad;

    ButtonFlat mSaveButton, mClearButton;

    TextView mSignDate;

    MaterialEditText mFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSaveButton = (ButtonFlat) findViewById(R.id.save_sign);
        mClearButton = (ButtonFlat) findViewById(R.id.clear_sign);

        mFullName = (MaterialEditText) findViewById(R.id.full_name);

        mSignDate = (TextView) findViewById(R.id.sign_date);

        mSignDate.setText(DbDateUtil.getSignDate());

        mClearButton.setEnabled(false);
        mSaveButton.setEnabled(false);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onSigned() {
                mClearButton.setEnabled(true);
                mSaveButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mClearButton.setEnabled(false);
                mSaveButton.setEnabled(false);
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                String signText = new StringBuilder().append(mSignDate.getText()).append(" ").append(mFullName.getText()).toString();
                if(addSignatureToGallery(signatureBitmap, signText)) {
                    Toast.makeText(AuthorizeActivity.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthorizeActivity.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo, String text) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);

        if("".equalsIgnoreCase(text)){
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.BLUE);
            paintText.setTextSize(50);
            paintText.setStyle(Paint.Style.FILL);
            Rect rectText = new Rect();
            paintText.getTextBounds(text, 0, text.length(), rectText);
            canvas.drawText(text, 10, rectText.height(), paintText);
        }

        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature, String text) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("signatures"), String.format("Sign_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo, text);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
