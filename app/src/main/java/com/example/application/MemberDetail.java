package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.Supplier;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context _this = MemberDetail.this;
        final Intent intent = this.getIntent();
        final ItemDetail query = new ItemDetail(_this);
        query.seq = Integer.parseInt(intent.getExtras().getString("seq"));
        final Main.Member member = query.get();
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources()
                        .getDrawable(
                                getResources()
                                        .getIdentifier(
                                                _this.getPackageName()+":drawable/"+member.photo,
                                                null,
                                                null
                                        )
                        )
        );
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        TextView phone = findViewById(R.id.phone);
        TextView addr = findViewById(R.id.addr);
        name.setText(member.name);
        email.setText(member.email);
        phone.setText(member.phone);
        addr.setText(member.addr);

        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_this, MemberUpdate.class);
                intent.putExtra("spec" , String.format("%s,%s,%s,%s,%s,%s",
                        member.seq, member.name, member.email ,member.phone, member.addr, member.photo));

                startActivity(intent);

            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(_this, MemberList.class));
            }
        });


    }
    private class MemberDetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDetailQuery(Context _this) {
            super(_this);
            helper = new Main.SQLiteHelper(_this);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends MemberDetailQuery{
        int seq;
        public ItemDetail(Context _this) {
            super(_this);
        }
        public Main.Member get(){
            Main.Member member = null;
            Toast.makeText(_this, "쿼리에 들어갈 seq "+seq, Toast.LENGTH_LONG).show();
            Cursor cursor = getDatabase()
                    .rawQuery(String.format("SELECT * FROM %s WHERE %s LIKE '%s'",
                            Main.MEMBERS, Main.SEQ, seq),null);
            if(cursor != null && cursor.moveToNext()){
                member = new Main.Member();
                member.seq = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Main.SEQ)));
                member.name = cursor.getString(cursor.getColumnIndex(Main.NAME));
                member.passwd = cursor.getString(cursor.getColumnIndex(Main.PASSWD));
                member.email = cursor.getString(cursor.getColumnIndex(Main.EMAIL));
                member.phone = cursor.getString(cursor.getColumnIndex(Main.PHONE));
                member.addr = cursor.getString(cursor.getColumnIndex(Main.ADDR));
                member.photo = cursor.getString(cursor.getColumnIndex(Main.PHOTO));
            }
            return member;
        }
    }
}