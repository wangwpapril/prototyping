package com.swishlabs.prototyping.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.swishlabs.prototyping.R;

public class TermsActivity extends AppCompatActivity {

    Button btnDone;
    Button btnAccept;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms);
        btnDone = (Button) findViewById(R.id.done_button);
        btnDone.setEnabled(false);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        btnAccept = (Button) findViewById(R.id.accept_button);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDone.setEnabled(true);
                btnAccept.setVisibility(View.GONE);
            }
        });

        tvContent = (TextView) findViewById(R.id.terms_content);
        String source = "<b><i>It's simple and easy to use!</i></b><br><br>" +
                "With your permission, <b>GrabOp GO</b> uses GPS to determine your location. By signing in with your Facebook or GrabOp account, we can transfer information to make it easier for you to create your <b>GrabOp GO</b> profile.<br><br>"
                +"Based on the search criteria you have selected, <b>GrabOp GO</b> finds potential matches that are near you. Then it conveniently determines the number of direct opportunities you may have with each person, helping you to be more productive with your networking." +
                "<br><br>If you are interested in establishing a connection, swipe right to <b>'connect'</b> with that person. If they also swipe right to <b>'connect'</b> with you...then voilà it’s a match! Now, your virtual business cards are automatically exchanged and stored in each others' contact list. If on the other hand, you are not interested in establishing a connection, simply swipe left to 'pass'." +
                "<br><br>This is how <b>GrabOp GO</b> connects you to opportunities!";

        tvContent.setText(Html.fromHtml(source));

    }
}
