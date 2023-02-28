package com.btproject.barberise.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RecyclerViewUtils {

    public static class CustomGridLayoutManager  extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager (Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return false;
        }
    }

}
