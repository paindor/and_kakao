package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context _this = MemberUpdate.this;
        Log.d("넘어온값"  , this.getIntent().getExtras().toString());
        final String[] arr = this.getIntent()
                .getStringExtra("spec")
                .split(",");

        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources()
                .getDrawable(
                        getResources().getIdentifier(
                                _this.getPackageName()+":drawable/" +arr[5],
                                null,null
                        )
                )
        );


        final TextView name = findViewById(R.id.name);
        final TextView email = findViewById(R.id.changeEmail);
        final TextView phone = findViewById(R.id.changePhone);
        final TextView addr = findViewById(R.id.changeAddress);
        name.setText(arr[1]);
        email.setText(arr[2]);
        phone.setText(arr[3]);
        addr.setText(arr[4]);

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemUpdate itemUpdate = new ItemUpdate(_this);
                itemUpdate.name = name.getText().toString();
                itemUpdate.email = email.getText().toString();
                itemUpdate.phone = phone.getText().toString();
                itemUpdate.addr = addr.getText().toString();
                itemUpdate.seq = arr[0];



                itemUpdate.run();
                Intent intent = new Intent(_this, MemberDetail.class);
                intent.putExtra("seq" , arr[0]);
                startActivity(intent);



            }
        });

    }
    private class MemberUpdateQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberUpdateQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemUpdate extends MemberUpdateQuery {
        String name, email, phone, addr, seq;
        public ItemUpdate(Context _this) {
            super(_this);
        }
        public void run(){
            String sql = String.format(
                    " UPDATE %s\n" +
                            "     SET %s = '%s',\n" +
                            "         %s = '%s',\n" +
                            "         %s = '%s',\n" +
                            "         %s = '%s'\n" +
                            "     WHERE %s LIKE '%s'",
                    Main.MEMBERS, Main.NAME, name,
                    Main.EMAIL, email,
                    Main.PHONE, phone,
                    Main.ADDR, addr, Main.SEQ, seq);
            getDatabase().execSQL(sql);

        }
    }
}
