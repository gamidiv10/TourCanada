package com.example.cloudproject;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class SampleticketActivity extends AppCompatActivity {
    private TextView ticketId;
    private TextView source;
    private TextView departureDate;
    private TextView price;
    private ImageView bCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampleticket);
        ticketId = findViewById(R.id.ticket_id);
        source = findViewById(R.id.src);
        departureDate = findViewById(R.id.departure_date);
        price = findViewById(R.id.price);
        bCode = findViewById(R.id.bar_code);

        String a1 = getIntent().getExtras().getString("source");

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(a1, BarcodeFormat.CODE_128,400,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            bCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        ticketId.setText("Ticket1123");
        String src = a1;
        String dest = getIntent().getExtras().getString("destination");
        source.setText(src + " to " + dest);
        String departDate = getIntent().getExtras().getString("departDate");
        String returnDate = getIntent().getExtras().getString("returnDate");
        departureDate.setText(departDate + " to " + returnDate);
        String ticketPrice = getIntent().getExtras().getString("ticketPrice");
        price.setText("Ticket Price: $" + ticketPrice);

    }
}
