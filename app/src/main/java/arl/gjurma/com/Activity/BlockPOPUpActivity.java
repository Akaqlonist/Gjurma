package arl.gjurma.com.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by KRYTON on 07-10-2016.
 */
public class BlockPOPUpActivity extends AppCompatActivity {
    @Bind(arl.gjurma.com.R.id.toolbar)
    Toolbar toolbar;
    @Bind(arl.gjurma.com.R.id.tv_title)
    TextView tvTitle;
    @Bind(arl.gjurma.com.R.id.tv_message)
    TextView tvMessage;
    @Bind(arl.gjurma.com.R.id.tv_link)
    TextView tvLink;

    private String mLink;
    private String mMessage;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(arl.gjurma.com.R.layout.activity_popup);
        ButterKnife.bind(this);

        toolbar.setTitle("GJURMA");

        mMessage = getIntent().getStringExtra("message");
        mTitle = getIntent().getStringExtra("title");
        mLink = getIntent().getStringExtra("link");
        tvMessage.setText(mMessage);
        tvTitle.setText(mTitle);
    }

    @OnClick(arl.gjurma.com.R.id.tv_link)
    public void onClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));
        startActivity(browserIntent);
    }
}
