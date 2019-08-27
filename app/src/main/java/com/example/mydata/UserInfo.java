package com.example.mydata;

import androidx.appcompat.app.AppCompatActivity;

public class UserInfo extends AppCompatActivity {
    public String ph;
    public String ad;
    public UserInfo()
    {

    }

    public UserInfo(String ph, String ad) {
        this.ph = ph;
        this.ad = ad;
  }

    public String getPh() {
        return ph;
    }

    public String getAd() {
        return ad;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }
}
