package com.harieravie.numerology;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Gravity;
import android.widget.*;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NativeMainActivity extends Activity {
    private EditText input;
    private LinearLayout results;
    private JSONObject meanings;
    private String lastShareText = "";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(23,19,45));
        getWindow().setNavigationBarColor(Color.rgb(23,19,45));
        loadMeanings();
        buildScreen();
    }

    private void loadMeanings() {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(getAssets().open("numerology.json"), "UTF-8"));
            StringBuilder b = new StringBuilder(); String line;
            while ((line = r.readLine()) != null) b.append(line);
            r.close(); meanings = new JSONObject(b.toString());
        } catch (Exception e) { meanings = new JSONObject(); }
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(18), dp(16), dp(18), dp(28));
        page.setBackgroundColor(Color.rgb(246,244,250));
        scroll.addView(page);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER);
        hero.setPadding(dp(18), dp(26), dp(18), dp(26));
        hero.setBackground(roundBg(Color.rgb(35,25,66), Color.rgb(102,68,151), dp(22)));
        TextView brand = text("✦  HARIE RAVIE'S", 12, Color.rgb(220,204,245), true);
        brand.setLetterSpacing(.18f);
        hero.addView(brand);
        TextView title = text("Numerology", 30, Color.WHITE, true);
        title.setPadding(0, dp(8), 0, 0); hero.addView(title);
        TextView sub = text("Pyramid Pattern Calculator", 14, Color.rgb(232,222,247), false);
        sub.setPadding(0, dp(5), 0, 0); hero.addView(sub);
        page.addView(hero, lp(-1,-2,0,0,0,dp(16)));

        LinearLayout form = card();
        TextView label = text("Enter a name or number", 16, Color.rgb(45,35,62), true);
        form.addView(label);
        input = new EditText(this);
        input.setSingleLine(true);
        input.setTextSize(16);
        input.setHint("Example: HARIE RAVIE or 12345");
        input.setPadding(dp(13), dp(12), dp(13), dp(12));
        input.setBackground(roundBg(Color.WHITE, Color.rgb(216,207,230), dp(12)));
        LinearLayout.LayoutParams ip = lp(-1,-2,0,dp(12),0,0);
        form.addView(input, ip);
        TextView hint = text("Letters and numbers are supported. Spaces are ignored.", 12, Color.GRAY, false);
        hint.setPadding(0,dp(7),0,dp(14)); form.addView(hint);

        LinearLayout actions = new LinearLayout(this); actions.setOrientation(LinearLayout.HORIZONTAL);
        Button generate = button("Generate pattern", Color.rgb(104,67,161), Color.WHITE);
        Button clear = button("Clear", Color.rgb(239,235,246), Color.rgb(70,56,91));
        actions.addView(generate, new LinearLayout.LayoutParams(0,dp(48),1));
        actions.addView(clear, lp(90,48,dp(9),0,0,0));
        form.addView(actions);
        Button webVersion = button("Open WebView version", Color.rgb(239,235,246), Color.rgb(70,56,91));
        webVersion.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        form.addView(webVersion, lp(-1, 46, 0, dp(10), 0, 0));
        page.addView(form, lp(-1,-2,0,0,0,dp(15)));

        results = new LinearLayout(this);
        results.setOrientation(LinearLayout.VERTICAL);
        results.setVisibility(View.GONE);
        page.addView(results);

        generate.setOnClickListener(v -> calculate());
        clear.setOnClickListener(v -> { input.setText(""); results.removeAllViews(); results.setVisibility(View.GONE); });
        input.setOnEditorActionListener((v, actionId, event) -> { calculate(); return true; });

        TextView footer = text("Numerology Pyramid Pattern  ·  Offline calculator", 11, Color.rgb(145,137,157), false);
        footer.setGravity(Gravity.CENTER);
        page.addView(footer, lp(-1,-2,0,dp(20),0,0));
        setContentView(scroll);
    }

    private void calculate() {
        String raw = input.getText().toString().replaceAll("\\s+", "");
        if (raw.isEmpty()) { input.setError("Please enter a name or number"); return; }
        List<Integer> digits = new ArrayList<>();
        int total = 0;
        for (int i=0; i<raw.length(); i++) {
            char c = raw.charAt(i);
            int value = Character.isDigit(c) ? Character.getNumericValue(c) : letterValue(c);
            digits.add(value); total += value;
        }
        List<String> rows = new ArrayList<>();
        StringBuilder first = new StringBuilder();
        for (int d : digits) first.append(d);
        rows.add(first.toString());
        List<Integer> current = new ArrayList<>(digits);
        while (current.size() > 1) {
            List<Integer> next = new ArrayList<>();
            for (int i=0; i<current.size()-1; i++) {
                int n=current.get(i)+current.get(i+1);
                if (n>9) n=(n/10)+(n%10);
                next.add(n);
            }
            StringBuilder row=new StringBuilder();
            for (int n:next) row.append(n);
            rows.add(row.toString()); current=next;
        }
        String totalKey = String.format(java.util.Locale.US,"%02d",total);
        String meaning = meanings.optString(totalKey, "No interpretation is available for " + totalKey + ".");
        StringBuilder pyramid = new StringBuilder();
        for (String row:rows) pyramid.append(row).append("\n");

        results.removeAllViews();
        LinearLayout summary=card();
        summary.setBackground(roundBg(Color.rgb(44,31,78),Color.rgb(101,69,151),dp(17)));
        TextView cap=text("TOTAL NUMEROLOGY VALUE",10,Color.rgb(222,207,245),true);
        cap.setGravity(Gravity.CENTER); summary.addView(cap);
        TextView totalView=text(String.valueOf(total),44,Color.WHITE,true);
        totalView.setGravity(Gravity.CENTER); summary.addView(totalView);
        TextView entered=text("Entered value: "+raw,13,Color.rgb(235,225,247),false);
        entered.setGravity(Gravity.CENTER); summary.addView(entered);
        results.addView(summary,lp(-1,-2,0,0,0,dp(13)));

        addSection("Numerology interpretation", meaning);
        addSection("Pyramid pattern", pyramid.toString().trim());

        if (raw.matches("[0-9]+")) {
            StringBuilder pairText=new StringBuilder();
            for(int i=0;i+1<raw.length();i+=2) {
                String pair=raw.substring(i,i+2);
                if(meanings.has(pair)) pairText.append(pair).append(": ").append(meanings.optString(pair)).append("\n\n");
            }
            if(pairText.length()>0) addSection("Two-digit number meanings",pairText.toString().trim());
        }
        lastShareText="Harie Ravie's Numerology\nEntered value: "+raw+"\nTotal: "+total+"\nMeaning: "+meaning+"\n\nPyramid:\n"+pyramid;
        Button share=button("Share result",Color.rgb(235,229,245),Color.rgb(83,54,126));
        share.setOnClickListener(v -> {
            Intent send=new Intent(Intent.ACTION_SEND); send.setType("text/plain");
            send.putExtra(Intent.EXTRA_SUBJECT,"Numerology Result"); send.putExtra(Intent.EXTRA_TEXT,lastShareText);
            startActivity(Intent.createChooser(send,"Share numerology result"));
        });
        results.addView(share,lp(-1,dp(48),0,dp(2),0,dp(12)));
        results.setVisibility(View.VISIBLE);
    }

    private void addSection(String heading,String body) {
        LinearLayout c=card();
        c.addView(text(heading,16,Color.rgb(48,35,67),true));
        TextView content=text(body,15,Color.rgb(55,45,68),false);
        content.setLineSpacing(dp(3),1f);
        content.setPadding(dp(12),dp(12),dp(12),dp(12));
        content.setBackground(roundBg(Color.rgb(248,246,251),Color.rgb(229,222,239),dp(9)));
        c.addView(content,lp(-1,-2,0,dp(11),0,0));
        results.addView(c,lp(-1,-2,0,0,0,dp(12)));
    }

    private int letterValue(char ch) {
        char c=Character.toLowerCase(ch);
        if("aijqy".indexOf(c)>=0)return 1;
        if("bkr".indexOf(c)>=0)return 2;
        if("cgls".indexOf(c)>=0)return 3;
        if("dmt".indexOf(c)>=0)return 4;
        if("ehnx".indexOf(c)>=0)return 5;
        if("uvw".indexOf(c)>=0)return 6;
        if("oz".indexOf(c)>=0)return 7;
        if("fp".indexOf(c)>=0)return 8;
        return 0;
    }

    private LinearLayout card() {
        LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16),dp(16),dp(16),dp(16));
        c.setBackground(roundBg(Color.WHITE,Color.rgb(235,230,242),dp(16)));
        return c;
    }
    private TextView text(String s,int size,int color,boolean bold) {
        TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return t;
    }
    private Button button(String s,int bg,int fg) {
        Button b=new Button(this); b.setText(s); b.setTextSize(13); b.setAllCaps(false);
        b.setTextColor(fg); b.setBackground(roundBg(bg,bg,dp(12))); return b;
    }
    private GradientDrawable roundBg(int color,int stroke,int radius) {
        GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius);
        d.setStroke(dp(1),stroke); return d;
    }
    private int dp(float v) { return (int)(v*getResources().getDisplayMetrics().density+.5f); }
    private LinearLayout.LayoutParams lp(int w,int h,int left,int top,int right,int bottom) {
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(w<0?w:dp(w),h<0?h:dp(h));
        p.setMargins(left,top,right,bottom); return p;
    }
}
