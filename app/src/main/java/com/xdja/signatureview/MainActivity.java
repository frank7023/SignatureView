package com.xdja.signatureview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.eleven.library.SignatureView;

public class MainActivity extends AppCompatActivity {
    private SignatureView mSignatureView;
    private Button btnClear, btnSave;
    private ImageView imvShowResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignatureView = (SignatureView) findViewById(R.id.signature_view);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSave = (Button) findViewById(R.id.btn_save);
        imvShowResult = (ImageView) findViewById(R.id.imv_show);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignatureView.clear();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = mSignatureView.createViewBitmap();
                imvShowResult.setImageBitmap(bitmap);
            }
        });
    }
}
